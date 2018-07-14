package com.aegeanflow.core.model.builder;

import com.aegeanflow.core.model.NodeUIModel;

public final class NodeUIModelBuilder {
    private Double x;
    private Double y;
    private Double w;
    private Double h;
    private String color;

    private NodeUIModelBuilder() {
    }

    public static NodeUIModelBuilder aNodeUIProxy() {
        return new NodeUIModelBuilder();
    }

    public NodeUIModelBuilder withX(Double x) {
        this.x = x;
        return this;
    }

    public NodeUIModelBuilder withY(Double y) {
        this.y = y;
        return this;
    }

    public NodeUIModelBuilder withW(Double w) {
        this.w = w;
        return this;
    }

    public NodeUIModelBuilder withH(Double h) {
        this.h = h;
        return this;
    }

    public NodeUIModelBuilder withColor(String color) {
        this.color = color;
        return this;
    }

    public NodeUIModel build() {
        return new NodeUIModel(x, y, w, h, color);
    }
}
