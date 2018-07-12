package com.aegeanflow.core.proxy.builder;

import com.aegeanflow.core.proxy.NodeProxy;
import com.aegeanflow.core.proxy.NodeUIProxy;
import com.aegeanflow.core.spi.AnnotatedBox;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
public class NodeProxyBuilder {

    private Map<String, Object> configMap;

    private Class<? extends AnnotatedBox> nodeClass;

    private UUID uuid;

    private String name;

    private NodeUIProxy ui;

    public NodeProxyBuilder(Class<? extends AnnotatedBox> nodeClass) {
        this.nodeClass = nodeClass;
        configMap = new HashMap<>();
    }

    public NodeProxyBuilder addConfig(String key, Object value){
        configMap.put(key, value);
        return this;
    }

    public NodeProxyBuilder uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public NodeProxyBuilder name(String name) {
        this.name = name;
        return this;
    }

    public NodeProxyBuilder ui(NodeUIProxy ui) {
        this.ui = ui;
        return this;
    }

    public NodeProxy build(){
        NodeProxy nodeProxy = new NodeProxy(name, uuid, nodeClass, configMap, ui);
        return nodeProxy;
    }
}
