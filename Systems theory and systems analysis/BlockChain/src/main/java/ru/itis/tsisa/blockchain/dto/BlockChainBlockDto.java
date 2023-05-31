package ru.itis.tsisa.blockchain.dto;

import lombok.Builder;
import lombok.Data;
import ru.itis.tsisa.blockchain.core.encryption.BlockChainTerms;
import ru.itis.tsisa.blockchain.utils.BytesUtils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Data
@Builder
public class BlockChainBlockDto {

    private Long id;
    private String content;
    private int minerId;
    private byte[] hash;
    private byte[] previousHash;
    private long none;
    private byte[] minerContentSign;
    private byte[] minerBlockSign;
    private byte[] auditorContentSign;

    private byte[] auditorBlockSign;

    private String contentSignTime;

    private String blockSignTime;

    private LocalDateTime addTime;

    public byte[] getContentBytes(boolean isForAuditor){
        byte[] bytesBody = content.getBytes(StandardCharsets.UTF_8);
        bytesBody = BytesUtils.concatenate(bytesBody, previousHash);
        if (isForAuditor){
            bytesBody = BytesUtils.concatenate(bytesBody, minerContentSign);
        }
        return bytesBody;
    }

    public byte[] getBlockBytes(boolean isForAuditor){
        byte[] bytesBody = getFullContentBytes();
        bytesBody = BytesUtils.concatenate(bytesBody, ByteBuffer.allocate(4).putInt(minerId)
                .array());
        if (isForAuditor){
            bytesBody = BytesUtils.concatenate(bytesBody, hash);
            bytesBody = BytesUtils.concatenate(bytesBody, ByteBuffer.allocate(BlockChainTerms.NONE_BYTES_SIZE).putLong(none)
                    .array());
            bytesBody = BytesUtils.concatenate(bytesBody, minerBlockSign);
        }
        return bytesBody;
    }

    public byte[] getFullContentBytes(){
        byte[] bytesBody = getContentBytes(true);
        bytesBody = BytesUtils.concatenate(bytesBody, auditorContentSign);
        return bytesBody;
    }

}
