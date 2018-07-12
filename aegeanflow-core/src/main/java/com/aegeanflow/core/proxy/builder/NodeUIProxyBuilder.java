package com.aegeanflow.core.proxy.builder;

import com.aegeanflow.core.proxy.NodeUIProxy;

public class NodeUIProxyBuilder {

    private Double x, y, w, h;

    private String color;

    public NodeUIProxyBuilder x(Double x) {
        this.x = x;
        return this;
    }

    public NodeUIProxyBuilder y(Double y) {
        this.y = y;
        return this;
    }

    public NodeUIProxyBuilder h(Double h) {
        this.h = h;
        return this;
    }

    public NodeUIProxyBuilder w(Double w) {
        this.w = w;
        return this;
    }

    public NodeUIProxyBuilder color(String color) {
        this.color = color;
        return this;
    }

    public NodeUIProxy build() {
        return new NodeUIProxy(x, y, w, h, color);
    }
}
