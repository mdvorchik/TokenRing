package org.sbt.app.tokenring.receiver;

import org.sbt.app.tokenring.Batch;
import org.sbt.app.tokenring.BatchReceiver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public class BlockingTransferReceiver implements BatchReceiver {
    BlockingQueue<Batch> queue = new LinkedTransferQueue<>();

    @Override
    public void sendToNext(Batch batch) throws InterruptedException {
        queue.put(batch);
    }

    @Override
    public Batch pollFromPrevious() throws InterruptedException {
        return queue.take();
    }
}
