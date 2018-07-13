package com.aegeanflow.core.proxy.builder;

import com.aegeanflow.core.AnnotatedNode;
import com.aegeanflow.core.Node;
import com.aegeanflow.core.proxy.NodeProxy;
import com.aegeanflow.core.proxy.NodeUIProxy;
import com.aegeanflow.core.spi.AnnotatedBox;

import java.util.Map;
import java.util.UUID;

public final class NodeProxyBuilder {
    private String label;
    private UUID uuid;
    private Class<? extends Node> type;
    private Class<? extends AnnotatedBox> boxType;
    private Map<String, Object> configuration;
    private NodeUIProxy ui;

    private NodeProxyBuilder() {
    }

    public static NodeProxyBuilder aNodeProxy() {
        return new NodeProxyBuilder();
    }

    public NodeProxyBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    public NodeProxyBuilder withUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public NodeProxyBuilder withRandomUUID() {
        this.uuid = UUID.randomUUID();
        return this;
    }

    public NodeProxyBuilder withType(Class<? extends Node> type) {
        this.type = type;
        return this;
    }

    public NodeProxyBuilder withBoxType(Class<? extends AnnotatedBox> boxType) {
        this.boxType = boxType;
        return withType(AnnotatedNode.class);
    }

    public NodeProxyBuilder withConfiguration(Map<String, Object> configuration) {
        this.configuration = configuration;
        return this;
    }

    public NodeProxyBuilder withUi(NodeUIProxy ui) {
        this.ui = ui;
        return this;
    }

    public NodeProxy build() {
        return new NodeProxy(label, uuid, type, boxType, configuration, ui);
    }
}
