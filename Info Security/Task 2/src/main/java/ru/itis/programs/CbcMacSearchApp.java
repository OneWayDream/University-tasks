package ru.itis.programs;

import ru.itis.entries.CbcMacMessage;
import ru.itis.utils.CbcMacMessageChanger;
import ru.itis.utils.CustomAESExecutor;

import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public class CbcMacSearchApp {

    protected static final int KEY_LENGTH = 256;
    protected static final String PASSWORD = null;
    protected static final String SALT = null;
    protected static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    protected static final byte[] SECRET = new byte[0];
    protected static final String KEY_STRING = "YELLOW SUBMARINE";
    protected static final IvParameterSpec IV_PARAMETER_SPEC = new IvParameterSpec(new byte[16]);
    protected static final String ORIGINAL_MESSAGE="alert(\"Hello world!\");";
    protected static final String CORRUPTED_MESSAGE="alert(\"You are pwned!\");";


    // Вероятность того, что последний блок не будет содержать двоек и будет заканчиваться на единицу - 0.00368
    public static void main(String[] args) {

        //create aes-executor and set custom key and iv.
        CustomAESExecutor executor = new CustomAESExecutor(KEY_LENGTH, PASSWORD, SALT, ALGORITHM, SECRET);
        executor.generateSecretKeyByString(KEY_STRING);
        executor.setIvParameterSpec(IV_PARAMETER_SPEC);

        //create searcher
        CbcMacMessageChanger changer = new CbcMacMessageChanger(executor, 16);

        String encryptedMessage = executor.encryptString(ORIGINAL_MESSAGE);
        byte[] encryptedBytes = encryptedMessage.getBytes(StandardCharsets.ISO_8859_1);
        String tag = new String(
                Arrays.copyOfRange(
                        encryptedBytes,
                        encryptedBytes.length - 16,
                         encryptedBytes.length
                ),
                StandardCharsets.ISO_8859_1
        );

        CbcMacMessage message = CbcMacMessage.builder()
                .content(ORIGINAL_MESSAGE)
                .tag(tag)
                .build();

        System.out.println("Result of check: " + executor.checkCbcMac(message.getContent(), message.getTag())
                + "\n-----\nmessage: " + message.getContent() + "\n-----\ntag: " + tag);

        message = changer.changeMessageContent(message, CORRUPTED_MESSAGE);

        System.out.println("\n^u^ ^u^ ^u^ ^u^ ^u^ ^u^ ^u^ ^u^ ^u^ ^u^ ^u^ ^u^ ^u^ ^u^ ^u^ ^u^ ^u^ ^u^\n");

        System.out.println("Result of check: " + executor.checkCbcMac(message.getContent(), message.getTag())
                + "\n-----\nmessage: " + message.getContent() + "\n-----\ntag: " + tag);

    }
}
