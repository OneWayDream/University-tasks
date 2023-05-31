package ru.itis.tsisa.blockchain.commands;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.BlockChainConsoleApplication;
import ru.itis.tsisa.blockchain.core.structures.BlockChain;
import ru.itis.tsisa.blockchain.dto.BlockChainBlockDto;
import ru.itis.tsisa.blockchain.exceptions.EntityNotFoundException;
import ru.itis.tsisa.blockchain.utils.BlockInfoConstructor;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class CheckBlockCommand implements DescribedCommand {

    private final BlockChain blockChain;
    private final Scanner scanner = BlockChainConsoleApplication.getCommandLineScanner();

    @Override
    public void execute() {
        try{
            BlockChainBlockDto blockDto = getBlockChainBlockById();
            String blockContent = BlockInfoConstructor.constructBlockInfo(blockDto);
            displayBlockInfo(blockContent);
        } catch (EntityNotFoundException ex){
            System.out.println("There is no block with this id.");
        } catch (IllegalArgumentException ex){
            System.out.println("Id is a numeric (long) value.");
        }
    }

    @Override
    public Command getCommandType() {
        return Command.CHECK_BLOCK;
    }

    private BlockChainBlockDto getBlockChainBlockById(){
        Long blockId = askForBlockId();
        return blockChain.getBlockById(blockId);
    }

    private Long askForBlockId(){
        System.out.println("Enter the block id: ");
        try{
            return scanner.nextLong();
        } catch (InputMismatchException ex){
            throw new IllegalArgumentException();
        } finally {
            scanner.nextLine();
        }
    }

    private void displayBlockInfo(String blockContent){
        System.out.println(blockContent);
    }

    @Override
    public String getInfo() {
        return getCommandType().getValue() + " - Get information about the block by id.";
    }

}
