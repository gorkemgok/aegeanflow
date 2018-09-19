package com.aegeanflow.core.route;

import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.spi.node.Node;

public class Route<O extends I, I> {

    private final OutputPoint<O> source;

    private final InputPoint<I> target;

    private final RouteOptions options;

    public Route(OutputPoint<O> source, InputPoint<I> target, RouteOptions options) {
        this.source = source;
        this.target = target;
        this.options = options;
    }

    public Route(OutputPoint<O> source, InputPoint<I> target) {
        this(source, target, new RouteOptions());
    }

    public OutputPoint<O> getSource() {
        return source;
    }

    public InputPoint<I> getTarget() {
        return target;
    }

    public boolean isOutputOf(Node node, Output<?> parameter) {
        return source.getNode().equals(node) && source.getOutput().isAssignable(parameter);
    }

    public RouteOptions getOptions() {
        return options;
    }
}
