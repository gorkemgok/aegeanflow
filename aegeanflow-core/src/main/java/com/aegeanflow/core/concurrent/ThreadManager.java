package com.aegeanflow.core.concurrent;

import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.spi.node.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ThreadManager {

    private final List<NodeFuture> futureList;

    public ThreadManager() {
        this.futureList = Collections.synchronizedList(new ArrayList<>());
    }

    public NodeFuture run(Session session, Node node) {
        CompletableFuture future = CompletableFuture.runAsync(node::execute);
        NodeFuture nodeFuture = new NodeFuture(session, node, future);
        futureList.add(nodeFuture);
        future.thenApply(result -> futureList.remove(nodeFuture));
        return nodeFuture;
    }

    public void awaitCompletion(Session session) {
        for (int i = 0; i < futureList.size(); i++) {
            futureList.get(i).join();
        }
    }
}
