package ru.itis.encoding.algorithms.core;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MtfEntry {

    protected MtfEntry next;
    protected Character character;

}
