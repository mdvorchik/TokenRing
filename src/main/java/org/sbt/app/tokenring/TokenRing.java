package org.sbt.app.tokenring;

import org.sbt.app.tokenring.receiver.ExchangerReceiver;
import org.sbt.app.tokenring.receiver.NonBlockingQueueReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class TokenRing {
    private final int nodeCount;
    private final int batchCount;
    private final List<Node> nodes = new ArrayList<>();
    private final ExecutorService executorService;
    private final BatchReceiver firstReceiver = new NonBlockingQueueReceiver();
    private final AtomicReference<Integer> dataReceivedCount = new AtomicReference<>(0);
    private List<BatchReceiver> receivers = new ArrayList<>();

    public TokenRing(int nodeCount, int batchCount) {

        this.nodeCount = nodeCount;
        this.batchCount = batchCount;
        executorService = Executors.newFixedThreadPool(nodeCount);


        receivers.add(firstReceiver);
        BatchReceiver secondReceiver = new NonBlockingQueueReceiver();
        receivers.add(secondReceiver);
        Node node = new Node("0", dataReceivedCount, firstReceiver, secondReceiver);
        nodes.add(node);
        for (int i = 1; i < nodeCount; i++) {
            BatchReceiver batchReceiver = (i == nodeCount - 1) ? firstReceiver : new ExchangerReceiver();
            nodes.add(new Node("" + i, dataReceivedCount, secondReceiver, batchReceiver));
            secondReceiver = batchReceiver;

            receivers.add(secondReceiver);
        }
    }

    public void start() {
        for (Node node : nodes) {
            executorService.execute(node);
        }
    }

    public void turnOffLogging() {
        for (Node node : nodes) {
            node.turnOffLogging();
        }
    }

    public void turnOnLogging() {
        for (Node node : nodes) {
            node.turnOnLogging();
        }
    }

    public int getBatchCountReceived(){
        return dataReceivedCount.get();
    }

    public void sendData() throws InterruptedException {
        receivers = receivers.stream().distinct().collect(Collectors.toList());

        //normal distribution
//        for (int i = 0; i < batchCount; i++) {
//            firstReceiver.sendToNext(new Batch(i, "data", "" + (int) (Math.random() * nodeCount), System.nanoTime()));
//        }

        //all data from start to end node
        for (int i = 0; i < batchCount; i++) {
            firstReceiver.sendToNext(new Batch(i, "data", "" + (nodeCount - 1), System.nanoTime()));
        }
    }

    public void stop() {
        executorService.shutdownNow();
    }

    public void refresh() {
        dataReceivedCount.set(0);
    }
}
