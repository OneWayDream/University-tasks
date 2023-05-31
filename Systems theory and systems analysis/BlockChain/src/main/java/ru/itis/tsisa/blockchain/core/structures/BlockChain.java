package ru.itis.tsisa.blockchain.core.structures;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.mappers.BlockChainBlockMapper;
import ru.itis.tsisa.blockchain.core.blocks.SignedBlock;
import ru.itis.tsisa.blockchain.core.encryption.BlockChainTerms;
import ru.itis.tsisa.blockchain.core.encryption.HashUtils;
import ru.itis.tsisa.blockchain.dto.BlockChainBlockDto;
import ru.itis.tsisa.blockchain.models.BlockChainBlock;
import ru.itis.tsisa.blockchain.services.BlockChainBlocksService;

import java.nio.charset.StandardCharsets;

@Component
public class BlockChain {

    private final BlockChainBlocksService service;
    private final byte[] initializingVector;

    public BlockChain(
            BlockChainBlocksService service,
            @Value("${block-chain.initializing-vector}") String initializingVector
    ){
        this.service = service;
        this.initializingVector = new HashUtils().getHash(initializingVectorToBytes(initializingVector), 0L);
    }

    private byte[] initializingVectorToBytes(String initializingVector){
        return initializingVector.getBytes(StandardCharsets.UTF_8);
    }

    public void addNewBlock(SignedBlock block){
        BlockChainBlock blockChainBlock = BlockChainBlockMapper.toBlockChainBlock(block);
        BlockChainTerms.validateBlock(BlockChainBlockMapper.toBlockChainBlockDto(blockChainBlock));
        service.add(blockChainBlock);
    }

    public BlockChainBlockDto getLastBlock(){
        BlockChainBlock block = service.getLast();
        return BlockChainBlockMapper.toBlockChainBlockDto(block);
    }

    public BlockChainBlockDto getFirstBlock(){
        BlockChainBlock block = service.getFirst();
        return BlockChainBlockMapper.toBlockChainBlockDto(block);
    }

    public BlockChainBlockDto getBlockById(Long blockId){
        BlockChainBlock block = service.getById(blockId);
        return BlockChainBlockMapper.toBlockChainBlockDto(block);
    }

    public byte[] getInitializingVector(){
        return initializingVector;
    }

    public BlockChainIterator getBlockChainIterator(){
        return new BlockChainIterator(this);
    }

}
