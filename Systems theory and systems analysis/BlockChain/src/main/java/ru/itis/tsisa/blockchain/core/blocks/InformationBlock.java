package ru.itis.tsisa.blockchain.core.blocks;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.nio.charset.StandardCharsets;

@Data
@AllArgsConstructor
public class InformationBlock {

    private String content;

    public byte[] getBytes(){
        return content.getBytes(StandardCharsets.UTF_8);
    };

}
