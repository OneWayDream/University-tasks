package ru.itis.tsisa.blockchain.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.core.structures.BlockChain;
import ru.itis.tsisa.blockchain.dto.BlockChainBlockDto;
import ru.itis.tsisa.blockchain.exceptions.EntityNotFoundException;
import ru.itis.tsisa.blockchain.utils.BlockInfoConstructor;


@Component
@RequiredArgsConstructor
public class CheckLastBlockCommand implements DescribedCommand {

    private final BlockChain blockChain;

    @Override
    public void execute() {
        try{
            BlockChainBlockDto blockDto = getLastBlockChainBlock();
            String blockContent = BlockInfoConstructor.constructBlockInfo(blockDto);
            displayBlockInfo(blockContent);
        } catch (EntityNotFoundException ex){
            System.out.println("BlockChain is empty rn :c");
        }
    }

    @Override
    public Command getCommandType() {
        return Command.CHECK_LAST_BLOCK;
    }

    private BlockChainBlockDto getLastBlockChainBlock(){
        return blockChain.getLastBlock();
    }

    private void displayBlockInfo(String blockContent){
        System.out.println(blockContent);
    }

    @Override
    public String getInfo() {
        return getCommandType().getValue() + " - Get information about the last block in the BlockChain system.";
    }

}
