package com.aegeanflow.core;

import com.aegeanflow.core.definition.NodeDefinition;
import com.aegeanflow.core.spi.Node;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

/**
 * Created by gorkem on 12.01.2018.
 */
public class CompiledNodeInfo {

    private final Class<? extends Node> nodeClass;

    private final NodeDefinition definition;

    private final Map<String, Method> inputMethods;

    private final Map<String, Method> configMethods;

    public CompiledNodeInfo(Class<? extends Node> nodeClass, NodeDefinition definition, Map<String, Method> inputMethods, Map<String, Method> configMethods) {
        this.nodeClass = nodeClass;
        this.definition = definition;
        this.inputMethods = Collections.unmodifiableMap(inputMethods);
        this.configMethods = Collections.unmodifiableMap(configMethods);
    }

    public Class<? extends Node> getNodeClass() {
        return nodeClass;
    }

    public NodeDefinition getDefinition() {
        return definition;
    }

    public Map<String, Method> getInputMethods() {
        return inputMethods;
    }

    public Map<String, Method> getConfigMethods() {
        return configMethods;
    }
}
