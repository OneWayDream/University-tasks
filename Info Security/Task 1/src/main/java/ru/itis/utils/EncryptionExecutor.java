package ru.itis.utils;

public interface EncryptionExecutor {

    void encryptBmp(String inputPath, String outputPath);
    void decryptBmp(String inputPath, String outputPath);
    String encryptString (String input);

}
