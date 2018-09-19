package com.aegeanflow.core.route.tunnel.rest;

import com.aegeanflow.core.route.tunnel.StreamTunnel;
import com.aegeanflow.core.spi.Streamable;

public class RestStreamTunnel<I> implements StreamTunnel<I> {

    private final Streamable<I> streamable;

    public RestStreamTunnel(Streamable<I> streamable) {
        this.streamable = streamable;
    }

    @Override
    public void send(I item) {
        streamable.add(item);
    }

    @Override
    public void close() {
        streamable.close();
    }
}
