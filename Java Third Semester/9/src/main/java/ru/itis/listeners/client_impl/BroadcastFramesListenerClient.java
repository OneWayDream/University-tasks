package ru.itis.listeners.client_impl;

import ru.itis.client.Client;
import ru.itis.client.exceptions.ClientEventListenerException;
import ru.itis.listeners.AbstractClientEventListener;
import ru.itis.protocol.Message;
import ru.itis.server.exceptions.ServerEventListenerException;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class BroadcastFramesListenerClient extends AbstractClientEventListener {

    //Some simple client service frames:
    //exit - close client handler thread.
    protected final static int TYPE = 63;

    @Override
    public void handle(Client client, Message message) throws ClientEventListenerException {
        if (!this.init){
            throw new ClientEventListenerException("Listener hasn't been initiated yet.");
        }
        ByteBuffer buffer = ByteBuffer.wrap(message.getData());
        buffer.rewind();
        StringBuilder messageContent = new StringBuilder();
        for (int i = 0; i<buffer.array().length/2; i++){
            messageContent.append(buffer.getChar());
        }
        System.out.println(messageContent);
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
