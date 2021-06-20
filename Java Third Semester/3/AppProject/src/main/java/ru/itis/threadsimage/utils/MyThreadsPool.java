package ru.itis.threadsimage.utils;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

// wait, notify, synchronized
public class MyThreadsPool {

    // очередь задач
    private Deque<Runnable> tasks;

    // пул потоков
    private PoolWorker threads[];

    public MyThreadsPool(int threadsCount) {
        this.tasks = new ConcurrentLinkedDeque<>();
        this.threads = new PoolWorker[threadsCount];

        for (int i = 0; i < this.threads.length; i++) {
            this.threads[i] = new PoolWorker();
            this.threads[i].start();
        }
    }

    public void submit(Runnable task) {
        synchronized (tasks){
            tasks.add(task);
            tasks.notify();
        }
    }

    // класс - рабочий поток
    private class PoolWorker extends Thread {
        @Override
        public void run() {
            Runnable currentTask;
            while(true) {
                currentTask = null;
                synchronized (tasks){
                    if (tasks.size()==0){
                        try{
                            tasks.wait();
                        } catch (InterruptedException ex){
                            throw new IllegalArgumentException(ex);
                        }
                    }
                    currentTask = tasks.pollLast();
                }
                currentTask.run();
            }
        }
    }
}
