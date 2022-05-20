package org.sbt.app.tokenring.receiver;

import org.sbt.app.tokenring.Batch;
import org.sbt.app.tokenring.BatchReceiver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class SynchronousQueueReceiver implements BatchReceiver {
    private final BlockingQueue<Batch> queue = new SynchronousQueue<>();

    @Override
    public void sendToNext(Batch batch) throws InterruptedException {
        queue.put(batch);
    }

    @Override
    public Batch pollFromPrevious() throws InterruptedException {
        return queue.take();
    }
}
