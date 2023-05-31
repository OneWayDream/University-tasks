package ru.itis.tsisa.blockchain.core.suppliers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.core.blocks.InformationBlock;
import ru.itis.tsisa.blockchain.core.structures.IncomingInformationBlocksQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class RandomTransactionsSupplies implements TransactionsSupplies {

    private final IncomingInformationBlocksQueue queue;
    private final int RANDOM_TRANSACTIONS_AMOUNT;

    public RandomTransactionsSupplies(
            IncomingInformationBlocksQueue queue,
            @Value("${block-chain.random-transactions-amount}") Integer randomTransactionsAmount

    ) {
        this.queue = queue;
        RANDOM_TRANSACTIONS_AMOUNT = randomTransactionsAmount;
    }

    @Override
    public void addExternalBlocks(int amount) {
        List<InformationBlock> informationBlocks = generateRandomTransactionsBlocks(amount);
        addInformationBlocksToBlockChainQueue(informationBlocks);
    }

    @Override
    public void addBlock(InformationBlock block) {
        queue.add(block);
    }

    private List<InformationBlock> generateRandomTransactionsBlocks(int amount){
        List<InformationBlock> blocks = new ArrayList<>();
        for (int i = 0; i < amount; i++){
            blocks.add(generateRandomTransactionsBlock());
        }
        return blocks;
    }

    private InformationBlock generateRandomTransactionsBlock(){
        String informationBlockContent = generateInformationBlockContent();
        return new InformationBlock(informationBlockContent);
    }

    private String generateInformationBlockContent(){
        return IntStream.range(0, RANDOM_TRANSACTIONS_AMOUNT)
                .mapToObj(i -> generateRandomTransaction())
                .collect(Collectors.joining("\n"));
    }

    private String generateRandomTransaction(){
        return UUID.randomUUID() + " ===( " + (Math.random()*10000) + "$ )==> " + UUID.randomUUID();
    }

    private void addInformationBlocksToBlockChainQueue(List<InformationBlock> informationBlocks){
        queue.addAll(informationBlocks);
    }

}
