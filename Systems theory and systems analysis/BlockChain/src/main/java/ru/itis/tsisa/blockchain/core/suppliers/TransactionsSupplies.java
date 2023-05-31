package ru.itis.tsisa.blockchain.core.suppliers;

import ru.itis.tsisa.blockchain.core.blocks.InformationBlock;

public interface TransactionsSupplies {

    void addExternalBlocks(int amount);
    void addBlock(InformationBlock block);

}
