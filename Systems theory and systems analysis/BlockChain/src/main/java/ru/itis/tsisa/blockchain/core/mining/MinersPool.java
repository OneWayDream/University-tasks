package ru.itis.tsisa.blockchain.core.mining;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itis.tsisa.blockchain.core.encryption.KeyManager;
import ru.itis.tsisa.blockchain.core.structures.AuditorService;
import ru.itis.tsisa.blockchain.core.structures.BlockChain;
import ru.itis.tsisa.blockchain.core.structures.IncomingInformationBlocksQueue;
import ru.itis.tsisa.blockchain.core.blocks.SignedBlock;
import ru.itis.tsisa.blockchain.core.blocks.InformationBlock;
import ru.itis.tsisa.blockchain.exceptions.BlockVerificationException;
import ru.itis.tsisa.blockchain.exceptions.EntityNotFoundException;
import ru.itis.tsisa.blockchain.exceptions.MinersWaitingException;

import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Component
public class MinersPool {

    private final IncomingInformationBlocksQueue incomingInformationBlocksQueue;
    private final BlockChain blockChain;
    private final KeyManager keyManager;
    private final MiningUtils miningUtils;
    private final AuditorService auditorService;
    private final int MINERS_AMOUNT;

    private List<Miner> miners;
    private MinersPoolWorker minersPoolWorker;

    public MinersPool(
            IncomingInformationBlocksQueue incomingInformationBlocksQueue,
            BlockChain blockChain,
            KeyManager keyManager,
            MiningUtils miningUtils,
            @Value("${block-chain.miners-amount}") int minersAmount,
            AuditorService auditorService
    ) {
        this.incomingInformationBlocksQueue = incomingInformationBlocksQueue;
        this.blockChain = blockChain;
        this.keyManager = keyManager;
        this.miningUtils = miningUtils;
        this.auditorService = auditorService;
        initMiners(minersAmount);
        MINERS_AMOUNT = minersAmount;
        start();
    }

    private void initMiners(int minersAmount){
        miners = new ArrayList<>();
        for (int i = 0; i < minersAmount; i++){
            miners.add(getMiner(i));
        }
    }

    private Miner getMiner(int id){
        Miner currentMiner = new Miner(id, this, keyManager, miningUtils, auditorService);
        currentMiner.setName("Miner " + id);
        currentMiner.setDaemon(true);
        return currentMiner;
    }

    private void start(){
        initMinersPoolWorker();
        minersPoolWorker.start();
    }

    private void initMinersPoolWorker(){
        minersPoolWorker = new MinersPoolWorker();
        minersPoolWorker.setDaemon(true);
    }

    public InformationBlock getCurrentInformationBlock(){
        lockMinerBeforeTask();
        return minersPoolWorker.currentInformationBlock;
    }

    public byte[] getPreviousBlockHash(){
        return minersPoolWorker.previousBlockHash;
    }

    public synchronized void offerBlockChainBlock(SignedBlock block){
        minersPoolWorker.offeredHashedBlocks.offer(block);
        notifyWorker();
    }

    public boolean isHashFound(){
        return minersPoolWorker.isHashFound;
    }

    public void awaitAllMiners(){
        minersPoolWorker.awaitAllMinersAfterTask();
    }

    private void lockMinerBeforeTask(){
        minersPoolWorker.awaitAllMinersBeforeTask();
    }

    private void notifyWorker(){
        synchronized (minersPoolWorker.workerLock){
            minersPoolWorker.workerLock.notify();
        }
    }


    private class MinersPoolWorker extends Thread {

        private volatile InformationBlock currentInformationBlock;
        private volatile byte[] previousBlockHash;
        private final Object workerLock = new Object();
        private final Object finishedMinersLock = new Object();
        private final Queue<SignedBlock> offeredHashedBlocks = new LinkedList<>();
        private volatile boolean isHashFound;
        private CyclicBarrier afterTaskCyclicBarrier;
        private CyclicBarrier beforeTaskCyclicBarrier;

        @Override
        public void run() {
            initVariables();
            startMiners();
            while (!Thread.interrupted()){
                addNextBlock();
                waitForCorrectBlock();
            }
        }

        private void initVariables(){
            this.setName("Miners Pool Worker");
            afterTaskCyclicBarrier = new CyclicBarrier(MINERS_AMOUNT + 1, () -> beforeTaskCyclicBarrier.reset());
            beforeTaskCyclicBarrier = new CyclicBarrier(MINERS_AMOUNT + 1, () -> afterTaskCyclicBarrier.reset());
        }

        private void startMiners(){
            for (Miner miner : miners){
                miner.start();
            }
        }

        private void addNextBlock(){
            currentInformationBlock = incomingInformationBlocksQueue.peek();
            addPreviousBlockHash();
            resetVariablesBeforeNewBlock();
            awaitAllMinersBeforeTask();
        }

        private void addPreviousBlockHash(){
            try{
                previousBlockHash = blockChain.getLastBlock().getHash();
            } catch (EntityNotFoundException ex){
                previousBlockHash = blockChain.getInitializingVector();
            }
        }

        private void resetVariablesBeforeNewBlock(){
            isHashFound = false;
            offeredHashedBlocks.clear();
        }

        private void waitForCorrectBlock(){
            boolean gotCorrectBlock = false;
            SignedBlock currentBlock;
            while (!gotCorrectBlock){
                while (offeredHashedBlocks.isEmpty()){
                    lockWorker();
                }
                currentBlock = offeredHashedBlocks.poll();
                gotCorrectBlock = tryToAddCurrentBlock(currentBlock);
            }
            endBlockHashing();
        }

        private void lockWorker(){
            try{
                executeWorkerLock();
            } catch (InterruptedException e) {
                System.out.println("Something went wrong for the thread " + Thread.currentThread().getName()
                        + "(" + Thread.currentThread().getId() + ").");
            }
        }

        private void endBlockHashing(){
            isHashFound = true;
            changeVariablesAfterHandledBlock();
            notifyAllFinishedMiners();
            awaitAllMinersAfterTask();
        }

        private void changeVariablesAfterHandledBlock(){
            incomingInformationBlocksQueue.poll();
            currentInformationBlock = null;
        }

        private void awaitAllMinersBeforeTask(){
            try{
                beforeTaskCyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException ex) {
                throw new MinersWaitingException(ex);
            }
        }

        private void awaitAllMinersAfterTask(){
            if (!isHashFound) {
                waitOfCheck();
            }
            try{
                afterTaskCyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException ex) {
                throw new MinersWaitingException(ex);
            }
        }

        private void waitOfCheck(){
            synchronized (finishedMinersLock){
                try{
                    finishedMinersLock.wait();
                } catch (InterruptedException ex) {
                    System.out.println("Something went wrong for the thread " + Thread.currentThread().getName()
                            + "(" + Thread.currentThread().getId() + ").");
                }
            }
        }

        private void notifyAllFinishedMiners(){
            synchronized (finishedMinersLock){
                finishedMinersLock.notifyAll();
            }
        }

        private void executeWorkerLock() throws InterruptedException {
            synchronized (workerLock){
                workerLock.wait();
            }
        }

        private boolean tryToAddCurrentBlock(SignedBlock currentBlock){
            boolean isCorrectBlock = false;
            try{
                blockChain.addNewBlock(currentBlock);
                isCorrectBlock = true;
            } catch (BlockVerificationException ex){
                System.out.println(ex.getMessage());
            }
            return isCorrectBlock;
        }
    }

}
