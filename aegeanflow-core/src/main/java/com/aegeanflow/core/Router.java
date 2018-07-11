package com.aegeanflow.core;

import java.util.List;
import java.util.Optional;

public class Router {

    private final List<Route<?>> routeList;

    public Router(List<Route<?>> routeList) {
        this.routeList = routeList;
    }

    public <T> void next(Node node, Output<T> output, Exchange<T> value) {
        //TODO: do parallel
        Optional<Route<T>> routeOptional = routeList.stream().filter(route -> route.isOutputOf(node, output))
                .map(route -> (Route<T>) route)
                .findFirst();
        if (routeOptional.isPresent()) {
            Route<T> route = routeOptional.get();
            route.getTarget().getNode().accept(route.getTarget().getInput(), value.get());
        }

    }

    public <T> Route<T> connect(Node sourceNode, Output<T> sourceParameter,
                            Node targetNode, Input<T> targetParameter) {
        Route<T> route = new Route<>(new OutputPoint<>(sourceNode, sourceParameter),
                new InputPoint<>(targetNode, targetParameter));
        routeList.add(route);
        return route;
    }
}
