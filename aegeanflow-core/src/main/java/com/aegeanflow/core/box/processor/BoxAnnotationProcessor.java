package com.aegeanflow.core.box.processor;

import com.aegeanflow.core.box.BoxInfo;
import com.aegeanflow.core.box.BoxInputInfo;
import com.aegeanflow.core.box.definition.BoxDefinition;
import com.aegeanflow.core.box.definition.BoxIODDefComparator;
import com.aegeanflow.core.box.definition.BoxIODefinition;
import com.aegeanflow.core.box.definition.builder.BoxDefinitionBuilder;
import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.aegeanflow.core.spi.annotation.*;
import com.aegeanflow.core.spi.box.AnnotatedBox;
import com.google.inject.Inject;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.*;

public class BoxAnnotationProcessor {

    private final BoxOutputAnnotationProcessor outputProcessor;

    private final BoxInputAnnotationProcessor inputProcessor;

    @Inject
    public BoxAnnotationProcessor(BoxOutputAnnotationProcessor outputProcessor, BoxInputAnnotationProcessor inputProcessor) {
        this.outputProcessor = outputProcessor;
        this.inputProcessor = inputProcessor;
    }

    public BoxInfo process(Class<? extends AnnotatedBox> nodeClass) throws IllegalNodeConfigurationException {


        NodeEntry nodeEntry = nodeClass.getAnnotation(NodeEntry.class);
        if (nodeEntry != null) {
            BoxInputInfo boxInputInfo = inputProcessor.process(nodeClass);
            List<BoxIODefinition> nodeOutputDefinitionList = outputProcessor.process(nodeClass);

            Collections.sort(boxInputInfo.getIoDefinitionList(), BoxIODDefComparator.INSTANCE);
            Collections.sort(nodeOutputDefinitionList, BoxIODDefComparator.INSTANCE);

            BoxDefinition boxDefinition = BoxDefinitionBuilder.aBoxDefinition()
                .withType(nodeClass)
                .withName(!nodeEntry.name().isEmpty() ? nodeEntry.name() : nodeClass.getSimpleName())
                .withInputs(boxInputInfo.getIoDefinitionList())
                .withOutputs(nodeOutputDefinitionList).build();

            return new BoxInfo(nodeClass, boxDefinition, boxInputInfo.getMethods());
        }
        throw new IllegalNodeConfigurationException("AnnotatedNode type must have @NodeEntry annotation");
    }

    public static String getVarName(Method method){
        String methodName = method.getName();
        return Introspector.decapitalize(methodName.substring(methodName.startsWith("is") ? 2 : 3));
    }
}
