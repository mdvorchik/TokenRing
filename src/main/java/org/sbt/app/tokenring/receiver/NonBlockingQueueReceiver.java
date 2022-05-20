package org.sbt.app.tokenring.receiver;

import org.sbt.app.tokenring.Batch;
import org.sbt.app.tokenring.BatchReceiver;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NonBlockingQueueReceiver implements BatchReceiver {
    private final Queue<Batch> queue = new ConcurrentLinkedQueue<>();

    @Override
    public void sendToNext(Batch batch) throws InterruptedException {
        queue.add(batch);
    }

    @Override
    public Batch pollFromPrevious() throws InterruptedException {
        return queue.poll();
    }
}
