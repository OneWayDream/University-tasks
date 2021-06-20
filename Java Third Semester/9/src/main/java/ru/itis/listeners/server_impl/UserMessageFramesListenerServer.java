package ru.itis.listeners.server_impl;

import ru.itis.protocol.Message;
import ru.itis.protocol.exceptions.MessageException;
import ru.itis.server.exceptions.ServerEventListenerException;
import ru.itis.server.exceptions.ServerException;
import ru.itis.listeners.AbstractServerEventListener;
import ru.itis.server.utils.ServerConnection;

import java.nio.ByteBuffer;
import java.util.Arrays;


public class UserMessageFramesListenerServer extends AbstractServerEventListener {

    //Listener for user messages
    protected final static int TYPE = 10;

    @Override
    public void handle(ServerConnection client, Message message) throws ServerEventListenerException {
        if (!this.init){
            throw new ServerEventListenerException("Listener hasn't been initiated yet.");
        }
        try{
            String clientName = client.getClientNickname();
            ByteBuffer byteBuffer = ByteBuffer.allocate(clientName.length()*2 + 8 + message.getData().length);
            for (int i = 0; i < clientName.length(); i++){
                byteBuffer.putChar(clientName.charAt(i));
            }
            byteBuffer.putChar(' ');
            byteBuffer.putChar('=');
            byteBuffer.putChar('>');
            byteBuffer.putChar(' ');
            byte[] messageData = message.getData();
            for (int i = 0; i < messageData.length; i++){
                byteBuffer.put(messageData[i]);
            }
            server.sendBroadCastMessage(Message.createMessage(63, byteBuffer.array()), client);
            System.out.println("Listener " + this.getClass().getSimpleName()
                    + " successfully handled [" + Arrays.toString(byteBuffer.array()) + "].");
        } catch (MessageException ex) {
            throw new ServerEventListenerException("Unexpected exception during creating message.", ex);
        } catch (ServerException ex) {
            throw new ServerEventListenerException("Cannot send broadcast.", ex);
        }
    }

    @Override
    public int getType() {
        return TYPE;
    }

}
