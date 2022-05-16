package org.sbt.app.tokenring;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        TokenRing tokenRing = new TokenRing(8, 1);

        tokenRing.start();

        Thread.sleep(100);

        tokenRing.sendData();
        Thread.sleep(100);
        tokenRing.sendData();
        Thread.sleep(100);
        System.out.println("Works");
        tokenRing.stop();
    }
}
