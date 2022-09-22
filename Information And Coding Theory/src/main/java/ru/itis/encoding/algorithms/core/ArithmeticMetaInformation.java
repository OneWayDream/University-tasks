package ru.itis.encoding.algorithms.core;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class ArithmeticMetaInformation {

    protected Map<Character, BigDecimal> probabilities;
    protected Integer length;

}
