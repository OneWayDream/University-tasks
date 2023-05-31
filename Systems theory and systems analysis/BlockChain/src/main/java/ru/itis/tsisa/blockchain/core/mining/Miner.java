package ru.itis.tsisa.blockchain.core.mining;

import ru.itis.tsisa.blockchain.core.blocks.Block;
import ru.itis.tsisa.blockchain.core.blocks.ContentBlock;
import ru.itis.tsisa.blockchain.core.blocks.InformationBlock;
import ru.itis.tsisa.blockchain.core.blocks.SignedBlock;
import ru.itis.tsisa.blockchain.core.encryption.HashUtils;
import ru.itis.tsisa.blockchain.core.encryption.KeyManager;
import ru.itis.tsisa.blockchain.core.encryption.Subscriber;
import ru.itis.tsisa.blockchain.core.structures.AuditorService;

import java.security.PrivateKey;

public class Miner extends Thread {

    private final MinersPool minersPool;
    private final Subscriber subscriber;
    private final MiningUtils miningUtils;
    private final AuditorService auditorService;
    private final int MINER_ID;
    private final HashUtils hashUtils;

    public Miner(
            int id,
            MinersPool minersPool,
            KeyManager keyManager,
            MiningUtils miningUtils,
            AuditorService auditorService
    ){
        MINER_ID = id;
        this.minersPool = minersPool;
        PrivateKey privateKey = keyManager.getPrivateKey(id);
        this.subscriber = new Subscriber(privateKey);
        this.miningUtils = miningUtils.init(id);
        this.auditorService = auditorService;
        hashUtils = new HashUtils();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()){
            InformationBlock currentInformationBlock = minersPool.getCurrentInformationBlock();
            ContentBlock contentBlock = createAndSignContentBlock(currentInformationBlock);
            startMining(contentBlock);
        }
    }

    private ContentBlock createAndSignContentBlock(InformationBlock informationBlock){
        byte[] previousHash = minersPool.getPreviousBlockHash();
        ContentBlock contentBlock = new ContentBlock(informationBlock, previousHash);
        signBlock(contentBlock);
        getAuditorSign(contentBlock);
        return contentBlock;
    }

    private void signBlock(Block block){
        byte[] minerSign = getSignForBlock(block);
        block.setMinerSign(minerSign);
    }

    private byte[] getSignForBlock(Block contentBlock){
        return subscriber.subscribeMessage(contentBlock.getBytes());
    }

    private void getAuditorSign(Block block){
        auditorService.signBlock(block);
    }

    private void startMining(ContentBlock contentBlock){
        long none = miningUtils.getStartValue();
        SignedBlock signedBlock = SignedBlock.builder()
                .contentBlock(contentBlock)
                .minerId(MINER_ID)
                .build();
        byte[] contentBlockBytes = signedBlock.getBytes();
        byte[] currentHash;
        boolean isRequiredHash;
        while ((!minersPool.isHashFound() && none <= miningUtils.getFinishValue())){
            currentHash = hashUtils.getHash(contentBlockBytes, none);
            isRequiredHash = hashUtils.checkHash(currentHash);
            if (isRequiredHash){
                signBlock(signedBlock);
                signedBlock.setHash(currentHash);
                signedBlock.setNone(none);
                getAuditorSign(signedBlock);
                minersPool.offerBlockChainBlock(signedBlock);
                break;
            }
            none++;
        }
        minersPool.awaitAllMiners();
    }

}
