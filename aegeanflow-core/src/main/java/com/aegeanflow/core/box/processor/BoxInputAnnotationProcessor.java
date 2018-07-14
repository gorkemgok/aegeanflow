package com.aegeanflow.core.box.processor;

import com.aegeanflow.core.box.BoxInputInfo;
import com.aegeanflow.core.box.definition.BoxIODefinition;
import com.aegeanflow.core.box.definition.builder.BoxIODefinitionBuilder;
import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.aegeanflow.core.spi.annotation.NodeInput;
import com.aegeanflow.core.spi.box.AnnotatedBox;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aegeanflow.core.box.processor.BoxAnnotationProcessor.getVarName;

public class BoxInputAnnotationProcessor {

    public BoxInputInfo process(Class<? extends AnnotatedBox> nodeClass) throws IllegalNodeConfigurationException {
        Map<String, Method> methods = new HashMap<>();
        List<BoxIODefinition> ioDefinitionList = new ArrayList<>();
        for (Method method : nodeClass.getMethods()) {
            NodeInput annotation = method.getAnnotation(NodeInput.class);
            String varName = getVarName(method);
            if (annotation != null) {
                if (method.getParameterCount() == 1) {
                    methods.put(varName, method);
                    BoxIODefinition ioDefinition = BoxIODefinitionBuilder.aBoxIODefinition()
                        .withType(method.getParameters()[0].getType())
                        .withName(varName)
                        .withLabel(!annotation.label().isEmpty() ? annotation.label() : getVarName(method))
                        .withOrder(annotation.order())
                        .withInputType(annotation.inputType()).build();
                    ioDefinitionList.add(ioDefinition);
                } else {
                    throw new IllegalNodeConfigurationException("Input setter method must have exactly one parameter");
                }
            }
        }
        return new BoxInputInfo(methods, ioDefinitionList);
    }
}
