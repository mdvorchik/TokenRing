package org.sbt.app.tokenring.receiver;

import org.sbt.app.tokenring.Batch;
import org.sbt.app.tokenring.BatchReceiver;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExchangerReceiver implements BatchReceiver {
    private final Exchanger<Batch> exchanger = new Exchanger<>();
    private final long workingTime;
    private final Queue<Batch> buffer = new ArrayDeque<>();

    public ExchangerReceiver(long workingTime) {
        this.workingTime = workingTime;
    }

    @Override
    public void sendToNext(Batch batch) throws InterruptedException, TimeoutException {
        exchanger.exchange(batch, workingTime, TimeUnit.SECONDS);
        int bufferSize = buffer.size();
        for (int i = 0; i < bufferSize; i++) {
            exchanger.exchange(buffer.poll(), workingTime, TimeUnit.SECONDS);
        }
    }

    @Override
    public Batch pollFromPrevious() throws InterruptedException, TimeoutException {
        return exchanger.exchange(null, workingTime, TimeUnit.SECONDS);
    }


    @Override
    public void addToBuffer(Batch batch) {
        buffer.add(batch);
    }
}
