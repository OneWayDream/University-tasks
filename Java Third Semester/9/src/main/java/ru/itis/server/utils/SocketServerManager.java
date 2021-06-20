package ru.itis.server.utils;

import ru.itis.server.SocketChatServer;

import java.io.IOException;
import java.net.Socket;

public class SocketServerManager implements ServerManager {

    @Override
    public ServerConnection getConnection(SocketChatServer server) throws IOException {
        Socket newSocket = server.getServer().accept();
        synchronized (this){
            int connectionId = server.getConnections().size();
            server.getConnections().add(new SocketServerConnection(newSocket, connectionId));
            return server.getConnections().get(connectionId);
        }
    }
}
