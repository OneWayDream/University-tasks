package ru.itis.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ServerConnection {
    int getConnectionId();
    InputStream getInputStream() throws IOException;
    OutputStream getOutputStream() throws IOException;
    String getClientNickname();
    ClientHandleThread getClientHandleThread();
    void setClientHandleThread(ClientHandleThread clientHandleThread);
}
