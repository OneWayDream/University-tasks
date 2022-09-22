package ru.itis.encoding.algorithms.core;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Interval {

    protected BigDecimal start;
    protected BigDecimal end;

}
