package ru.itis.listeners;

import ru.itis.client.Client;
import ru.itis.protocol.Message;
import ru.itis.server.Server;
import ru.itis.server.exceptions.ServerEventListenerException;
import ru.itis.server.utils.ServerConnection;

public interface ServerEventListener {
    public void init(Server server);
    public void handle(ServerConnection client, Message message) throws ServerEventListenerException;
    public int getType();
}
