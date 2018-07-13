package com.aegeanflow.core.proxy.builder;

import com.aegeanflow.core.proxy.NodeUIProxy;

public final class NodeUIProxyBuilder {
    private Double x;
    private Double y;
    private Double w;
    private Double h;
    private String color;

    private NodeUIProxyBuilder() {
    }

    public static NodeUIProxyBuilder aNodeUIProxy() {
        return new NodeUIProxyBuilder();
    }

    public NodeUIProxyBuilder withX(Double x) {
        this.x = x;
        return this;
    }

    public NodeUIProxyBuilder withY(Double y) {
        this.y = y;
        return this;
    }

    public NodeUIProxyBuilder withW(Double w) {
        this.w = w;
        return this;
    }

    public NodeUIProxyBuilder withH(Double h) {
        this.h = h;
        return this;
    }

    public NodeUIProxyBuilder withColor(String color) {
        this.color = color;
        return this;
    }

    public NodeUIProxy build() {
        return new NodeUIProxy(x, y, w, h, color);
    }
}
