package com.aegeanflow.core.route.tunnel;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.exchange.StreamExchange;
import com.aegeanflow.core.route.InputPoint;
import com.aegeanflow.core.spi.Streamable;

public interface FlowTunnel {

    <T> void accept(InputPoint<T> inputPoint, T value);

    <E extends Exchange<T>, T> void acceptExchange(InputPoint<T> inputPoint, E value);

    <T extends Streamable<I>, I> StreamTunnel<I> acceptStreamable(InputPoint inputPoint, T value);

    <E extends StreamExchange<T, I>, T extends Streamable<I> , I> StreamTunnel<I> acceptStreamExchange(InputPoint<T> inputPoint, E value);

    TunnelType getTunnelType();
}
