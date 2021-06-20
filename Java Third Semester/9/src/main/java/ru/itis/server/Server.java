package ru.itis.server;

import ru.itis.protocol.Message;
import ru.itis.server.exceptions.ServerException;
import ru.itis.listeners.ServerEventListener;
import ru.itis.server.utils.ServerConnection;

import java.util.List;

public interface Server {
    public void registerListener (ServerEventListener listener) throws ServerException;
    public void sendMessage(ServerConnection client, Message message) throws ServerException;
    public void sendRequest(int connectionId) throws ServerException;
    public void sendBroadCastMessage(Message message) throws ServerException;
    public void sendBroadCastMessage(Message message, ServerConnection excludedServerConnection) throws ServerException;
    public void start() throws ServerException;
    public List<ServerEventListener> getListeners();
    public List<ServerConnection> getConnections();
}
