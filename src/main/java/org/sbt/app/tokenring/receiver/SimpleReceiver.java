package org.sbt.app.tokenring.receiver;

import org.sbt.app.tokenring.Batch;
import org.sbt.app.tokenring.BatchReceiver;

public class SimpleReceiver implements BatchReceiver {
    private Batch batch = null;

    @Override
    public void sendToNext(Batch batch) {
        while (this.batch != null) {

        }
        this.batch = batch;
    }

    @Override
    public Batch pollFromPrevious() {
        try {
            return this.batch;
        } finally {
            this.batch = null;
        }
    }
}
