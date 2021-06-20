package ru.itis.listeners.server_impl;

import ru.itis.protocol.Message;
import ru.itis.protocol.exceptions.MessageException;
import ru.itis.server.exceptions.ServerEventListenerException;
import ru.itis.listeners.AbstractServerEventListener;
import ru.itis.server.exceptions.ServerException;
import ru.itis.server.utils.ServerConnection;

import java.nio.ByteBuffer;

public class ServiceFramesListenerServer extends AbstractServerEventListener {

    //Some simple client service frames:
    //exit - close client handler thread.
    protected final static int TYPE = 0;

    @Override
    public void handle(ServerConnection client, Message message) throws ServerEventListenerException {
        if (!this.init){
            throw new ServerEventListenerException("Listener hasn't been initiated yet.");
        }
        ByteBuffer buffer = ByteBuffer.wrap(message.getData());
        buffer.rewind();
        StringBuilder messageContent = new StringBuilder();
        for (int i = 0; i<buffer.array().length/2; i++){
            messageContent.append(buffer.getChar());
        }
        switch (messageContent.toString()){
            case "/exit":
                try{
                    ByteBuffer disconnectBuffer = ByteBuffer.allocate(22);
                    String disconnectCommand = "/disconnect";
                    for (int i = 0; i < disconnectCommand.length(); i++){
                        disconnectBuffer.putChar(disconnectCommand.charAt(i));
                    }
                    server.sendMessage(client, Message.createMessage(0, disconnectBuffer.array()));
                    client.getClientHandleThread().stopThread();
                    server.getConnections().remove(client);
                    System.out.println("Client " + client.getClientNickname() + " was disconnected.");
                    String notMessage = "User " + client.getClientNickname() + " leave chat.";
                    ByteBuffer notBuffer = ByteBuffer.allocate(notMessage.length()*2);
                    for (int i = 0; i < notMessage.length(); i++){
                        notBuffer.putChar(notMessage.charAt(i));
                    }
                    server.sendBroadCastMessage(Message.createMessage(63, notBuffer.array()));
                } catch (MessageException| ServerException ex){
                    System.out.println("Cannot disconnect user " + client.getClientNickname());
                    //throw new IllegalArgumentException(ex);
                }
                break;
            default:
                //ignore
                break;
        }
        System.out.println("Listener " + this.getClass().getSimpleName()
                + " successfully handled [" + messageContent + "].");
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
