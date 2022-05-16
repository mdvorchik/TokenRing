package org.sbt.app.tokenring;

import org.apache.log4j.Logger;

public class Node implements Runnable {
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
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Batch batch;
                synchronized (previousReceiver) {
                    while ((batch = previousReceiver.pollFromPrevious()) == null) {
                        previousReceiver.wait();
                    }
                }
                receive(batch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnOnLogging() {
        needLogging = true;
    }

    public void turnOffLogging() {
        needLogging = false;
    }

    private void receive(Batch batch) throws InterruptedException {
        long deltaTime = System.nanoTime() - batch.getDateOfCreationInNanoSec();
        if (batch.getFinalReceiverId().equals(nodeID)) {
            if (needLogging) {
                LOGGER.debug(nodeID + "," + batch.getUuid() + "," + deltaTime + "," + 1);
            }
        } else {
            if (needLogging) {
                LOGGER.debug(nodeID + "," + batch.getUuid() + "," + deltaTime + "," + 0);
            }
            synchronized (nextReceiver) {
                nextReceiver.sendToNext(batch);
                nextReceiver.notify();
            }
        }
    }
}
