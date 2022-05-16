package org.sbt.app.tokenring.receiver;

import org.sbt.app.tokenring.Batch;
import org.sbt.app.tokenring.BatchReceiver;

import java.util.concurrent.Exchanger;

public class ExchangerReceiver implements BatchReceiver {
    private final Exchanger<Batch> exchanger = new Exchanger<>();

    @Override
    public void sendToNext(Batch batch) throws InterruptedException {
         exchanger.exchange(batch);
    }

    @Override
    public Batch pollFromPrevious() throws InterruptedException {
        return exchanger.exchange(null);
    }
}
