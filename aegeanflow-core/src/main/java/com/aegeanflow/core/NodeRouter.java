package com.aegeanflow.core;

import com.aegeanflow.core.exchange.Exchange;

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

    public void next(Output<Double> output, Double value) {
        next(output, Exchange.of(value));
    }

    public void next(Output<String> output, String value) {
        next(output, Exchange.of(value));
    }
}
