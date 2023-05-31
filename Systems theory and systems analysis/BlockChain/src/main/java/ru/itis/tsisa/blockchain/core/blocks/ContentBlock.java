package ru.itis.tsisa.blockchain.core.blocks;

import lombok.*;
import ru.itis.tsisa.blockchain.utils.BytesUtils;

@Getter
@Setter
@RequiredArgsConstructor
public class ContentBlock implements Block {

    private final InformationBlock informationBlock;
    private final byte[] previousHash;
    private byte[] minerSign;
    private String auditorSignDateTime;
    private byte[] auditorSign;

    public byte[] getBytes(){
        byte[] bytesBody = informationBlock.getBytes();
        bytesBody = BytesUtils.concatenate(bytesBody, previousHash);
        if (minerSign != null){
            bytesBody = BytesUtils.concatenate(bytesBody, minerSign);
        }
        if (auditorSign != null){
            bytesBody = BytesUtils.concatenate(bytesBody, auditorSign);
        }
        return bytesBody;
    }

}
