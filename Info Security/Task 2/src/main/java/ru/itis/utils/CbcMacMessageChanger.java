package ru.itis.utils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.itis.entries.CbcMacMessage;

import java.nio.charset.StandardCharsets;


@RequiredArgsConstructor
public class CbcMacMessageChanger {

    @NonNull
    protected final EncryptionExecutor executor;
    @NonNull
    protected final int blockSize;

    public CbcMacMessage changeMessageContent(CbcMacMessage message, String newContent){
        newContent = newContent + "\n//";
        newContent = new String(
                concatArrays(
                        newContent.getBytes(StandardCharsets.ISO_8859_1),
                        new byte[16]
                ),
                StandardCharsets.ISO_8859_1
        );

        message.setContent(findCorrectContent(newContent, message.getTag(), (byte) -127));
        return message;
    }

    protected String findCorrectContent(String newContent, String tag, byte newByte){
        boolean isFound = false;
        String decryptedString = null;
        int counter = 0;
        while (!isFound){
            try{
                String str = new String(
                        concatArrays(
                                newContent.getBytes(StandardCharsets.ISO_8859_1),
                                new byte[]{newByte}
                        ),
                        StandardCharsets.ISO_8859_1
                );
                byte[] newEncryptedBlocks = executor.encryptString(str).getBytes(StandardCharsets.ISO_8859_1);
                byte[] newBytes = concatArrays(newEncryptedBlocks, tag.getBytes(StandardCharsets.ISO_8859_1));
                decryptedString = executor.decryptString(new String(newBytes, StandardCharsets.ISO_8859_1));
                if (decryptedString.split("\n//")[1].contains("\n")){
                    throw new IllegalArgumentException();
                }
                isFound = true;
            } catch (Exception ex){
                if (newByte == 127){
                    newContent = new String(
                            concatArrays(
                                    newContent.getBytes(StandardCharsets.ISO_8859_1),
                                    new byte[]{0}
                            ),
                            StandardCharsets.ISO_8859_1
                    );
                    newByte = -127;
                } else {
                    newByte++;
                }
            }
            counter++;
        }
        System.out.println("\nOperation's amount to find this message: " + counter);
        return decryptedString;
    }

    protected byte[] concatArrays(byte[] a, byte[] b){
        int aL = a.length;
        int bL = b.length;
        byte[] result = new byte[aL+bL];
        System.arraycopy(a, 0, result, 0, aL);
        System.arraycopy(b, 0, result, aL, bL);
        return result;
    }

}
