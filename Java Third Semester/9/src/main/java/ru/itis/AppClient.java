package ru.itis;

import ru.itis.client.Client;
import ru.itis.client.SocketClient;
import ru.itis.client.exceptions.ClientException;
import ru.itis.listeners.client_impl.BroadcastFramesListenerClient;
import ru.itis.listeners.client_impl.ServiceFramesListenerClient;
import ru.itis.protocol.Message;
import ru.itis.protocol.exceptions.MessageException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Scanner;

public class AppClient {
    protected static final String HOST = "127.0.0.1";
    protected static final int PORT =  2554;
    protected static volatile boolean isWork;

    public static void main(String[] args) {
        try {
            Client client = SocketClient.init(InetAddress.getByName(HOST), PORT);
            client.registerListener(new BroadcastFramesListenerClient());
            client.registerListener(new ServiceFramesListenerClient());
            client.connect();
            isWork = true;
            Scanner sc = new Scanner(System.in);
            System.out.println("Welcome to the simple chat. Write some text to send in to other people here.");
            System.out.println("Write \"/help\", if you want to get all commands here.");
            String userText;
            while (isWork){
                userText = sc.nextLine().trim();
                switch (userText){
                    case "/exit":
                        ByteBuffer exitBuffer = ByteBuffer.allocate(userText.length()*2);
                        for (int i = 0; i < userText.length(); i++){
                            exitBuffer.putChar(userText.charAt(i));
                        }
                        client.sendMessage(Message.createMessage(0, exitBuffer.array()));
                        Thread.sleep(3000);
                        break;
                    case "/help":
                        //This information may be obtained using service frame, but I'm lazy c:
                        System.out.println("Write anything to send it to other people in the chat.");
                        System.out.println("Write \"exit\' to leave this chat.");
                        break;
                    case "":
                        //ignore
                        break;
                    default:
                        ByteBuffer messageBuffer = ByteBuffer.allocate(userText.length()*2);
                        for (int i = 0; i < userText.length(); i++){
                            messageBuffer.putChar(userText.charAt(i));
                        }
                        client.sendMessage(Message.createMessage(10, messageBuffer.array()));
                        break;
                }
            }
            System.out.println("You successfully leaved chat!");
        } catch (UnknownHostException ex){
            System.out.println("Incorrect host");
        } catch (ClientException ex){
            System.out.println(ex.getMessage());
        } catch (MessageException e) {
            System.out.println("Incorrect message.");
        } catch (InterruptedException e) {
            System.out.println("Emergency exit.");
        }
    }
    public static void disconnectClient(){
        isWork = false;
    }
}
