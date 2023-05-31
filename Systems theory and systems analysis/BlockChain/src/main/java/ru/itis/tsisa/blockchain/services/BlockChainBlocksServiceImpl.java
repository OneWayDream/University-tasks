package ru.itis.tsisa.blockchain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.tsisa.blockchain.exceptions.EntityNotFoundException;
import ru.itis.tsisa.blockchain.models.BlockChainBlock;
import ru.itis.tsisa.blockchain.repositories.BlockChainBlocksRepository;

@Service
@RequiredArgsConstructor
public class BlockChainBlocksServiceImpl implements BlockChainBlocksService {

    private final BlockChainBlocksRepository repository;

    @Override
    public void add(BlockChainBlock block) {
        repository.save(block);
    }

    @Override
    public BlockChainBlock getLast() {
        return repository.findLastBlock().orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public BlockChainBlock getById(Long blockId) {
        return repository.findById(blockId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public BlockChainBlock getFirst() {
        return repository.findFirstBlock().orElseThrow(EntityNotFoundException::new);
    }

}
