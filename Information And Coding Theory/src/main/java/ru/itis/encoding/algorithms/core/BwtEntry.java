package ru.itis.encoding.algorithms.core;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BwtEntry {

    protected Character firstSymbol;
    protected Character secondSymbol;
    protected Integer firstIndex;
    protected Integer secondIndex;

}
