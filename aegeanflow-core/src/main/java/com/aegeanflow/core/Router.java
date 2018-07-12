package com.aegeanflow.core;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Router {

    private final List<Route<?, ?>> routeList;

    @Inject
    public Router() {
        this.routeList = new ArrayList<>();
    }

    public <T> void next(Node node, Output<T> output, Exchange<T> value) {
        //TODO: do parallel
        Optional<Route<T, Object>> routeOptional = routeList.stream().filter(route -> route.isOutputOf(node, output))
                .map(route -> (Route<T, Object>) route)
                .findFirst();
        if (routeOptional.isPresent()) {
            Route<T, Object> route = routeOptional.get();
            route.getTarget().getNode().acceptAndRun(route.getTarget().getInput(), value.get());
        }


    }

    public <O extends I, I> Route<O, I> connect(Node sourceNode, Output<O> sourceParameter,
                            Node targetNode, Input<I> targetParameter) {
        Route<O, I> route = new Route<>(new OutputPoint<>(sourceNode, sourceParameter),
                new InputPoint<>(targetNode, targetParameter));
        routeList.add(route);
        return route;
    }
}
