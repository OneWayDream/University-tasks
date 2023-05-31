package ru.itis.tsisa.blockchain.mappers;

import ru.itis.tsisa.blockchain.core.blocks.SignedBlock;
import ru.itis.tsisa.blockchain.dto.BlockChainBlockDto;
import ru.itis.tsisa.blockchain.models.BlockChainBlock;

import java.time.LocalDateTime;

public class BlockChainBlockMapper {

    public static BlockChainBlock toBlockChainBlock(SignedBlock signedBlock){
        return BlockChainBlock.builder()
                .content(signedBlock.getContentBlock().getInformationBlock().getContent())
                .minerId(signedBlock.getMinerId())
                .hash(signedBlock.getHash())
                .previousHash(signedBlock.getContentBlock().getPreviousHash())
                .none(signedBlock.getNone())
                .minerContentSign(signedBlock.getContentBlock().getMinerSign())
                .minerBlockSign(signedBlock.getMinerSign())
                .auditorContentSign(signedBlock.getContentBlock().getAuditorSign())
                .auditorBlockSign(signedBlock.getAuditorSign())
                .contentSignTime(signedBlock.getContentBlock().getAuditorSignDateTime())
                .blockSignTime(signedBlock.getAuditorSignDateTime())
                .addTime(LocalDateTime.now())
                .build();
    }

    public static BlockChainBlockDto toBlockChainBlockDto(BlockChainBlock block){
        return BlockChainBlockDto.builder()
                .id(block.getId())
                .content(block.getContent())
                .minerId(block.getMinerId())
                .hash(block.getHash())
                .previousHash(block.getPreviousHash())
                .none(block.getNone())
                .minerContentSign(block.getMinerContentSign())
                .minerBlockSign(block.getMinerBlockSign())
                .auditorContentSign(block.getAuditorContentSign())
                .auditorBlockSign(block.getAuditorBlockSign())
                .contentSignTime(block.getContentSignTime())
                .blockSignTime(block.getBlockSignTime())
                .addTime(LocalDateTime.now())
                .build();
    }

}
