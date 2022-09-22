package ru.itis.encoding.algorithms.core;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DictionaryTreeNode {

    protected List<DictionaryTreeNode> subLeaves;
    protected Character symbol;

}
