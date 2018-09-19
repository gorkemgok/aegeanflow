package com.aegeanflow.core.route;

import com.aegeanflow.core.spi.Streamable;
import com.aegeanflow.core.route.tunnel.StreamTunnel;
import com.aegeanflow.core.exchange.StreamExchange;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.node.Node;

public class NodeRouter {

    private final Node node;

    private final Router router;

    public NodeRouter(Node node, Router router) {
        this.node = node;
        this.router = router;
    }

    public <T> void next(Output<T> output, T value) {

    }

    public <T extends Streamable<I>, I> StreamTunnel<I> next(Output<T> output, T value) {
        return router.next(node, output, value);
    }

    public <T> void next(Output<T> output, Exchange<T> value) {
        router.next(node, output, value);
    }

    public <T extends Streamable<I>, I> void next(Output<T> output, StreamExchange<T, I> value) {
        router.next(node, output, value);
    }

    public void next(Output<Double> output, Double value) {
        next(output, Exchange.of(value));
    }

    public void next(Output<String> output, String value) {
        next(output, Exchange.of(value));
    }
}
