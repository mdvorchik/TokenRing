package org.sbt.app.tokenring;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Batch {
    private final UUID uuid;
    private final String data;
    private final List<Long> keyDatesInNanoSec = new ArrayList<>();

    private final int finalReceiverId;
    private final long dateOfCreationInNanoSec;
    private long dateOfDeliveryInNanoSec;

    public Batch(UUID uuid, String data, int finalReceiverId, long dateOfCreationInNanoSec) {
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


    public List<Long> getKeyDatesInNanoSec() {
        return keyDatesInNanoSec;
    }


    public int getFinalReceiverId() {
        return finalReceiverId;
    }

    public long getDateOfCreationInNanoSec() {
        return dateOfCreationInNanoSec;
    }

    public long getDateOfDeliveryInNanoSec() {
        return dateOfDeliveryInNanoSec;
    }

    public void setDateOfDeliveryInNanoSec(long dateOfDeliveryInNanoSec) {
        this.dateOfDeliveryInNanoSec = dateOfDeliveryInNanoSec;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "uuid=" + uuid +
                ", data='" + data + '\'' +
                ", keyDatesInNanoSec=" + keyDatesInNanoSec +
                ", finalReceiverId=" + finalReceiverId +
                ", dateOfCreationInNanoSec=" + dateOfCreationInNanoSec +
                ", dateOfDeliveryInNanoSec=" + dateOfDeliveryInNanoSec +
                ", timeOfDeliveryInNanoSec=" + (dateOfDeliveryInNanoSec - dateOfCreationInNanoSec) +
                '}';
    }
}
