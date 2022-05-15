package org.sbt.app.tokenring;

public interface BatchReceiver {
    void sendToNext(Batch batch);

    Batch pollFromPrevious();
}
