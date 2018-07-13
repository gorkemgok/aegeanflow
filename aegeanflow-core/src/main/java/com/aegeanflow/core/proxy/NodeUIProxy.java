package com.aegeanflow.core.proxy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NodeUIProxy {

    private final Double x;
    private final Double y;
    private final Double w;
    private final Double h;
    private final String color;

    @JsonCreator
    public NodeUIProxy(
            @JsonProperty("x") Double x,
            @JsonProperty("y") Double y,
            @JsonProperty("w") Double w,
            @JsonProperty("h") Double h,
            @JsonProperty("color") String color) {
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
