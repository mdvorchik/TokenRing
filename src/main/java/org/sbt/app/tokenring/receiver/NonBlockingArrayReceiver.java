package org.sbt.app.tokenring.receiver;

import org.sbt.app.tokenring.Batch;
import org.sbt.app.tokenring.BatchReceiver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class NonBlockingArrayReceiver implements BatchReceiver {
    private final List<Batch> batches;
    private volatile int writeIndex = -1;
    private volatile int readIndex = 0;
    private final Queue<Batch> buffer = new ArrayDeque<>();

    public NonBlockingArrayReceiver(int batchCount) {
        this.batches = new ArrayList<>(batchCount);
        for (int i = 0; i < batchCount; i++) {
            this.batches.add(null);
        }
    }

    @Override
    public void sendToNext(Batch batch) throws InterruptedException {
        int nextwriteIndex = writeIndex + 1;
        batches.set(nextwriteIndex % batches.size(), batch);
        writeIndex++;
        int bufferSize = buffer.size();
        for (int i = 0; i < bufferSize; i++) {
            nextwriteIndex = writeIndex + 1;
            batches.set(nextwriteIndex % batches.size(), buffer.poll());
            writeIndex++;
        }
    }

    @Override
    public Batch pollFromPrevious() throws InterruptedException {
        boolean isEmpty = writeIndex < readIndex;
        if (!isEmpty) {
            Batch batch = batches.get(readIndex % batches.size());
            readIndex++;
            return batch;
        } else {
            return null;
        }
    }


    @Override
    public void addToBuffer(Batch batch) {
        buffer.add(batch);
    }
}
