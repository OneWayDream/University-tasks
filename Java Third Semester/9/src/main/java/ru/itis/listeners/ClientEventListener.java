package ru.itis.listeners;

import ru.itis.client.Client;
import ru.itis.client.exceptions.ClientEventListenerException;
import ru.itis.protocol.Message;

public interface ClientEventListener {
    public void init(Client client);
    public void handle(Client client, Message message) throws ClientEventListenerException;
    public int getType();
}
