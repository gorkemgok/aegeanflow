package com.aegeanflow.core.box;

import com.aegeanflow.core.box.definition.BoxDefinition;
import com.aegeanflow.core.spi.box.AnnotatedBox;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

/**
 * Created by gorkem on 12.01.2018.
 */
public class BoxInfo {

    private final Class<? extends AnnotatedBox> nodeClass;

    private final BoxDefinition definition;

    private final Map<String, Method> inputMethods;

    public BoxInfo(Class<? extends AnnotatedBox> nodeClass, BoxDefinition definition, Map<String, Method> inputMethods) {
        this.nodeClass = nodeClass;
        this.definition = definition;
        this.inputMethods = Collections.unmodifiableMap(inputMethods);
    }

    public Class<? extends AnnotatedBox> getNodeClass() {
        return nodeClass;
    }

    public BoxDefinition getDefinition() {
        return definition;
    }

    public Map<String, Method> getInputMethods() {
        return inputMethods;
    }
}
