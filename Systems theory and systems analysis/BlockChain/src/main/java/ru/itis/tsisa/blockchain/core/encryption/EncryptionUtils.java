package ru.itis.tsisa.blockchain.core.encryption;

import ru.itis.tsisa.blockchain.exceptions.EncryptionUtilsInitializationException;
import ru.itis.tsisa.blockchain.exceptions.InvalidKeyBytesException;

import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class EncryptionUtils {

    private static final String ENCRYPTION_ALGORITHM = "RSA";
    private static final KeyPairGenerator generator;
    private static final KeyFactory keyFactory;

    static {
        try {
            generator = KeyPairGenerator.getInstance(ENCRYPTION_ALGORITHM);
            keyFactory = KeyFactory.getInstance(ENCRYPTION_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionUtilsInitializationException();
        }
        generator.initialize(2048);
    }

    public static KeyPair generateKeyPair(){
        return generator.generateKeyPair();
    }

    public static PublicKey recoverPublicKey(byte[] publicKeyBytes){
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return generatePublicKey(publicKeySpec);
    }

    public static PrivateKey recoverPrivateKey(byte[] privateKeyBytes){
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return generatePrivateKey(privateKeySpec);
    }

    public static byte[] publicKeyToBytes(PublicKey publicKey){
        return publicKey.getEncoded();
    }

    public static byte[] privateKeyToBytes(PrivateKey privateKey){
        return privateKey.getEncoded();
    }

    private static PublicKey generatePublicKey(EncodedKeySpec publicKeySpec){
        try{
            return keyFactory.generatePublic(publicKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new InvalidKeyBytesException(ex);
        }
    }

    private static PrivateKey generatePrivateKey(EncodedKeySpec privateKeySpec){
        try{
            return keyFactory.generatePrivate(privateKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new InvalidKeyBytesException(ex);
        }
    }

}
