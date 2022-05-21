package org.sbt.app.tokenring;


import org.apache.log4j.Logger;

public class Main {
    public final static int NODE_COUNT = 128;
    public final static int BATCH_COUNT = 500;
    public final static int CYCLE_COUNT = 5;

    private final static Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        TokenRing tokenRing = new TokenRing(NODE_COUNT, BATCH_COUNT);

        LOGGER.debug("node_id, batch_id, time_batch, time_node, is_success");
        tokenRing.start();

        //warm up JVM
        tokenRing.turnOffLogging();
        for (int i = 0; i < CYCLE_COUNT; i++) {
            tokenRing.sendData();
            tokenRing.refresh();
        }

        //real start
        tokenRing.turnOnLogging();
        for (int i = 0; i < CYCLE_COUNT; i++) {
            tokenRing.sendData();
            tokenRing.refresh();
        }

        System.out.println("All data received");

        tokenRing.stop();
    }
}
