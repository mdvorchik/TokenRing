package org.sbt.app.tokenring;

import java.util.concurrent.TimeoutException;

public interface BatchReceiver {
    void sendToNext(Batch batch) throws InterruptedException, TimeoutException;

    Batch pollFromPrevious() throws InterruptedException, TimeoutException;

    void addToBuffer(Batch batch);
}
