package ru.itis.client.utils;

import ru.itis.AppClient;
import ru.itis.client.Client;
import ru.itis.client.exceptions.ClientEventListenerException;
import ru.itis.client.exceptions.ClientException;
import ru.itis.listeners.ClientEventListener;
import ru.itis.protocol.Message;
import ru.itis.protocol.exceptions.FrameContentException;
import ru.itis.protocol.exceptions.MessageException;
import ru.itis.server.exceptions.ClientConnectionException;
import ru.itis.server.exceptions.ClientThreadException;

public class ServerHandleThread implements Runnable {

    protected Client client;
    protected boolean isWork;

    public ServerHandleThread(Client client){
        this.client = client;
    }

    @Override
    public void run() {
        isWork = true;
        while (isWork){
            try{
                Message message = Message.readMessage(client.getInputStream());
                if (message!=null){
                    for (ClientEventListener listener : client.getListeners()){
                        if (listener.getType() == message.getType()){
                            listener.handle(client, message);
                        }
                    }
                }
            } catch (FrameContentException ex) {
                //TODO send request to repeat frame;
            } catch (MessageException ex) {
                throw new ClientThreadException("Exception during getting client frame.", ex);
            } catch (ClientEventListenerException ex) {
                throw new ClientThreadException("Exception during handling user frame.", ex);
            } catch (ClientException ex) {
                throw new ClientThreadException("Exception in user connection", ex);
            } catch (ClientConnectionException e) {
                System.out.println("You was disconnected from chat server.");
                AppClient.disconnectClient();
                isWork = false;
            }
        }
    }

    public void stopThread(){
        this.isWork = false;
    }
}
