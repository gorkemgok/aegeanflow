package com.aegeanflow.core.exchange;

import com.aegeanflow.core.route.tunnel.StreamTunnel;

public abstract class AbstractExchange<T> implements Exchange<T>{

    protected final T value;

    protected StreamTunnel streamTunnel;

    public AbstractExchange(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    public void setStreamTunnel(StreamTunnel streamTunnel) {
        this.streamTunnel = streamTunnel;
    }
}
