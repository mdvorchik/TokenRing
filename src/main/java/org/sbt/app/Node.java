package org.sbt.app;

public interface Node {
    int getId();

    void receive(Batch batch);
}
