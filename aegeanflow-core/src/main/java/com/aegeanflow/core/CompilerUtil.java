package com.aegeanflow.core;

import com.aegeanflow.core.definition.BoxConfigurationDefinition;
import com.aegeanflow.core.definition.BoxDefinition;
import com.aegeanflow.core.definition.BoxIODDefComparator;
import com.aegeanflow.core.definition.BoxIODefinition;
import com.aegeanflow.core.spi.AnnotatedBox;
import com.aegeanflow.core.spi.annotation.*;
import com.aegeanflow.core.exception.IllegalNodeConfigurationException;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class CompilerUtil {

    public static BoxInfo compile(Class<? extends AnnotatedBox> nodeClass) throws IllegalNodeConfigurationException {
        Map<String, Method> inputMethods = new HashMap<>();
        Map<String, Method> configMethods = new HashMap<>();
        NodeEntry nodeEntry = nodeClass.getAnnotation(NodeEntry.class);
        if (nodeEntry != null) {
            BoxDefinition boxDefinition = new BoxDefinition();
            boxDefinition.setType(nodeClass);
            boxDefinition.setLabel(!nodeEntry.label().isEmpty() ? nodeEntry.label() : nodeClass.getSimpleName());
            List<BoxIODefinition> nodeInputDefinitionList = new ArrayList<>();
            List<BoxIODefinition> nodeOutputDefinitionList = new ArrayList<>();
            List<BoxIODefinition> nodeConfigurationDefinitionList = new ArrayList<>();
            try {
                Method callMethod = nodeClass.getMethod("call");
                NodeOutputBean nodeOutputBean = callMethod.getReturnType().getAnnotation(NodeOutputBean.class);
                if (nodeOutputBean != null) {
                    for (Method outputMethod : callMethod.getReturnType().getMethods()){
                        String outputName = getVarName(outputMethod);
                        NodeOutput nodeOutput = outputMethod.getAnnotation(NodeOutput.class);
                        if (nodeOutput != null) {
                            if (!outputMethod.isAccessible()) {
                                throw new IllegalNodeConfigurationException("NodeOutput method must be public");
                            }
                            if (!outputMethod.getReturnType().isAssignableFrom(Exchange.class)) {
                                throw new IllegalNodeConfigurationException("NodeOutput method return type must be Exchange");
                            }
                            BoxIODefinition boxIODefinition = new BoxIODefinition();
                            boxIODefinition.setType((Class) ((ParameterizedType)outputMethod.getGenericReturnType()).getActualTypeArguments()[0]);
                            boxIODefinition.setMethod(outputMethod);
                            boxIODefinition.setName(outputName);
                            boxIODefinition.setLabel(!nodeOutput.label().isEmpty() ? nodeOutput.label() : outputName);
                            boxIODefinition.setOrder(nodeOutput.order());
                            nodeOutputDefinitionList.add(boxIODefinition);
                        }
                    }
                }else if (!callMethod.getReturnType().equals(Void.class)){
                    BoxIODefinition boxIODefinition = new BoxIODefinition();
                    boxIODefinition.setType((Class) ((ParameterizedType) callMethod.getGenericReturnType()).getActualTypeArguments()[0]);
                    boxIODefinition.setName("main");
                    boxIODefinition.setLabel("main");
                    boxIODefinition.setOrder(0);
                    nodeOutputDefinitionList.add(boxIODefinition);
                }

            } catch (NoSuchMethodException e) {
                throw new IllegalNodeConfigurationException("AnnotatedNode class must have a call() method with no parameter");
            }
            for (Method method : nodeClass.getMethods()) {
                NodeInput nodeInput = method.getAnnotation(NodeInput.class);
                String varName = getVarName(method);
                if (nodeInput != null) {
                    if (method.getParameterCount() == 1) {
                        inputMethods.put(varName, method);
                        BoxIODefinition boxIODefinition = new BoxIODefinition();
                        boxIODefinition.setType(method.getParameters()[0].getType());
                        boxIODefinition.setName(varName);
                        boxIODefinition.setLabel(!nodeInput.label().isEmpty() ? nodeInput.label() : getVarName(method));
                        boxIODefinition.setOrder(nodeInput.order());
                        nodeInputDefinitionList.add(boxIODefinition);
                    } else {
                        throw new IllegalNodeConfigurationException("Input setter method must have exactly one parameter");
                    }
                }
                NodeConfig nodeConfig = method.getAnnotation(NodeConfig.class);
                if (nodeConfig != null) {
                    if (method.getParameterCount() == 1) {
                        configMethods.put(varName, method);
                        BoxIODefinition nodeConfigurationDefinition = new BoxConfigurationDefinition();
                        nodeConfigurationDefinition.setType(method.getParameters()[0].getType());
                        nodeConfigurationDefinition.setName(varName);
                        nodeConfigurationDefinition.setLabel(!nodeConfig.label().isEmpty() ? nodeConfig.label() : getVarName(method));
                        nodeConfigurationDefinition.setOrder(nodeConfig.order());
                        nodeConfigurationDefinitionList.add(nodeConfigurationDefinition);
                    } else {
                        throw new IllegalNodeConfigurationException("Config setter method must have exactly one parameter");
                    }
                }
            }
            Collections.sort(nodeInputDefinitionList, BoxIODDefComparator.INSTANCE);
            Collections.sort(nodeOutputDefinitionList, BoxIODDefComparator.INSTANCE);
            Collections.sort(nodeConfigurationDefinitionList, BoxIODDefComparator.INSTANCE);
            boxDefinition.setInputs(nodeInputDefinitionList);
            boxDefinition.setOutputs(nodeOutputDefinitionList);
            boxDefinition.setConfigurations(nodeConfigurationDefinitionList);
            return new BoxInfo(nodeClass, boxDefinition, inputMethods, configMethods);
        }
        throw new IllegalNodeConfigurationException("AnnotatedNode type must have @NodeEntry annotation");
    }

    public static String getVarName(Method method){
        String methodName = method.getName();
        return Introspector.decapitalize(methodName.substring(methodName.startsWith("is") ? 2 : 3));
    }
}
