package ru.itis.tsisa.blockchain.core.blocks;

import lombok.*;
import ru.itis.tsisa.blockchain.core.encryption.BlockChainTerms;
import ru.itis.tsisa.blockchain.utils.BytesUtils;

import java.nio.ByteBuffer;

@Getter
@Setter
@Builder
public class SignedBlock implements Block{

    private ContentBlock contentBlock;
    private byte[] hash;
    private Long none;
    private Integer minerId;
    private byte[] minerSign;

    private String auditorSignDateTime;

    private byte[] auditorSign;

    public byte[] getBytes(){
        byte[] bytesBody = contentBlock.getBytes();
        if (minerId != null){
            bytesBody = BytesUtils.concatenate(bytesBody, ByteBuffer.allocate(4).putInt(minerId)
                    .array());
        }
        if (hash != null){
            bytesBody = BytesUtils.concatenate(bytesBody, hash);
        }
        if (none != null){
            bytesBody = BytesUtils.concatenate(bytesBody, ByteBuffer.allocate(BlockChainTerms.NONE_BYTES_SIZE).putLong(none)
                    .array());
        }
        if (minerSign != null){
            bytesBody = BytesUtils.concatenate(bytesBody, minerSign);
        }
        return bytesBody;
    }

}
