package ru.itis.tsisa.blockchain.core.encryption;

import ru.itis.tsisa.blockchain.exceptions.SignatureCreationException;
import ru.itis.tsisa.blockchain.exceptions.SubscriberException;

import java.security.*;

public class Subscriber {

    private static final String SIGN_ALGORITHM = "SHA256withRSA";

    private final PrivateKey privateKey;

    public Subscriber(PrivateKey privateKey){
        this.privateKey = privateKey;
    }

    public byte[] subscribeMessage(byte[] message){
        Signature signature = initSignature();
        return signMessage(signature, message);
    }

    private Signature initSignature(){
        try{
            Signature signature =  Signature.getInstance(SIGN_ALGORITHM);
            signature.initSign(privateKey);
            return signature;
        } catch (NoSuchAlgorithmException ex) {
            throw new SignatureCreationException(ex);
        } catch (InvalidKeyException ex) {
            throw new ru.itis.tsisa.blockchain.exceptions.InvalidKeyException(ex);
        }
    }

    private byte[] signMessage(Signature signature, byte[] message){
        try{
            signature.update(message);
            return signature.sign();
        } catch (SignatureException ex) {
            throw new SubscriberException(ex);
        }
    }
}
