package ru.itis.tsisa.blockchain.core.structures;

import ru.itis.tsisa.blockchain.dto.BlockChainBlockDto;
import ru.itis.tsisa.blockchain.exceptions.BlockChainBottomException;
import ru.itis.tsisa.blockchain.exceptions.BlockChainIteratorInitializationException;
import ru.itis.tsisa.blockchain.exceptions.EntityNotFoundException;
import ru.itis.tsisa.blockchain.exceptions.LostBlockException;

public class BlockChainIterator {

    private final BlockChain blockChain;

    private BlockChainBlockDto currentBlock;
    private byte[] previousBlockHash;
    private Long lastBlockId;


    public BlockChainIterator(BlockChain blockChain){
        this.blockChain = blockChain;
        init();
    }

    private void init(){
        try{
            currentBlock = blockChain.getFirstBlock();
            previousBlockHash = blockChain.getInitializingVector();
            lastBlockId = blockChain.getLastBlock().getId();
        } catch (EntityNotFoundException ex){
            throw new BlockChainIteratorInitializationException(ex);
        }
    }

    public BlockChainBlockDto getCurrentBlock(){
        return currentBlock;
    }

    public byte[] getPreviousBlockHash(){
        return previousBlockHash;
    }

    public boolean hasBlock(){
        return currentBlock != null;
    }

    public void next(){
        if (!hasBlock()){
            throw new BlockChainBottomException();
        }
        previousBlockHash = currentBlock.getHash();
        currentBlock = getNextBlock();
    }

    private BlockChainBlockDto getNextBlock(){
        BlockChainBlockDto result;
        try{
            result = blockChain.getBlockById(currentBlock.getId() + 1);
        } catch (EntityNotFoundException ex){
            if (currentBlock.getId() + 1 <= lastBlockId){
                throw new LostBlockException();
            }
            result = null;
        }
        return result;
    }

}
