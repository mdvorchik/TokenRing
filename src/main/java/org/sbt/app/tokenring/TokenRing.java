package org.sbt.app.tokenring;

import org.sbt.app.tokenring.receiver.BlockingQueueReceiver;
import org.sbt.app.tokenring.receiver.ExchangerReceiver;
import org.sbt.app.tokenring.receiver.NonBlockingArrayReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class TokenRing {
    private final int nodeCount;
    private final int batchCount;
    private final long workingTime;
    private final List<Node> nodes = new ArrayList<>();
    private final ExecutorService executorService;
    private final BatchReceiver firstReceiver;
    private List<BatchReceiver> receivers = new ArrayList<>();

    public TokenRing(int nodeCount, int batchCount, long workingTime) {
        this.nodeCount = nodeCount;
        this.batchCount = batchCount;
        this.workingTime = workingTime;
        executorService = Executors.newFixedThreadPool(nodeCount);
        firstReceiver = getReceiver(batchCount, workingTime);

        receivers.add(firstReceiver);
        BatchReceiver secondReceiver = getReceiver(batchCount, workingTime);
        receivers.add(secondReceiver);
        Node node = new Node("0", firstReceiver, secondReceiver);
        nodes.add(node);
        for (int i = 1; i < nodeCount; i++) {
            BatchReceiver batchReceiver = (i == nodeCount - 1) ? firstReceiver : getReceiver(batchCount, workingTime);
            nodes.add(new Node("" + i, secondReceiver, batchReceiver));
            secondReceiver = batchReceiver;

            receivers.add(secondReceiver);
        }
    }

    private BatchReceiver getReceiver(int batchCount, long workingTime) {
//        return new NonBlockingArrayReceiver(batchCount);
//        return new BlockingQueueReceiver();
        return new ExchangerReceiver(workingTime);
    }

    private void start(long workingTime) {
        try {
            for (Future<Integer> res : executorService.invokeAll(nodes, workingTime, TimeUnit.SECONDS)) {
                res.get();
            }
        } catch (Exception ignore) {}
        executorService.shutdown();
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

    public void sendData(int numberOfStart) throws InterruptedException, TimeoutException {
        receivers = receivers.stream().distinct().collect(Collectors.toList());


        //all data from start to end node
        long startTime = System.nanoTime();
        long finishTime = startTime + TimeUnit.SECONDS.toNanos(workingTime);
        Batch firstBatch = new Batch(numberOfStart, 0, "data", "" + (nodeCount - 1), startTime, finishTime);
        Thread mainExecutor = new Thread(() -> {
            try {
//                start(workingTime);
                firstReceiver.sendToNext(firstBatch);
            } catch ( Exception e) {
                e.printStackTrace();
            }
        });
        for (int i = 1; i < batchCount; i++) {
            startTime = System.nanoTime();
            finishTime = startTime + TimeUnit.SECONDS.toNanos(workingTime);
            firstReceiver.addToBuffer(new Batch(numberOfStart, i, "data", "" + (nodeCount - 1), startTime, finishTime));
        }
        mainExecutor.start();
        start(workingTime);
        mainExecutor.join();
    }
}
