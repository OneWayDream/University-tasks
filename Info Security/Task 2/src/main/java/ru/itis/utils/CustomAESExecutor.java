package ru.itis.utils;

import lombok.Getter;
import lombok.Setter;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

@Getter
@Setter
public class CustomAESExecutor implements EncryptionExecutor {

    protected static final String KEY_GENERATION_ALGORITHM = "AES";
    protected static final String KEY_GENERATION_FROM_PASSWORD_ALGORITHM = "PBKDF2WithHmacSHA256";

    protected SecretKey secretKey;
    protected String algorithm;
    protected IvParameterSpec ivParameterSpec;
    protected byte[] secret;
    public CustomAESExecutor(int size, String password, String salt, String algorithm, byte[] secret){
        if ((password != null) && (salt != null)){
            secretKey = generateKeyFromPassword(size, password, salt);
        } else {
            secretKey = generateKey(size);
        }
        this.algorithm = algorithm;
        this.ivParameterSpec = generateIv();
        this.secret = secret;
    }

    public void encryptBmp(String inputPath, String outputPath) {
        applyAlgorithm(Cipher.ENCRYPT_MODE, inputPath, outputPath);
    }

    public void decryptBmp(String inputPath, String outputPath){
        applyAlgorithm(Cipher.DECRYPT_MODE, inputPath, outputPath);
    }

    public String encryptString(String input){
        try{
            byte[] bytes = input.getBytes(StandardCharsets.ISO_8859_1);
            return new String(applyAlgorithm(Cipher.ENCRYPT_MODE, bytes), StandardCharsets.ISO_8859_1);
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    public String decryptString(String input){
        try{
            byte[] bytes = input.getBytes(StandardCharsets.ISO_8859_1);
            return new String(applyAlgorithm(Cipher.DECRYPT_MODE, bytes), StandardCharsets.ISO_8859_1);
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    public boolean checkCbcMac(String message, String tag){
        byte[] encryptedBytes = encryptString(message).getBytes(StandardCharsets.ISO_8859_1);
        String currentTag = new String(
                Arrays.copyOfRange(
                        encryptedBytes,
                        encryptedBytes.length - 16,
                        encryptedBytes.length
                ),
                StandardCharsets.ISO_8859_1
        );
        return tag.equals(currentTag);
    }

    public void generateSecretKeyByString(String keyString){
        secretKey = new SecretKeySpec(keyString.getBytes(StandardCharsets.ISO_8859_1), 0, keyString.length(), KEY_GENERATION_ALGORITHM);
    }

    protected byte[] applyAlgorithm(int mode, byte[] content){
        try{
            Cipher cipher = createCipher(mode);
            return cipher.doFinal(concatWithSecret(content));
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected byte[] concatWithSecret(byte[] arr){
        int a = arr.length;
        int b = secret.length;
        byte[] result = new byte[a+b];
        System.arraycopy(arr, 0, result, 0, a);
        System.arraycopy(secret, 0, result, a, b);
        return result;
    }

    protected void applyAlgorithm(int mode, String inputPath, String outputPath){
        try{
            Cipher cipher = createCipher(mode);
            FileInputStream inputStream = new FileInputStream(inputPath);
            FileOutputStream outputStream = new FileOutputStream(outputPath);
            int bytesRead;
            byte[] headerBuffer = new byte[54];
            bytesRead = inputStream.read(headerBuffer);
            if (bytesRead != -1){
                outputStream.write(headerBuffer);
                byte[] readBuffer = new byte[128];
                while ((bytesRead = inputStream.read(readBuffer)) != -1) {
                    byte[] output = cipher.update(readBuffer, 0, bytesRead);
                    if (output != null) {
                        outputStream.write(output);
                    }
                }
                byte[] outputBytes = cipher.doFinal();
                if (outputBytes != null) {
                    outputStream.write(outputBytes);
                }
                inputStream.close();
                outputStream.close();
            } else {
                inputStream.close();
                outputStream.close();
                throw new IllegalArgumentException("Can't read the header.");
            }
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected Cipher createCipher(int mode){
        try{
            Cipher cipher = Cipher.getInstance(algorithm);
            if (algorithm.equals("AES/ECB/PKCS5Padding")){
                cipher.init(mode, secretKey);
            } else {
                cipher.init(mode, secretKey, ivParameterSpec);
            }
            return cipher;
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected SecretKey generateKey(int size){
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_GENERATION_ALGORITHM);
            keyGenerator.init(size);
            return keyGenerator.generateKey();
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected SecretKey generateKeyFromPassword(int size, String password, String salt){
        try{
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_GENERATION_FROM_PASSWORD_ALGORITHM);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, size);
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), KEY_GENERATION_ALGORITHM);
        } catch (Exception ex){
            throw new IllegalArgumentException(ex);
        }
    }

    protected IvParameterSpec generateIv(){
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}
