package ru.itis.tsisa.blockchain.services;

import ru.itis.tsisa.blockchain.models.BlockChainBlock;

public interface BlockChainBlocksService {

    void add(BlockChainBlock block);
    BlockChainBlock getLast();
    BlockChainBlock getById(Long blockId);
    BlockChainBlock getFirst();

}
