package ru.itis.listeners;

import ru.itis.client.Client;

public abstract class AbstractClientEventListener implements ClientEventListener {
    protected boolean init;
    protected Client client;

    @Override
    public void init (Client client){
        this.client = client;
        this.init = true;
    }
}
