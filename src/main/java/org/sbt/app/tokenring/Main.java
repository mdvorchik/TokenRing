package org.sbt.app.tokenring;


public class Main {
    public final static int NODE_COUNT = 8;
    public final static int BATCH_COUNT = 5;

    public static void main(String[] args) throws InterruptedException {
        TokenRing tokenRing = new TokenRing(NODE_COUNT, BATCH_COUNT);

        tokenRing.start();

        //warm up JVM
        tokenRing.turnOffLogging();
        for (int i = 0; i < 100; i++) {
            tokenRing.sendData();
        }
        Thread.sleep(100);

        //real start
        tokenRing.refresh();
        tokenRing.turnOnLogging();
        tokenRing.sendData();
        Thread.sleep(100);
        if (tokenRing.getBatchCountReceived() == BATCH_COUNT) {
            System.out.println("All data received");
        } else {
            System.out.printf("ERROR: only %d / %d batch received%n", tokenRing.getBatchCountReceived(), BATCH_COUNT);
        }
        tokenRing.stop();
    }
}
