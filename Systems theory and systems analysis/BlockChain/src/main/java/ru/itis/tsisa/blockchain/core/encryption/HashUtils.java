package ru.itis.tsisa.blockchain.core.encryption;

import lombok.NoArgsConstructor;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.exceptions.MiningUtilsInitializationException;
import ru.itis.tsisa.blockchain.utils.BytesUtils;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Component
@NoArgsConstructor
public class HashUtils {

    private static final String MESSAGE_DIGEST_ALGORITHM = "SHA-256";
    private static int ZEROES_AMOUNT;

    private MessageDigest messageDigest;

    @Autowired
    public HashUtils(
            @Value("${block-chain.hash-zeroes-amount}") int zeroesAmount
    ) {
        ZEROES_AMOUNT = zeroesAmount;
    }

    {
        initMessageDigest();
    }

    private void initMessageDigest(){
        try{
            messageDigest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            throw new MiningUtilsInitializationException(ex);
        }
    }

    public boolean checkHash(byte[] bytes){
        boolean result = true;
        for (int i = 0; i < ZEROES_AMOUNT; i++){
            if (bytes[i] != 0){
                result = false;
                break;
            }
        }
        return result;
    }

    public byte[] getHash(byte[] bytesToHash){
        return getHash(bytesToHash, null);
    }

    public byte[] getHash(byte[] contentBytes, Long none){
        byte[] bytesToHash;
        if (none != null){
            byte[] noneToBytes = ByteBuffer.allocate(BlockChainTerms.NONE_BYTES_SIZE).putLong(none).array();
            bytesToHash = BytesUtils.concatenate(contentBytes, noneToBytes);
        } else {
            bytesToHash = contentBytes;
        }
        try{
            return messageDigest.digest(bytesToHash);
        } catch (ArrayIndexOutOfBoundsException ex){
            ex.printStackTrace();
            System.out.println(Arrays.toString(bytesToHash));
        }
        return contentBytes;
    }

    public String bytesToHex(byte[] bytes) {
        return new String(Hex.encode(bytes));
    }

    public byte[] hexToBytes(String hex){
        return Hex.decode(hex.getBytes());
    }
}
