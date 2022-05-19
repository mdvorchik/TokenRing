package org.sbt.app.tokenring;


public class Main {
    public final static int NODE_COUNT = 8;
    public final static int BATCH_COUNT = 500;
    public final static int CYCLE_COUNT = 10;

    public static void main(String[] args) throws InterruptedException {
        TokenRing tokenRing = new TokenRing(NODE_COUNT, BATCH_COUNT);

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
