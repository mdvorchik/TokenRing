package org.sbt.app.tokenring;


import org.apache.log4j.Logger;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {
    public final static int NODE_COUNT = 4;
    public final static int BATCH_COUNT = 1;
    public final static int WORKING_TIME_IN_SEC = 1;

    private final static Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        TokenRing tokenRing = new TokenRing(NODE_COUNT, BATCH_COUNT, WORKING_TIME_IN_SEC);

        LOGGER.debug("number_of_start, node_id, batch_id, time_batch");

        //warm up JVM
        tokenRing.turnOffLogging();
        tokenRing.sendData(0);
        long s = System.currentTimeMillis();
        System.out.println(0);
        //real starts
        for (int i = 1; i <= 3; i++) {
            tokenRing = new TokenRing(NODE_COUNT, BATCH_COUNT, WORKING_TIME_IN_SEC);
            tokenRing.sendData(i);
            System.out.println(i);
        }

        long f = System.currentTimeMillis();
        System.out.println("All data received at " + (TimeUnit.MILLISECONDS.toSeconds(f-s)));
    }
}
