package org.sbt.app.tokenring;

import org.apache.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Node implements Callable<Integer> {
    private final static Logger LOGGER = Logger.getLogger(Node.class);
    private final String nodeID;
    private final BatchReceiver previousReceiver;
    private final BatchReceiver nextReceiver;
    private volatile boolean needLogging = true;

    public Node(String nodeID, BatchReceiver previousReceiver, BatchReceiver nextReceiver) {
        this.nodeID = nodeID;
        this.previousReceiver = previousReceiver;
        this.nextReceiver = nextReceiver;
    }

    @Override
    public Integer call() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Batch batch = previousReceiver.pollFromPrevious();
                if (batch != null) {
                    if (batch.getDateOfFinishInNanoSec() > System.nanoTime()) {
                        receive(batch);
                    } else {
                        return 0;
                    }
                }
            }
            return 1;
        } catch (Exception ignore) {
            return -1;
        }
    }

    public void turnOnLogging() {
        needLogging = true;
    }

    public void turnOffLogging() {
        needLogging = false;
    }

    private void receive(Batch batch) throws InterruptedException, TimeoutException {
        long deltaTime = System.nanoTime() - batch.getDateOfCreationInNanoSec();
        if (needLogging) {
            LOGGER.debug(batch.getNumberOfStart() + "," + nodeID + "," + batch.getId() + "," + deltaTime);
        }
        batch.setDateOfCreationInNanoSec(System.nanoTime());
        nextReceiver.sendToNext(batch);
    }
}

