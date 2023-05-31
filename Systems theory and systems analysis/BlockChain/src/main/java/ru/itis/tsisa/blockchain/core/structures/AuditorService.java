package ru.itis.tsisa.blockchain.core.structures;

import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.core.blocks.Block;

import java.security.PublicKey;

@Component
public interface AuditorService {

    void signBlock(Block block);

    PublicKey getPublicKey();


}
