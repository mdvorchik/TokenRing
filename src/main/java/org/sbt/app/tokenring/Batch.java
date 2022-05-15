package org.sbt.app.tokenring;

import java.util.UUID;

public class Batch {
    private final UUID uuid;
    private final String data;

    private final String finalReceiverId;
    private final long dateOfCreationInNanoSec;

    public Batch(UUID uuid, String data, String finalReceiverId, long dateOfCreationInNanoSec) {
        this.uuid = uuid;
        this.data = data;
        this.finalReceiverId = finalReceiverId;
        this.dateOfCreationInNanoSec = dateOfCreationInNanoSec;
    }


    public UUID getUuid() {
        return uuid;
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
                "uuid=" + uuid +
                ", data='" + data + '\'' +
                ", finalReceiverId=" + finalReceiverId +
                ", dateOfCreationInNanoSec=" + dateOfCreationInNanoSec +
                '}';
    }
}
