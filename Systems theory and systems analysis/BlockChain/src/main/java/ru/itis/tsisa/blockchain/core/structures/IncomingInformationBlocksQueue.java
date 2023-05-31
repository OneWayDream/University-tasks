package ru.itis.tsisa.blockchain.core.structures;

import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.core.blocks.InformationBlock;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
public class IncomingInformationBlocksQueue {

    private final Queue<InformationBlock> informationBlocksQueue = new LinkedList<>();

    public void add(InformationBlock informationBlock){
        informationBlocksQueue.add(informationBlock);
        executeNotify();
    }

    public void addAll(List<InformationBlock> informationBlocks){
        informationBlocksQueue.addAll(informationBlocks);
        executeNotify();
    }

    public void poll(){
        checkIfQueueIsEmpty();
        informationBlocksQueue.poll();
        displayIfQueueIsEmpty();
    }

    public InformationBlock peek(){
        checkIfQueueIsEmpty();
        return informationBlocksQueue.peek();
    }

    private void displayIfQueueIsEmpty(){
        if (informationBlocksQueue.isEmpty()){
            System.out.println("(´• ω •)ﾉ The blocks have been successfully added to the BlockChain.");
        }
    }

    private void checkIfQueueIsEmpty(){
        if (informationBlocksQueue.isEmpty()){
            executeConsumerWait();
        }
    }

    private void executeConsumerWait(){
        try{
            executeWait();
        } catch (InterruptedException e) {
            System.out.println("Something went wrong for the thread " + Thread.currentThread().getName()
                    + "(" + Thread.currentThread().getId() + ").");
        }
    }

    private void executeWait() throws InterruptedException {
        synchronized (this){
            this.wait();
        }
    }

    private void executeNotify(){
        synchronized (this){
            this.notify();
        }
    }

}
