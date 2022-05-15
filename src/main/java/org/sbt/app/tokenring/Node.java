package org.sbt.app.tokenring;

import org.sbt.app.util.ThreadID;

public interface Node {
    default int getId() {
     return ThreadID.get();
    }

    void receive(Batch batch);
}
