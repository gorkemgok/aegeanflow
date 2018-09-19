package com.aegeanflow.core.route.tunnel;

public interface StreamTunnel<I> extends AutoCloseable{

    void send(I item);

    @Override
    void close();
}
