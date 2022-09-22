package ru.itis.entries;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CbcMacMessage {

    protected String content;
    protected String tag;

}
