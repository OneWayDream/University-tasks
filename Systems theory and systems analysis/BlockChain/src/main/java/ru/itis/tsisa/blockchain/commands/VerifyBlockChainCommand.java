package ru.itis.tsisa.blockchain.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.core.encryption.BlockChainTerms;
import ru.itis.tsisa.blockchain.core.structures.BlockChain;
import ru.itis.tsisa.blockchain.core.structures.BlockChainIterator;
import ru.itis.tsisa.blockchain.dto.BlockChainBlockDto;
import ru.itis.tsisa.blockchain.exceptions.*;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class VerifyBlockChainCommand implements DescribedCommand{

    private final BlockChain blockChain;
    private BlockChainIterator blockChainIterator;

    private StringBuilder content;

    @Override
    public void execute() {
        try{
            blockChainIterator = blockChain.getBlockChainIterator();
            verifyBlockChain();
        } catch (BlockChainIteratorInitializationException ex){
            System.out.println("BlockChain is empty => is verified :/");
        } catch (SubscriberException ex){
            System.out.println("Ошибка проверки блокчейна - неверные конфигурации подписей.");
        } catch (AuditorConnectionException ex){
            System.out.println("Невозможно соединиться с удалённым аудитором.");
        }
    }

    private void verifyBlockChain(){
        initContentBuilder();
        while (blockChainIterator.hasBlock()){
            content.append("Block ").append(blockChainIterator.getCurrentBlock().getId()).append(": ");
            boolean isCorrectPreviousHash = Arrays.equals(blockChainIterator.getCurrentBlock().getPreviousHash(),
                    blockChainIterator.getPreviousBlockHash());
            if (!isCorrectPreviousHash){
                content.append("Incorrect previous hash.");
                break;
            } else {
                validateBlock(blockChainIterator.getCurrentBlock());
            }
            try{
                blockChainIterator.next();
            } catch (LostBlockException ex){
                content.append("Блок ")
                        .append(blockChainIterator.getCurrentBlock().getId() + 1)
                        .append(" был утерян! ");
                break;
            }
        }
        displayContent();
    }

    private void validateBlock(BlockChainBlockDto blockDto){
        try{
            BlockChainTerms.validateBlock(blockDto);
            content.append("All right.");
        } catch (BlockVerificationException ex){
            content.append(ex.getMessage());
        }
        content.append("\n");
    }

    private void initContentBuilder(){
        content = new StringBuilder();
    }

    private void displayContent(){
        System.out.println(content);
    }

    @Override
    public Command getCommandType() {
        return Command.VERIFY_BLOCK_CHAIN;
    }

    @Override
    public String getInfo() {
        return getCommandType().getValue() + " - Verify all BlockChain blocks and show results.";
    }

}
