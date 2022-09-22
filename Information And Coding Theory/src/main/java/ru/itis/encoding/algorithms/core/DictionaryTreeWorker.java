package ru.itis.encoding.algorithms.core;

public class DictionaryTreeWorker{

    protected DictionaryTreeNode root;
    protected DictionaryTreeNode currentNode;

    public DictionaryTreeWorker(DictionaryTreeNode root){
        this.root = root;
        this.currentNode = root;
    }

    public Character moveDown(Integer leaf){
        currentNode = currentNode.getSubLeaves().get(leaf);
        return currentNode.getSymbol();
    }

    public void reset(){
        currentNode = root;
    }

}
