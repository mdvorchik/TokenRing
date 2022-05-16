package org.sbt.app.tokenring;

import org.sbt.app.tokenring.receiver.SimpleReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TokenRing {
    private final int nodeCount;
    private final int batchCount;
    private final List<Node> nodes = new ArrayList<>();
    private final ExecutorService executorService;
    private final BatchReceiver firstReceiver = new SimpleReceiver();

    public TokenRing(int nodeCount, int batchCount) {

        this.nodeCount = nodeCount;
        this.batchCount = batchCount;
        executorService = Executors.newFixedThreadPool(nodeCount);

        BatchReceiver secondReceiver = new SimpleReceiver();
        Node node = new Node("0", firstReceiver, secondReceiver);
        nodes.add(node);
        for (int i = 1; i < nodeCount; i++) {
            BatchReceiver batchReceiver = (i == nodeCount - 1) ? firstReceiver : new SimpleReceiver();
            nodes.add(new Node(""+i, secondReceiver, batchReceiver));
            secondReceiver = batchReceiver;
        }
    }

    public void start() {
        for (Node node : nodes) {
            executorService.execute(node);
        }
    }

    public void sendData() throws InterruptedException {
        synchronized (firstReceiver) {
            firstReceiver.sendToNext(new Batch(UUID.randomUUID(), "data", "5", System.nanoTime()));
            firstReceiver.notify();
        }
    }

    public void stop() {
        executorService.shutdownNow();
    }

}
