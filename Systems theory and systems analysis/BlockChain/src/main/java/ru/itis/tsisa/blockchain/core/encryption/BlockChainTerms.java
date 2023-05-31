package ru.itis.tsisa.blockchain.core.encryption;

import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.core.structures.AuditorService;
import ru.itis.tsisa.blockchain.dto.BlockChainBlockDto;
import ru.itis.tsisa.blockchain.exceptions.BlockVerificationException;
import ru.itis.tsisa.blockchain.utils.BytesUtils;

import java.security.PublicKey;
import java.util.Arrays;

@Component
public class BlockChainTerms {

    public static final int NONE_BYTES_SIZE = 8;
    private static KeyManager KEY_MANAGER;
    private static AuditorService AUDITOR_SERVICE;
    private static HashUtils HASH_UTILS;

    public BlockChainTerms(
            KeyManager keyManager,
            AuditorService auditorService,
            HashUtils hashUtils
    ) {
        KEY_MANAGER = keyManager;
        AUDITOR_SERVICE = auditorService;
        HASH_UTILS = hashUtils;
    }

    public static void validateBlock(BlockChainBlockDto block){
        validateMinerContentSign(block);
        validateAuditorContentSign(block);
        validateMinerBlockSign(block);
        validateAuditorBlockSign(block);
        validateHash(block);
    }

    private static void validateMinerContentSign(BlockChainBlockDto block){
        byte[] sign = block.getMinerContentSign();
        PublicKey minerPublicKey = KEY_MANAGER.getPublicKey(block.getMinerId());
        Verifier verifier = new Verifier(minerPublicKey);
        if (!verifier.verifySignature(block.getContentBytes(false), sign)){
            throw new BlockVerificationException("Miner content sign is incorrect.");
        }
    }

    private static void validateAuditorContentSign(BlockChainBlockDto block){
        byte[] sign = block.getAuditorContentSign();
        PublicKey auditorPublicKey = AUDITOR_SERVICE.getPublicKey();
        Verifier verifier = new Verifier(auditorPublicKey);
        byte[] contentBytesHash = HASH_UTILS.getHash(block.getContentBytes(true));
        byte[] signDateTime = block.getContentSignTime().getBytes();
        byte[] contentBytes = BytesUtils.concatenate(signDateTime, contentBytesHash);
        if (!verifier.verifySignature(contentBytes, sign)){
            throw new BlockVerificationException("Auditor content sign is incorrect.");
        }
    }

    private static void validateMinerBlockSign(BlockChainBlockDto block){
        byte[] sign = block.getMinerBlockSign();
        PublicKey minerPublicKey = KEY_MANAGER.getPublicKey(block.getMinerId());
        Verifier verifier = new Verifier(minerPublicKey);
        if (!verifier.verifySignature(block.getBlockBytes(false), sign)){
            throw new BlockVerificationException("Miner block sign is incorrect.");
        }
    }

    private static void validateAuditorBlockSign(BlockChainBlockDto block){
        byte[] sign = block.getAuditorBlockSign();
        PublicKey auditorPublicKey = AUDITOR_SERVICE.getPublicKey();
        Verifier verifier = new Verifier(auditorPublicKey);
        byte[] contentBytesHash = HASH_UTILS.getHash(block.getBlockBytes(true));
        byte[] signDateTime = block.getBlockSignTime().getBytes();
        byte[] contentBytes = BytesUtils.concatenate(signDateTime, contentBytesHash);
        if (!verifier.verifySignature(contentBytes, sign)){
            throw new BlockVerificationException("Auditor block sign is incorrect.");
        }
    }

    private static void validateHash(BlockChainBlockDto block){
        byte[] currentBlockHash = new HashUtils().getHash(block.getBlockBytes(false), block.getNone());
        if (!Arrays.equals(currentBlockHash, block.getHash())){
            throw new BlockVerificationException("Incorrect hash");
        }
    }

}
