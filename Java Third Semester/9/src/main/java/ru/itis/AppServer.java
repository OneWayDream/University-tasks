package ru.itis;

import ru.itis.server.Server;
import ru.itis.server.SocketChatServer;
import ru.itis.server.exceptions.ServerException;
import ru.itis.listeners.server_impl.ServiceFramesListenerServer;
import ru.itis.listeners.server_impl.UserCommandFramesListenerServer;
import ru.itis.listeners.server_impl.UserMessageFramesListenerServer;
import ru.itis.server.utils.SocketServerManager;

public class AppServer {
    protected static final int PORT = 2554;

    public static void main(String[] args) {
        try{
            Server server = SocketChatServer.init(2554, new SocketServerManager());
            server.registerListener(new ServiceFramesListenerServer());
            server.registerListener(new UserCommandFramesListenerServer());
            server.registerListener(new UserMessageFramesListenerServer());
            System.out.println("Server successfully started.");
            server.start();
        } catch (ServerException ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }
}
