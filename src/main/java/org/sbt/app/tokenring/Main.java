package org.sbt.app.tokenring;


public class Main {
    public final static int NODE_COUNT = 8;
    public final static int BATCH_COUNT = 1000;
    public final static int CYCLE_COUNT = 10;

    public static void main(String[] args) throws InterruptedException {
        TokenRing tokenRing = new TokenRing(NODE_COUNT, BATCH_COUNT);

        tokenRing.start();

        //warm up JVM
        tokenRing.turnOffLogging();
        for (int i = 0; i < CYCLE_COUNT; i++) {
            tokenRing.sendData();
        }
        Thread.sleep(7 * BATCH_COUNT);

        //real start
        tokenRing.refresh();
        tokenRing.turnOnLogging();
        for (int i = 0; i < CYCLE_COUNT; i++) {
            tokenRing.sendData();
        }
        Thread.sleep(7 * BATCH_COUNT);
        if (tokenRing.getBatchCountReceived() == BATCH_COUNT * CYCLE_COUNT) {
            System.out.println("All data received");
        } else {
            System.out.printf("ERROR: only %d / %d batch received%n", tokenRing.getBatchCountReceived(), BATCH_COUNT * CYCLE_COUNT);
        }
        tokenRing.stop();
    }
}
