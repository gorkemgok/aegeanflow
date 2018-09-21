package com.aegeanflow.core.concurrent;

import com.aegeanflow.core.exception.NodeRuntimeException;
import com.aegeanflow.core.logger.SessionLogManager;
import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.spi.node.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Phaser;

public class ThreadManager {

    private final List<NodeFuture> futureList;

    private final Phaser phaser = new Phaser();

    public ThreadManager() {
        phaser.register();
        this.futureList = Collections.synchronizedList(new ArrayList<>());
    }

    public NodeFuture run(Session session, Node node) {
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            try {
                node.execute();
            } catch (Exception e) {
                throw new NodeRuntimeException(e, node.getId());
            }
        }).exceptionally(ex -> {
            SessionLogManager logManager = session.getContext().getLogManager();
            logManager.log(node);
            logManager.log(ex);
            return null;
        });
        NodeFuture nodeFuture = new NodeFuture(session, node, future);
        futureList.add(nodeFuture);
        phaser.register();
        future.thenApply(result -> {
            futureList.remove(nodeFuture);
            phaser.arriveAndDeregister();
            session.getContext().getLogManager().log(node);
            return null;
        });
        return nodeFuture;
    }

    public void awaitCompletion(Session session) {
        phaser.arriveAndAwaitAdvance();
    }
}
