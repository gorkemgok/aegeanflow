package com.aegeanflow.core.route.tunnel;

import com.aegeanflow.core.spi.Streamable;

public class LocalStreamTunnel<I> implements StreamTunnel<I> {

    private final Streamable<I> stream;

    public LocalStreamTunnel(Streamable<I> stream) {
        this.stream = stream;
    }

    @Override
    public void send(I item) {
        stream.add(item);
    }

    @Override
    public void close() {
        stream.close();
    }
}
