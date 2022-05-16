package org.sbt.app.tokenring.receiver;

import org.sbt.app.tokenring.Batch;
import org.sbt.app.tokenring.BatchReceiver;

import java.util.concurrent.atomic.AtomicReference;

public class SimpleReceiver implements BatchReceiver {
    private final AtomicReference<Batch> batch = new AtomicReference<>();

    @Override
    public void sendToNext(Batch batch) throws InterruptedException {
//        while (this.batch.get() != null) {
//            Thread.sleep(5);
//        }
        this.batch.set(batch);
    }

    @Override
    public Batch pollFromPrevious() {
        Batch tempBatch = batch.get();
        this.batch.set(null);
        return tempBatch;
    }
}
