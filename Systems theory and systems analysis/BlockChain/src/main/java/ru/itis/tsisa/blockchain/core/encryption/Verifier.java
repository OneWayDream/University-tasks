package ru.itis.tsisa.blockchain.core.encryption;

import ru.itis.tsisa.blockchain.exceptions.SignatureCreationException;
import ru.itis.tsisa.blockchain.exceptions.SubscriberException;

import java.security.*;

public class Verifier {

    private static final String SIGN_ALGORITHM = "SHA256withRSA";

    private final PublicKey publicKey;

    public Verifier(PublicKey publicKey){
        this.publicKey = publicKey;
    }

    public boolean verifySignature(byte[] message, byte[] sign){
        Signature signature = initSignature();
        return signMessage(signature, message, sign);
    }

    private Signature initSignature(){
        try{
            Signature signature =  Signature.getInstance(SIGN_ALGORITHM);
            signature.initVerify(publicKey);
            return signature;
        } catch (NoSuchAlgorithmException ex) {
            throw new SignatureCreationException(ex);
        } catch (InvalidKeyException ex) {
            throw new ru.itis.tsisa.blockchain.exceptions.InvalidKeyException(ex);
        }
    }

    private boolean signMessage(Signature signature, byte[] message, byte[] sign){
        try{
            signature.update(message);
            return signature.verify(sign);
        } catch (SignatureException ex) {
            throw new SubscriberException(ex);
        }
    }

}
