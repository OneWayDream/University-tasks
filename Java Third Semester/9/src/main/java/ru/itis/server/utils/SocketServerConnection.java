package ru.itis.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketServerConnection implements ServerConnection {

    Socket socket;
    int idConnection;
    String username;
    ClientHandleThread clientHandleThread;

    public SocketServerConnection(Socket socket, int idConnection){
        this.socket = socket;
        this.idConnection = idConnection;
        username = null;
        clientHandleThread = null;
    }

    @Override
    public int getConnectionId() {
        return idConnection;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    @Override
    public String getClientNickname() {
        return (username==null) ? "User_" + idConnection : username;
    }

    @Override
    public ClientHandleThread getClientHandleThread() {
        return clientHandleThread;
    }

    @Override
    public void setClientHandleThread(ClientHandleThread clientHandleThread) {
        this.clientHandleThread = clientHandleThread;
    }
}
