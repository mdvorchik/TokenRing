package org.sbt.app.tokenring.node;

import org.sbt.app.tokenring.Batch;
import org.sbt.app.tokenring.Node;

public class SimpleNode implements Node {
    private final Node nextNode;

    public SimpleNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    public void receive(Batch batch) {
        if (batch.getFinalReceiverId() == this.getId()) {
            batch.setDateOfDeliveryInNanoSec(System.nanoTime());
            System.out.println(batch);
        } else {
            nextNode.receive(batch);
        }
    }
}
