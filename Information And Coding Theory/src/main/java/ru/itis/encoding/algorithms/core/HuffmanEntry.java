package ru.itis.encoding.algorithms.core;

import lombok.*;

import java.util.List;

@Builder
@Data
public class HuffmanEntry {

    protected List<HuffmanEntry> subLeaves;
    protected double probability;
    protected char symbol;

}
