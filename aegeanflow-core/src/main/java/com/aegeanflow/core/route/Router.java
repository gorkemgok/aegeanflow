package com.aegeanflow.core.route;

import com.aegeanflow.core.concurrent.ThreadManager;
import com.aegeanflow.core.route.tunnel.*;
import com.aegeanflow.core.deployment.Deployment;
import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.spi.Streamable;
import com.aegeanflow.core.exchange.StreamExchange;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.node.Node;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Router {

    private final List<Route<?, ?>> routeList;

    private final FlowTunnelProvider flowTunnelProvider;

    private final FlowTunnelListener flowTunnelListener;

    private final Session session;

    private final ThreadManager threadManager;

    @Inject
    public Router(FlowTunnelProvider flowTunnelProvider,
                  FlowTunnelListener flowTunnelListener,
                  @Assisted Session session, ThreadManager threadManager) {
        this.session = session;
        this.flowTunnelProvider = flowTunnelProvider;
        this.flowTunnelListener = flowTunnelListener;
        this.threadManager = threadManager;
        this.routeList = new ArrayList<>();
    }

    public <T> void next(Node node, Output<T> output, T value) {
        forMatchingRoutes(node, output, route -> {
            Node targetNode = route.getTarget().getNode();
            if (route.getTarget().getDeployment().equals(Deployment.LOCAL)) {
                targetNode.accept(route.getTarget().getInput(), value);
                if (targetNode.isReady() && targetNode.getState() == Node.State.WAITING) {
                    threadManager.run(session, targetNode);
                }
            } else {
                flowTunnelProvider.get(route.getOptions().getTunnelType()).accept(route.getTarget(), value);
            }
        });
    }

    public <T extends Streamable<I>, I> StreamTunnel<I> next(Node node, Output<T> output, T value) {
        List<StreamTunnel<I>> streamTunnels = new ArrayList<>();
        forMatchingRoutes(node, output, route -> {
            Node targetNode = route.getTarget().getNode();
            if (route.getTarget().getDeployment().equals(Deployment.LOCAL)) {
                targetNode.accept(route.getTarget().getInput(), value);
                if (targetNode.isReady() && targetNode.getState() == Node.State.WAITING) {
                    threadManager.run(session, targetNode);
                }
                streamTunnels.add(new LocalStreamTunnel<>(value));
            } else {
                StreamTunnel<I> streamTunnel =
                    flowTunnelProvider.get(route.getOptions().getTunnelType())
                        .acceptStreamable(route.getTarget(), value);
                streamTunnels.add(streamTunnel);
            }
        });
        return new StreamTunnels<>(streamTunnels);
    }

    public <T> void next(Node node, Output<T> output, Exchange<T> exchange) {
        next(node, output, exchange.get());
    }

    public <T extends Streamable<I>, I> StreamTunnel<I> next(Node node, Output<T> output, StreamExchange<T, I> exchange) {
        return next(node, output, exchange.get());
    }

    // CONNECT METHODS
    public <O extends I, I> Route<O, I> connect(Node sourceNode, Output<O> sourceParameter,
                                                Node targetNode, Input<I> targetParameter, RouteOptions routeOptions) {
        return connect(sourceNode, sourceParameter, Deployment.LOCAL, targetNode, targetParameter, Deployment.LOCAL, routeOptions);
    }

    public <O extends I, I> Route<O, I> connect(
        Node sourceNode, Output<O> sourceParameter, Deployment outputDeployment,
        Node targetNode, Input<I> targetParameter, Deployment inputDeployment,
        RouteOptions routeOptions)
    {
        Route<O, I> route = new Route<>(new OutputPoint<>(sourceNode, sourceParameter, outputDeployment),
                new InputPoint<>(session, targetNode, targetParameter, inputDeployment), routeOptions);
        if (!outputDeployment.equals(Deployment.LOCAL)) {
            flowTunnelListener.register(route.getSource(), outputDeployment,
                exchange -> next(route.getSource().getNode(), route.getSource().getOutput(), exchange));

        }
        routeList.add(route);
        return route;
    }

    public void disconnect(Route route) {

    }

    //LOOP HELPER
    private <T> void forMatchingRoutes(Node node, Output<T> output, Consumer<Route<T, Object>> consumer) {
        List<Route<T, Object>> matchingRoutes = routeList.stream().filter(route -> route.isOutputOf(node, output))
            .map(route -> (Route<T, Object>) route)
            .collect(Collectors.toList());
        matchingRoutes.forEach(consumer);
    }
}
