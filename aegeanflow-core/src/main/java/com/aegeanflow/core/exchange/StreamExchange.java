package com.aegeanflow.core.exchange;

import com.aegeanflow.core.spi.Streamable;
import com.aegeanflow.core.route.tunnel.StreamTunnel;

public interface StreamExchange<T extends Streamable<I>, I> extends Exchange<T> {

    void add(I item);

    void setStreamTunnel(StreamTunnel<I> streamTunnel);

}
