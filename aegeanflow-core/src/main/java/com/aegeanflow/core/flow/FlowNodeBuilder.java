package com.aegeanflow.core.flow;

import com.aegeanflow.core.spi.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
public class FlowNodeBuilder {

    private Map<String, Object> configMap;

    private Class<? extends Node> nodeClass;

    public FlowNodeBuilder(Class<? extends Node> nodeClass) {
        this.nodeClass = nodeClass;
        configMap = new HashMap<>();
    }

    public FlowNodeBuilder addConfig(String key, Object value){
        configMap.put(key, value);
        return this;
    }

    public FlowNode build(){
        FlowNode flowNode = new FlowNode();
        flowNode.setUuid(UUID.randomUUID());
        flowNode.setNodeClass(nodeClass.getTypeName());
        flowNode.setConfiguration(configMap);
        return flowNode;
    }
}
