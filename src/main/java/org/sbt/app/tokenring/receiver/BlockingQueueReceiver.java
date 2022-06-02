package org.sbt.app.tokenring.receiver;

import org.sbt.app.tokenring.Batch;
import org.sbt.app.tokenring.BatchReceiver;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

public class BlockingQueueReceiver implements BatchReceiver {
    private final BlockingQueue<Batch> queue = new LinkedBlockingQueue<>();
    private final Queue<Batch> buffer = new ArrayDeque<>();

    @Override
    public void sendToNext(Batch batch) throws InterruptedException {
        queue.put(batch);
        int bufferSize = buffer.size();
        for (int i = 0; i < bufferSize; i++) {
            queue.put(buffer.poll());
        }
    }

    @Override
    public Batch pollFromPrevious() throws InterruptedException {
        return queue.take();
    }

    @Override
    public void addToBuffer(Batch batch) {
        buffer.add(batch);
    }
}
