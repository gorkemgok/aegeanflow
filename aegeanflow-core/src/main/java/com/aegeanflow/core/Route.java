package com.aegeanflow.core;

public class Route<T> {

    private final RoutePoint<T> source;

    private final RoutePoint<T> target;

    public Route(RoutePoint<T> source, RoutePoint<T> target) {
        this.source = source;
        this.target = target;
    }

    public RoutePoint<T> getSource() {
        return source;
    }

    public RoutePoint<T> getTarget() {
        return target;
    }

    public boolean isOutputOf(Node node, Parameter<?> parameter) {
        return source.getNode().equals(node) && source.getParameter().equals(parameter);
    }
}
