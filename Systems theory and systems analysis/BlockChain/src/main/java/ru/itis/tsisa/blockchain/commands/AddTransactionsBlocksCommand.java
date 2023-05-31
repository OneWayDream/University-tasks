package ru.itis.tsisa.blockchain.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.BlockChainConsoleApplication;
import ru.itis.tsisa.blockchain.core.suppliers.RandomTransactionsSupplies;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class AddTransactionsBlocksCommand implements DescribedCommand {

    private final RandomTransactionsSupplies transactionsSupplies;
    private final Scanner scanner = BlockChainConsoleApplication.getCommandLineScanner();

    @Override
    public void execute() {
        int blocksAmount = askForBlocksAmount();
        transactionsSupplies.addExternalBlocks(blocksAmount);
        informAboutSuccessAdding();
    }

    @Override
    public Command getCommandType() {
        return Command.ADD_TRANSACTIONS_BLOCKS;
    }

    @Override
    public String getInfo() {
        return getCommandType().getValue() + " - Add some random blocks transactions to the blockchain queue.";
    }

    private int askForBlocksAmount(){
        System.out.println("Enter the amount of blocks to be added: ");
        int blocksAmount = scanner.nextInt();
        scanner.nextLine();
        return blocksAmount;
    }

    private void informAboutSuccessAdding(){
        System.out.println("(^_~) The blocks have been successfully added to the queue.");
    }

}
