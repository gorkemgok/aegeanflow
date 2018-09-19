package com.aegeanflow.core.route.tunnel;

import java.util.Collection;

public class StreamTunnels<I> implements StreamTunnel<I> {

    private final Collection<StreamTunnel<I>> streamTunnels;

    public StreamTunnels(Collection<StreamTunnel<I>> streamTunnels) {
        this.streamTunnels = streamTunnels;
    }

    @Override
    public void send(I item) {
        streamTunnels.forEach(streamTunnel -> streamTunnel.send(item));
    }

    @Override
    public void close() {
        streamTunnels.forEach(StreamTunnel::close);
    }

}
