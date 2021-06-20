package ru.itis.server;

import ru.itis.protocol.Message;
import ru.itis.protocol.exceptions.MessageException;
import ru.itis.server.exceptions.ServerException;
import ru.itis.listeners.ServerEventListener;
import ru.itis.server.utils.ClientHandleThread;
import ru.itis.server.utils.ServerConnection;
import ru.itis.server.utils.ServerManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class SocketChatServer implements Server {

    protected List<ServerEventListener> listeners;
    protected int port;
    protected ServerSocket server;
    protected boolean started;
    protected List<ServerConnection> connections;
    protected ServerManager serverManager;

    public static SocketChatServer init(int port, ServerManager serverManager){
        return SocketChatServer.builder()
                .listeners(new ArrayList<>())
                .port(port)
                .connections(new ArrayList<>())
                .started(false)
                .serverManager(serverManager)
                .build();
    }

    @Override
    public void registerListener(ServerEventListener listener) throws ServerException {
        if (started){
            throw new ServerException("Server has been already started.");
        }
        try{
            listener.init(this);
            this.listeners.add(listener);
        } catch (NullPointerException ex){
            throw new ServerException("Null listener:? Are you serious:?", ex);
        }
    }

    @Override
    public void sendMessage(ServerConnection client, Message message) throws ServerException {
        if (!isStarted()){
            throw new ServerException("Server hasn't been started yet.");
        }
        if (connections.contains(client)){
            try{
                OutputStream outputStream = client.getOutputStream();
                outputStream.write(Message.getBytes(message));
                outputStream.flush();
            } catch (IOException ex){
                throw new ServerException("Cannot send data to client.", ex);
            } catch (MessageException ex){
                throw new ServerException("Incorrect message", ex);
            }
        } else {
            throw new ServerException("Unknown connection.");
        }

    }

    @Override
    public void sendRequest(int connectionId) throws ServerException {
        //TODO TODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODOTODO
    }

    @Override
    public void sendBroadCastMessage(Message message) throws ServerException {
        if (!isStarted()){
            throw new ServerException("Server hasn't been started yet.");
        }
        try{
            byte[] messageByte = Message.getBytes(message);
            for (ServerConnection serverConnection : connections){
                OutputStream outputStream = serverConnection.getOutputStream();
                outputStream.write(messageByte);
                outputStream.flush();
            }
        } catch (IOException ex){
            throw new ServerException("Cannot send data to client.", ex);
        } catch (MessageException ex){
            throw new ServerException("Incorrect message", ex);
        }
    }

    @Override
    public void sendBroadCastMessage(Message message, ServerConnection excludedServerConnection) throws ServerException {
        if (!isStarted()){
            throw new ServerException("Server hasn't been started yet.");
        }
        try{
            byte[] messageByte = Message.getBytes(message);
            for (ServerConnection serverConnection : connections){
                if (!serverConnection.equals(excludedServerConnection)){
                    OutputStream outputStream = serverConnection.getOutputStream();
                    outputStream.write(messageByte);
                    outputStream.flush();
                }
            }
        } catch (IOException ex){
            throw new ServerException("Cannot send data to client.", ex);
        } catch (MessageException ex){
            throw new ServerException("Incorrect message", ex);
        }
    }

    @Override
    public void start() throws ServerException {
        try{
            server = new ServerSocket(port);
            started = true;

            while (true){
                ServerConnection serverConnection = serverManager.getConnection(this);
                ClientHandleThread clientHandleThread = new ClientHandleThread(this, serverConnection);
                serverConnection.setClientHandleThread(clientHandleThread);
                Thread thread = new Thread(clientHandleThread);
                thread.setDaemon(true);
                System.out.println("User " + serverConnection.getClientNickname() + " successfully join!");
                String notMessage = "User " + serverConnection.getClientNickname() + " join to the chat.";
                ByteBuffer notBuffer = ByteBuffer.allocate(notMessage.length()*2);
                for (int i = 0; i < notMessage.length(); i++){
                    notBuffer.putChar(notMessage.charAt(i));
                }
                this.sendBroadCastMessage(Message.createMessage(63, notBuffer.array()));
                thread.start();
            }
        } catch (IOException | MessageException ex){
            throw new ServerException("There is problem with server starting.", ex);
        }
    }

}
