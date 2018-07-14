package com.aegeanflow.core.box.processor;

import com.aegeanflow.core.box.definition.BoxIODefinition;
import com.aegeanflow.core.box.definition.builder.BoxIODefinitionBuilder;
import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.annotation.NodeOutput;
import com.aegeanflow.core.spi.annotation.NodeOutputBean;
import com.aegeanflow.core.spi.box.AnnotatedBox;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static com.aegeanflow.core.box.processor.BoxAnnotationProcessor.getVarName;

public class BoxOutputAnnotationProcessor {

    public List<BoxIODefinition> process(Class<? extends AnnotatedBox> nodeClass) throws IllegalNodeConfigurationException {
        try {
            List<BoxIODefinition> nodeOutputDefinitionList = new ArrayList<>();
            Method callMethod = nodeClass.getMethod("call");
            NodeOutputBean nodeOutputBean = callMethod.getReturnType().getAnnotation(NodeOutputBean.class);
            if (nodeOutputBean != null) {
                for (Method outputMethod : callMethod.getReturnType().getMethods()) {
                    String outputName = getVarName(outputMethod);
                    NodeOutput nodeOutput = outputMethod.getAnnotation(NodeOutput.class);
                    if (nodeOutput != null) {
                        if (!outputMethod.isAccessible()) {
                            throw new IllegalNodeConfigurationException("NodeOutput method must be public");
                        }
                        if (!outputMethod.getReturnType().isAssignableFrom(Exchange.class)) {
                            throw new IllegalNodeConfigurationException("NodeOutput method return type must be Exchange");
                        }
                        BoxIODefinition boxIODefinition = BoxIODefinitionBuilder.aBoxIODefinition()
                            .withType((Class) ((ParameterizedType) outputMethod.getGenericReturnType()).getActualTypeArguments()[0])
                            .withMethod(outputMethod)
                            .withName(outputName)
                            .withLabel(!nodeOutput.label().isEmpty() ? nodeOutput.label() : outputName)
                            .withOrder(nodeOutput.order()).build();
                        nodeOutputDefinitionList.add(boxIODefinition);
                    }
                }
            } else if (!callMethod.getReturnType().equals(Void.class)) {
                BoxIODefinition boxIODefinition = BoxIODefinitionBuilder.aBoxIODefinition()
                    .withType((Class) ((ParameterizedType) callMethod.getGenericReturnType()).getActualTypeArguments()[0])
                    .withName("main")
                    .withLabel("main")
                    .withOrder(0).build();
                nodeOutputDefinitionList.add(boxIODefinition);
            }
            return nodeOutputDefinitionList;
        } catch (NoSuchMethodException e) {
            throw new IllegalNodeConfigurationException("AnnotatedNode class must have a call() method with no parameter");
        }
    }
}
