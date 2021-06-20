package ru.itis.server.utils;

import ru.itis.protocol.Message;
import ru.itis.protocol.exceptions.FrameContentException;
import ru.itis.protocol.exceptions.MessageException;
import ru.itis.server.Server;
import ru.itis.server.exceptions.ClientConnectionException;
import ru.itis.server.exceptions.ClientThreadException;
import ru.itis.server.exceptions.ServerEventListenerException;
import ru.itis.listeners.ServerEventListener;

import java.io.IOException;


public class ClientHandleThread implements Runnable {

    protected Server server;
    protected ServerConnection client;
    protected boolean isWork;

    public ClientHandleThread(Server server, ServerConnection client){
        this.server = server;
        this.client = client;
    }

    @Override
    public void run() {
        isWork = true;
        while (isWork){
            try{
                Message message = Message.readMessage(client.getInputStream());
                System.out.println("---------------------------------");
                System.out.println(client.getClientNickname() + " send: ");
                System.out.println(Message.toString(message));
                System.out.println("---------------------------------");
                for (ServerEventListener listener : server.getListeners()){
                    if (listener.getType() == message.getType()){
                        listener.handle(client, message);
                    }
                }
            } catch (FrameContentException ex) {
                //TODO send request to repeat frame;
            } catch (MessageException ex) {
                throw new ClientThreadException("Exception during getting client " + client.getConnectionId() + " frame.", ex);
            } catch (IOException ex) {
                throw new ClientThreadException("Cannot get connection to user " + client.getConnectionId() + ".");
            } catch (ServerEventListenerException ex) {
                throw new ClientThreadException("Exception during handling user " + client.getConnectionId() + " frame.", ex);
            } catch (ClientConnectionException e) {
                System.out.println("Client " + client.getClientNickname() + " was disconnected.");
                this.stopThread();
                server.getConnections().remove(client);
            }
        }
    }

    public void stopThread(){
        this.isWork = false;
    }
}
