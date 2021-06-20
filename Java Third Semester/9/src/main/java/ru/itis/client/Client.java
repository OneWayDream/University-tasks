package ru.itis.client;

import ru.itis.client.exceptions.ClientException;
import ru.itis.listeners.ClientEventListener;
import ru.itis.protocol.Message;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface Client {
    public void connect() throws ClientException;
    public void sendMessage(Message message) throws ClientException;
    public void registerListener (ClientEventListener listener) throws ClientException;
    public List<ClientEventListener> getListeners();
    public InputStream getInputStream() throws ClientException;
    public OutputStream getOutputStream() throws ClientException;
}
