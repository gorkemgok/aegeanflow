package com.aegeanflow.core.route.tunnel;

import com.aegeanflow.core.deployment.Deployment;
import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.route.OutputPoint;

import java.util.function.Consumer;

public interface FlowTunnelListener {

    <T> void register(OutputPoint<T> outputPoint, Deployment deployment, Consumer<Exchange<T>> exchangeConsumer);
}
