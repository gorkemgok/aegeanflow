package com.aegeanflow.core;

public class RoutePoint<T> {

    private final Node node;

    private final Parameter<T> parameter;


    public RoutePoint(Node node, Parameter<T> parameter) {
        this.node = node;
        this.parameter = parameter;
    }

    public Node getNode() {
        return node;
    }

    public Parameter<T> getParameter() {
        return parameter;
    }
}
