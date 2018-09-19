package com.aegeanflow.core.route.tunnel.rest;

import com.aegeanflow.core.deployment.Deployment;
import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.route.OutputPoint;
import com.aegeanflow.core.route.tunnel.FlowTunnelListener;

import java.util.function.Consumer;

public class RestFlowTunnelListener implements FlowTunnelListener {
    @Override
    public <T> void register(OutputPoint<T> outputPoint, Deployment deployment, Consumer<Exchange<T>> exchangeConsumer) {

    }
}
