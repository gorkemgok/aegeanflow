package com.aegeanflow.core;

public class NodeRouter {

    private final Node node;

    private final Router router;

    public NodeRouter(Node node, Router router) {
        this.node = node;
        this.router = router;
    }

    public <T> void next(Output<T> output, Exchange<T> value) {
        router.next(node, output, value);
    }
}
