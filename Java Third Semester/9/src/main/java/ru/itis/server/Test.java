package ru.itis.server;

import ru.itis.protocol.Message;
import ru.itis.protocol.exceptions.MessageException;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) throws MessageException {
//        System.out.println((byte) (2<<6|4<<3|6));
//        System.out.println((byte) (166));
        System.out.println(Message.toString(Message.createMessage(0, new byte[]{1, 2, 3})));
    }
}
