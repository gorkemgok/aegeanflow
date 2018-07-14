package com.aegeanflow.core.route;

import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.node.Node;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Router {

    private final List<Route<?, ?>> routeList;

    @Inject
    public Router() {
        this.routeList = new ArrayList<>();
    }

    public <T> void next(Node node, Output<T> output, Exchange<T> value) {
        //TODO: do parallel
        List<Route<T, Object>> matchingRoutes = routeList.stream().filter(route -> route.isOutputOf(node, output))
                .map(route -> (Route<T, Object>) route)
                .collect(Collectors.toList());
        matchingRoutes.forEach(route -> route.getTarget().getNode().accept(route.getTarget().getInput(), value.get()));
    }

    public <O extends I, I> Route<O, I> connect(Node sourceNode, Output<O> sourceParameter,
                            Node targetNode, Input<I> targetParameter) {
        Route<O, I> route = new Route<>(new OutputPoint<>(sourceNode, sourceParameter),
                new InputPoint<>(targetNode, targetParameter));
        routeList.add(route);
        return route;
    }
}
