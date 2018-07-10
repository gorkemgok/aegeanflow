package com.aegeanflow.core;

import java.util.List;
import java.util.Optional;

public class Router {

    private final List<Route<?>> routeList;

    public Router(List<Route<?>> routeList) {
        this.routeList = routeList;
    }

    public <T> void next(Node node, Parameter<T> output, T value) {
        Optional<Route<T>> routeOptional = routeList.stream().filter(route -> route.isOutputOf(node, output))
                .map(route -> (Route<T>) route)
                .findFirst();
        if (routeOptional.isPresent()) {
            Route<T> route = routeOptional.get();
            route.getTarget().getNode().accept(route.getTarget().getParameter(), value);
        }

    }

    public <T> Route<T> connect(Node sourceNode, Parameter<T> sourceParameter,
                            Node targetNode, Parameter<T> targetParameter) {
        Route<T> route = new Route<>(new RoutePoint<>(sourceNode, sourceParameter),
                new RoutePoint<>(targetNode, targetParameter));
        routeList.add(route);
        return route;
    }
}
