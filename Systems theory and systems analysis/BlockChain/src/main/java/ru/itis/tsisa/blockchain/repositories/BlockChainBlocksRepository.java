package ru.itis.tsisa.blockchain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.tsisa.blockchain.models.BlockChainBlock;

import java.util.Optional;

public interface BlockChainBlocksRepository extends JpaRepository<BlockChainBlock, Long> {

    @Query(value = "SELECT * FROM blockchain_block WHERE blockchain_block.id=(SELECT MAX(id) FROM blockchain_block)",
            nativeQuery = true)
    Optional<BlockChainBlock> findLastBlock();

    @Query(value = "SELECT * FROM blockchain_block WHERE blockchain_block.id=(SELECT MIN(id) FROM blockchain_block)",
            nativeQuery = true)
    Optional<BlockChainBlock> findFirstBlock();


    
}
