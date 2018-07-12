package com.aegeanflow.core.proxy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NodeUIProxy {

    private final Double x, y, w, h;

    private final String color;

    @JsonCreator
    public NodeUIProxy(
            @JsonProperty Double x,
            @JsonProperty Double y,
            @JsonProperty Double w,
            @JsonProperty Double h,
            @JsonProperty String color) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = color;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getW() {
        return w;
    }

    public Double getH() {
        return h;
    }

    public String getColor() {
        return color;
    }
}
