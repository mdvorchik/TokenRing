package org.sbt.app.tokenring;

public class Batch {
    private final int numberOfStart;
    private final int id;
    private final String data;

    private final String finalReceiverId;
    private final long dateOfFinishInNanoSec;
    private long dateOfCreationInNanoSec;

    public Batch(int numberOfStart, int id, String data, String finalReceiverId, long dateOfCreationInNanoSec, long dateOfFinishInNanoSec) {
        this.numberOfStart = numberOfStart;
        this.id = id;
        this.data = data;
        this.finalReceiverId = finalReceiverId;
        this.dateOfCreationInNanoSec = dateOfCreationInNanoSec;
        this.dateOfFinishInNanoSec = dateOfFinishInNanoSec;
    }


    public int getId() {
        return id;
    }


    public String getData() {
        return data;
    }


    public String getFinalReceiverId() {
        return finalReceiverId;
    }

    public long getDateOfCreationInNanoSec() {
        return dateOfCreationInNanoSec;
    }

    public void setDateOfCreationInNanoSec(long dateOfCreationInNanoSec) {
        this.dateOfCreationInNanoSec = dateOfCreationInNanoSec;
    }

    public int getNumberOfStart() {
        return numberOfStart;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", finalReceiverId=" + finalReceiverId +
                ", dateOfCreationInNanoSec=" + dateOfCreationInNanoSec +
                '}';
    }

    public long getDateOfFinishInNanoSec() {
        return dateOfFinishInNanoSec;
    }
}
