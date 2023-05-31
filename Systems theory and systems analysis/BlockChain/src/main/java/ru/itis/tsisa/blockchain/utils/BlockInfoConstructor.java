package ru.itis.tsisa.blockchain.utils;

import ru.itis.tsisa.blockchain.core.encryption.BlockChainTerms;
import ru.itis.tsisa.blockchain.dto.BlockChainBlockDto;
import ru.itis.tsisa.blockchain.exceptions.BlockVerificationException;

import java.util.Arrays;

public class BlockInfoConstructor {

    private static final String CONTENT_DIVIDER = "───────── ⋆⋅☆⋅⋆ ────────────────── ⋆⋅☆⋅⋆ ────────────────── ⋆⋅☆⋅⋆ ─────────";
    private static final String SECTION_DIVIDER = "——————————— ୨୧ ———————————————————— ୨୧ ———————————————————— ୨୧ ———————————";

    private static StringBuilder content;

    public static String constructBlockInfo(BlockChainBlockDto blockDto){
        resetContentBuilder();
        appendContentPart(blockDto);
        appendSectionDivider();
        appendHashPart(blockDto);
        appendSectionDivider();
        appendSignsPart(blockDto);
        appendSectionDivider();
        appendVerificationPart(blockDto);
        return content.toString();
    }

    private static void resetContentBuilder(){
        content = new StringBuilder();
    }

    private static void appendContentPart(BlockChainBlockDto blockDto){
        appendBlockId(blockDto);
        appendBlockAddTime(blockDto);
        appendBlockContent(blockDto);
    }

    private static void appendBlockId(BlockChainBlockDto blockDto){
        content.append("BlockId: ").append(blockDto.getId()).append("\n");
    }

    private static void appendBlockAddTime(BlockChainBlockDto blockDto){
        content.append("Add time: ").append(blockDto.getAddTime().toString()).append("\n");
    }

    private static void appendBlockContent(BlockChainBlockDto blockDto){
        content.append("Block content: \n")
                .append(CONTENT_DIVIDER).append("\n")
                .append(blockDto.getContent())
                .append("\n").append(CONTENT_DIVIDER).append("\n");
    }

    private static void appendSectionDivider(){
        content.append("\n").append(SECTION_DIVIDER).append("\n");
    }

    private static void appendHashPart(BlockChainBlockDto blockDto){
        appendPreviousBlockHash(blockDto);
        appendBlockHash(blockDto);
        appendBlockNone(blockDto);
        appendBlockMinerId(blockDto);
    }

    private static void appendPreviousBlockHash(BlockChainBlockDto blockDto){
        content.append("Previous block hash: ").append(Arrays.toString(blockDto.getPreviousHash())).append("\n");
    }

    private static void appendBlockHash(BlockChainBlockDto blockDto){
        content.append("Block hash: ").append(Arrays.toString(blockDto.getHash())).append("\n");
    }

    private static void appendBlockNone(BlockChainBlockDto blockDto){
        content.append("Block none: ").append(blockDto.getNone()).append("\n");
    }

    private static void appendBlockMinerId(BlockChainBlockDto blockDto){
        content.append("Miner id: ").append(blockDto.getMinerId()).append("\n");
    }

    private static void appendSignsPart(BlockChainBlockDto blockDto){
        appendMinerContentSign(blockDto);
        appendAuditorContentSign(blockDto);
        appendMinerBlockSign(blockDto);
        appendAuditorBlockSign(blockDto);
    }

    private static void appendMinerContentSign(BlockChainBlockDto blockDto){
        content.append("Miner content sign: ").append(Arrays.toString(blockDto.getMinerContentSign())).append("\n");
    }

    private static void appendAuditorContentSign(BlockChainBlockDto blockDto){
        content.append("Auditor content sign: ").append(Arrays.toString(blockDto.getAuditorContentSign())).append("\n");
    }

    private static void appendMinerBlockSign(BlockChainBlockDto blockDto){
        content.append("Miner block sign: ").append(Arrays.toString(blockDto.getMinerBlockSign())).append("\n");
    }

    private static void appendAuditorBlockSign(BlockChainBlockDto blockDto){
        content.append("Auditor block sign: ").append(Arrays.toString(blockDto.getAuditorBlockSign())).append("\n");
    }

    private static void appendVerificationPart(BlockChainBlockDto blockDto){
        content.append("Block status: ");
        try{
            BlockChainTerms.validateBlock(blockDto);
            content.append("Verified");
        } catch (BlockVerificationException ex){
            content.append(ex.getMessage());
        }
        content.append("\n");
    }
    
}
