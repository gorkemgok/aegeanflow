package com.aegeanflow.core.session;

import com.aegeanflow.core.spi.node.Node;

public class SessionThread extends Thread {

    private final Node node;

    private final Session session;


    public SessionThread(Node node, Session session) {
        super("Node Thread:" + node.getUUID());
        this.node = node;
        this.session = session;
    }

    @Override
    public void run() {
        node.execute();
    }

    public Node getNode() {
        return node;
    }

    public Session getSession() {
        return session;
    }
}
