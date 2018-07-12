package com.aegeanflow.core;

import com.aegeanflow.core.definition.NodeDefinition;
import com.aegeanflow.core.spi.AnnotatedBox;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

/**
 * Created by gorkem on 12.01.2018.
 */
public class NodeInfo {

    private final Class<? extends AnnotatedBox> nodeClass;

    private final NodeDefinition definition;

    private final Map<String, Method> inputMethods;

    private final Map<String, Method> configMethods;

    public NodeInfo(Class<? extends AnnotatedBox> nodeClass, NodeDefinition definition, Map<String, Method> inputMethods, Map<String, Method> configMethods) {
        this.nodeClass = nodeClass;
        this.definition = definition;
        this.inputMethods = Collections.unmodifiableMap(inputMethods);
        this.configMethods = Collections.unmodifiableMap(configMethods);
    }

    public Class<? extends AnnotatedBox> getNodeClass() {
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
