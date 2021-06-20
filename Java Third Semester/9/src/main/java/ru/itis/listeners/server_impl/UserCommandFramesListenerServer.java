package ru.itis.listeners.server_impl;

import ru.itis.protocol.Message;
import ru.itis.server.exceptions.ServerEventListenerException;
import ru.itis.listeners.AbstractServerEventListener;
import ru.itis.server.utils.ServerConnection;

import java.nio.ByteBuffer;

public class UserCommandFramesListenerServer extends AbstractServerEventListener {

    //Listener for user commands
    protected final static int TYPE = 11;

    @Override
    public void handle(ServerConnection client, Message message) throws ServerEventListenerException {
        if (!this.init){
            throw new ServerEventListenerException("Listener hasn't been initiated yet.");
        }
        ByteBuffer buffer = ByteBuffer.wrap(message.getData());
        buffer.rewind();
        StringBuilder userCommand = new StringBuilder();
        for (int i = 0; i<buffer.array().length/2; i++){
            userCommand.append(buffer.getChar());
        }
        switch (userCommand.toString()){
            case "/nickname":
                //TODO TODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODO
                break;
            default:
                //ignore
                break;
        }
    }

    @Override
    public int getType() {
        return TYPE;
    }


}
