package ru.itis.listeners.client_impl;

import ru.itis.AppClient;
import ru.itis.client.Client;
import ru.itis.client.exceptions.ClientEventListenerException;
import ru.itis.listeners.AbstractClientEventListener;
import ru.itis.protocol.Message;

import java.nio.ByteBuffer;

public class ServiceFramesListenerClient extends AbstractClientEventListener {

    //Some simple client service frames:
    //exit - close client handler thread.
    protected final static int TYPE = 0;

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
        switch (messageContent.toString()){
            case "/disconnect":
                AppClient.disconnectClient();
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
