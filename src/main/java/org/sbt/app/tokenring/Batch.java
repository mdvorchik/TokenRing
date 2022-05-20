package org.sbt.app.tokenring;

import java.util.UUID;

public class Batch {
    private final int id;
    private final String data;

    private final String finalReceiverId;
    private final long dateOfCreationInNanoSec;

    public Batch(int id, String data, String finalReceiverId, long dateOfCreationInNanoSec) {
        this.id = id;
        this.data = data;
        this.finalReceiverId = finalReceiverId;
        this.dateOfCreationInNanoSec = dateOfCreationInNanoSec;
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


    @Override
    public String toString() {
        return "Batch{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", finalReceiverId=" + finalReceiverId +
                ", dateOfCreationInNanoSec=" + dateOfCreationInNanoSec +
                '}';
    }
}
