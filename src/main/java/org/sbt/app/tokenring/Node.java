package org.sbt.app.tokenring;

import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicReference;

public class Node implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(Node.class);
    private final String nodeID;
    private final AtomicReference<Integer> dataReceivedCount;
    private final BatchReceiver previousReceiver;
    private final BatchReceiver nextReceiver;
    private long startWorkingDate;
    private volatile boolean needLogging = true;

    public Node(String nodeID, AtomicReference<Integer> dataReceivedCount, BatchReceiver previousReceiver, BatchReceiver nextReceiver) {
        this.nodeID = nodeID;
        this.dataReceivedCount = dataReceivedCount;
        this.previousReceiver = previousReceiver;
        this.nextReceiver = nextReceiver;
        startWorkingDate = System.nanoTime();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Batch batch = previousReceiver.pollFromPrevious();
                if (batch != null) {
                    receive(batch);
                }
            }
        } catch (Exception ignore) {
        }
    }

    public void turnOnLogging() {
        needLogging = true;
    }

    public void turnOffLogging() {
        needLogging = false;
    }

    public void updateStartWorkingDate() {
        startWorkingDate = System.nanoTime();
    }

    private void receive(Batch batch) throws InterruptedException {
        long deltaTime = System.nanoTime() - batch.getDateOfCreationInNanoSec();
        if (batch.getFinalReceiverId().equals(nodeID)) {
            dataReceivedCount.getAndUpdate((x) -> x + 1);
            if (needLogging) {
                LOGGER.debug(nodeID + "," + batch.getId() + "," + deltaTime + "," + (System.nanoTime() - startWorkingDate) + "," + 1);
            }
        } else {
            if (needLogging) {
//                LOGGER.debug(nodeID + "," + batch.getId() + "," + deltaTime + "," + 0);
            }
            nextReceiver.sendToNext(batch);
        }
    }
}
