package com.aegeanflow.core.flow;

import com.aegeanflow.core.spi.AnnotatedBox;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
public class FlowNodeBuilder {

    private Map<String, Object> configMap;

    private Class<? extends AnnotatedBox> nodeClass;

    public FlowNodeBuilder(Class<? extends AnnotatedBox> nodeClass) {
        this.nodeClass = nodeClass;
        configMap = new HashMap<>();
    }

    public FlowNodeBuilder addConfig(String key, Object value){
        configMap.put(key, value);
        return this;
    }

    public FlowNode build(){
        FlowNode flowNode = new FlowNode();
        flowNode.setUUID(UUID.randomUUID());
        flowNode.setNodeClass(nodeClass);
        flowNode.setConfiguration(configMap);
        return flowNode;
    }
}
