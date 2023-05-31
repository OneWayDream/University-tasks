package ru.itis.tsisa.blockchain.core.structures;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.core.blocks.Block;
import ru.itis.tsisa.blockchain.core.encryption.KeyManager;
import ru.itis.tsisa.blockchain.core.encryption.Subscriber;

import java.security.PrivateKey;
import java.security.PublicKey;

@Component
@Profile("local")
public class LocalAuditorService implements AuditorService{

    private final Subscriber subscriber;
    private final PublicKey publicKey;

    public LocalAuditorService(
            KeyManager keyManager,
            @Value("${block-chain.auditor-id}") int auditorId
    ){
        PrivateKey privateKey = getPrivateKeyForAuditor(keyManager, auditorId);
        publicKey = getPublicKeyForAuditor(keyManager, auditorId);
        subscriber = new Subscriber(privateKey);
    }

    private PrivateKey getPrivateKeyForAuditor(KeyManager keyManager, int auditorId){
        return keyManager.getPrivateKey(auditorId);
    }

    private PublicKey getPublicKeyForAuditor(KeyManager keyManager, int auditorId){
        return keyManager.getPublicKey(auditorId);
    }

    public void signBlock(Block block){
        byte[] auditorSign = getSignForContentBlock(block);
        block.setAuditorSign(auditorSign);
    }

    private byte[] getSignForContentBlock(Block block){
        return subscriber.subscribeMessage(block.getBytes());
    }

    public PublicKey getPublicKey(){
        return publicKey;
    }

}
