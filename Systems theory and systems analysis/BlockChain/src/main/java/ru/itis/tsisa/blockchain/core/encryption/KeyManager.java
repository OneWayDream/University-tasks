package ru.itis.tsisa.blockchain.core.encryption;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.exceptions.EntityNotFoundException;
import ru.itis.tsisa.blockchain.models.UserKeys;
import ru.itis.tsisa.blockchain.services.MinersKeysService;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@Component
@RequiredArgsConstructor
public class KeyManager {

    private final MinersKeysService minersKeysService;

    public KeyPair getKeys(Integer userId){
        KeyPair keyPair;
        try{
            keyPair = getKeyPairFromDatabase(userId);
        } catch (EntityNotFoundException ex){
            keyPair = createMinerKeys(userId);
        }
        return keyPair;
    }

    public PublicKey getPublicKey(Integer userId){
        PublicKey publicKey;
        try{
            publicKey = getPublicKeyFromDatabase(userId);
        } catch (EntityNotFoundException ex){
            publicKey = createMinerKeys(userId).getPublic();
        }
        return publicKey;
    }

    public PrivateKey getPrivateKey(Integer userId){
        PrivateKey privateKey;
        try{
            privateKey = getPrivateKeyFromDatabase(userId);
        } catch (EntityNotFoundException ex){
            privateKey = createMinerKeys(userId).getPrivate();
        }
        return privateKey;
    }

    private KeyPair getKeyPairFromDatabase(Integer userId){
        UserKeys userKeys = minersKeysService.getByUserId(userId);
        return recoverKeyPair(userKeys);
    }

    private KeyPair recoverKeyPair(UserKeys keys){
        return new KeyPair(recoverPublicKey(keys.getPublicKey()), recoverPrivateKey(keys.getPrivateKey()));
    }

    private PublicKey recoverPublicKey(byte[] publicKeyBytes){
        return EncryptionUtils.recoverPublicKey(publicKeyBytes);
    }

    private PublicKey getPublicKeyFromDatabase(Integer userId){
        UserKeys userKeys = minersKeysService.getByUserId(userId);
        return recoverPublicKey(userKeys.getPublicKey());
    }

    private PrivateKey getPrivateKeyFromDatabase(Integer userId){
        UserKeys userKeys = minersKeysService.getByUserId(userId);
        return recoverPrivateKey(userKeys.getPrivateKey());
    }

    private PrivateKey recoverPrivateKey(byte[] privateKeyBytes){
        return EncryptionUtils.recoverPrivateKey(privateKeyBytes);
    }

    private KeyPair createMinerKeys(Integer userId){
        KeyPair keyPair = generateKeyPair();
        saveKeyPair(userId, keyPair);
        return keyPair;
    }

    private KeyPair generateKeyPair(){
        return EncryptionUtils.generateKeyPair();
    }

    private void saveKeyPair(Integer userId, KeyPair keyPair){
        minersKeysService.add(UserKeys.builder()
                .userId(userId)
                .publicKey(EncryptionUtils.publicKeyToBytes(keyPair.getPublic()))
                .privateKey(EncryptionUtils.privateKeyToBytes(keyPair.getPrivate()))
                .build());
    }

}
