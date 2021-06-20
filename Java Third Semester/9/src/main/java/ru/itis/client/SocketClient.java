package ru.itis.client;

import ru.itis.client.exceptions.ClientException;
import ru.itis.client.utils.ServerHandleThread;
import ru.itis.listeners.ClientEventListener;
import ru.itis.protocol.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import ru.itis.protocol.exceptions.MessageException;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class SocketClient implements Client {

    protected final InetAddress address;
    protected final int port;
    protected Socket socket;
    protected List<ClientEventListener> listeners;

    public static SocketClient init(InetAddress inetAddress, int port){
        return SocketClient.builder()
                .address(inetAddress)
                .port(port)
                .listeners(new ArrayList<>())
                .build();
    }

    @Override
    public void connect() throws ClientException {
        try {
            socket = new Socket(address, port);
            Thread thread = new Thread(new ServerHandleThread(this));
            thread.setDaemon(true);
            thread.start();
        } catch (IOException ex){
            throw new ClientException("Cannot connect to the chat server.", ex);
        }
    }

    @Override
    public void sendMessage(Message message) throws ClientException {
        try{
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(Message.getBytes(message));
            outputStream.flush();
        } catch (IOException | MessageException ex){
            throw new ClientException("Cannot send message.");
        }
    }

    @Override
    public void registerListener(ClientEventListener listener) throws ClientException {
        if (socket!=null){
            throw new ClientException("Client has been already connected.");
        }
        try{
            listener.init(this);
            this.listeners.add(listener);
        } catch (NullPointerException ex){
            throw new ClientException("Null listener:? Are you serious:?", ex);
        }
    }

    @Override
    public InputStream getInputStream() throws ClientException{
        try{
            return socket.getInputStream();
        } catch (IOException ex){
            throw new ClientException("Exception in server connection.", ex);
        }
    }

    @Override
    public OutputStream getOutputStream() throws ClientException{
        try{
            return socket.getOutputStream();
        } catch (IOException ex){
            throw new ClientException("Exception in server connection.", ex);
        }
    }
}
