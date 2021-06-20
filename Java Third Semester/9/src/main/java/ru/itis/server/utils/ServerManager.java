package ru.itis.server.utils;

import ru.itis.server.SocketChatServer;

import java.io.IOException;

public interface ServerManager {
    ServerConnection getConnection(SocketChatServer server) throws IOException;
}
