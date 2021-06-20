package ru.itis.protocol;


import lombok.*;
import ru.itis.protocol.exceptions.FrameContentException;
import ru.itis.protocol.exceptions.MessageException;
import ru.itis.server.exceptions.ClientConnectionException;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

// Message Content : [PR][SFD][TYPE][L][DATA][FCS]
// PR (preamble) - preamble for chat frames (11011100); [8 bit]
// SFD (start frame delimiter) - SFD for char frames (10101010); [8 bit]
// TYPE - type of frame content :
//                              [0 - 9] - service frame
//                              10 - user text message
//                              11 - user command;
//                              63 - broadcast message to chat
//                              [8 bit]
// L (length) - frame data length; [16 bit]
// DATA - frame data; [0 - 65536 byte]
//FCS (frame check sequence) - frame check for chat frames; [8 bit]
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Message {

    protected static final int MAX_LENGTH = 1024;
    protected static final int MAX_TYPE_VALUE = 63;
    protected static final int MIN_TYPE_VALUE = 0;
    protected static final byte PR = (byte) 0xCD;
    protected static final byte SFD = (byte) 0xAA;

    protected final byte[] data;
    protected final int type;

    public static Message createMessage (int type, byte[] data) throws MessageException {
        if (data.length>MAX_LENGTH){
            throw new MessageException("Message can't be " + data.length + " byte length. "
             + "Max length is " + MAX_LENGTH + ".");
        }
        if ((type > MAX_TYPE_VALUE)||(type < MIN_TYPE_VALUE)){
            throw new MessageException("Unexpected message type");
        }
        return Message.builder()
                .type(type)
                .data(data)
                .build();
    }

    public static byte[] getBytes(Message message) throws MessageException{
        if (message.getData().length>MAX_LENGTH){
            throw new MessageException("Message can't be " + message.getData().length + " byte length. "
                    + "Max length is " + MAX_LENGTH + ".");
        }
        if ((message.getType() > MAX_TYPE_VALUE)||(message.getType() < MIN_TYPE_VALUE)){
            throw new MessageException("Unexpected message type");
        }
        int messageLength = 1 + 1 + 1 + 2 + message.getData().length + 1;
        byte[] messageBytes = new byte[messageLength];
        int j = 0;
        messageBytes[j++] = PR;
        messageBytes[j++] = SFD;
        messageBytes[j++] = (byte) message.getType();
        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
        byte[] lengthBytes = byteBuffer.putShort((short) message.getData().length).array();
        for (int i = 0; i<lengthBytes.length; i++){
            messageBytes[j++] = lengthBytes[i];
        }
        byte[] data = message.getData();
        for (int i = 0; i <data.length; i++){
            messageBytes[j++] = data[i];
        }
        long byteSum = 0;
        for (int i = 0; i <messageBytes.length; i++){
            byteSum+=messageBytes[i];
        }
        messageBytes[j] = (byte)(byteSum%3 << 6 | byteSum%5 << 3 | byteSum%7);
        return messageBytes;
    }

    public static String toString(Message message) throws MessageException{
        StringBuilder result = new StringBuilder();
        String delimiter = " ";
        String n = System.getProperty("line.separator");
        byte[] messageBytes = Message.getBytes(message);
        result.append("Preamble:").append(delimiter).append(messageBytes[0]);
        result.append(n);
        result.append("SFD:").append(delimiter).append(messageBytes[1]);
        result.append(n);
        result.append("Type:").append(delimiter).append(messageBytes[2]);
        result.append(n);
        short dataLength = ByteBuffer.wrap(messageBytes, 3, 2).getShort();
        result.append("Length:").append(delimiter).append(dataLength);
        result.append(n);
        result.append("Data:").append(delimiter).append(Arrays.toString(Arrays.copyOfRange(messageBytes, 5, 5 + dataLength)).replace(", ", delimiter));
        result.append(n);
        result.append("FCS:").append(delimiter).append(messageBytes[messageBytes.length-1]);
        return result.toString();
    }

    public static Message readMessage(InputStream in) throws MessageException, FrameContentException, ClientConnectionException {
        try{
            Message result = null;
            byte pr = (byte) in.read();
            if (pr==PR){
                byte sfd = (byte) in.read();
                if (sfd==SFD){
                    int type = in.read();
                    if ((type<=MAX_TYPE_VALUE)&&(type>=MIN_TYPE_VALUE)){
                        int length = ByteBuffer.wrap(new byte[]{(byte) in.read(), (byte) in.read()}).getShort();
                        byte[] messageData = new byte[5 + length];
                        messageData[0] = pr;
                        messageData[1] = sfd;
                        messageData[2] = (byte) type;
                        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
                        byteBuffer.putShort((short) length);
                        byteBuffer.rewind();
                        messageData[3] = byteBuffer.get();
                        messageData[4] = byteBuffer.get();
                        for (int i = 5; i < length + 5; i++){
                            messageData[i] = (byte) in.read();
                        }
                        int byteSum = 0;
                        for (int i = 0; i < messageData.length; i++){
                            byteSum+=messageData[i];
                        }
                        byte fcs = (byte) in.read();
                        byte currentFCS = (byte) (byteSum%3 << 6 | byteSum%5 << 3 | byteSum%7);
                        if (currentFCS == fcs){
                            result = Message.builder()
                                    .type(type)
                                    .data(Arrays.copyOfRange(messageData, 5, messageData.length))
                                    .build();
                        } else {
                            throw new FrameContentException("Incorrect FCS-header in current frame.");
                        }
                    }
                }
            }
            return result;
        } catch (SocketException ex){
            throw new ClientConnectionException(ex);
        } catch (IOException ex){
            throw new MessageException("Exception with frames work.", ex);
        }
    }
}
