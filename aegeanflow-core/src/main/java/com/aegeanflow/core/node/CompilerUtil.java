package com.aegeanflow.core.node;

import com.aegeanflow.core.definition.NodeConfigurationDefinition;
import com.aegeanflow.core.definition.NodeDefinition;
import com.aegeanflow.core.definition.NodeIODDefComparator;
import com.aegeanflow.core.definition.NodeIODefinition;
import com.aegeanflow.core.spi.RunnableNode;
import com.aegeanflow.core.spi.annotation.*;
import com.aegeanflow.core.exception.IllegalNodeConfigurationException;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.*;

public class CompilerUtil {

    public static NodeInfo compile(Class<? extends RunnableNode> nodeClass) throws IllegalNodeConfigurationException {
        Map<String, Method> inputMethods = new HashMap<>();
        Map<String, Method> configMethods = new HashMap<>();
        NodeEntry nodeEntry = nodeClass.getAnnotation(NodeEntry.class);
        if (nodeEntry != null) {
            NodeDefinition nodeDefinition = new NodeDefinition();
            nodeDefinition.setType(nodeClass);
            nodeDefinition.setLabel(!nodeEntry.label().isEmpty() ? nodeEntry.label() : nodeClass.getSimpleName());
            List<NodeIODefinition> nodeInputDefinitionList = new ArrayList<>();
            List<NodeIODefinition> nodeOutputDefinitionList = new ArrayList<>();
            List<NodeConfigurationDefinition> nodeConfigurationDefinitionList = new ArrayList<>();
            try {
                Method callMethod = nodeClass.getMethod("call");
                NodeOutputBean nodeOutputBean = callMethod.getReturnType().getAnnotation(NodeOutputBean.class);
                if (nodeOutputBean != null) {
                    for (Method outputMethod : callMethod.getReturnType().getMethods()){
                        String outputName = getVarName(outputMethod);
                        NodeOutput nodeOutput = outputMethod.getAnnotation(NodeOutput.class);
                        if (nodeOutput != null) {
                            NodeIODefinition nodeIODefinition = new NodeIODefinition();
                            nodeIODefinition.setType(outputMethod.getReturnType());
                            nodeIODefinition.setName(outputName);
                            nodeIODefinition.setLabel(!nodeOutput.label().isEmpty() ? nodeOutput.label() : outputName);
                            nodeIODefinition.setOrder(nodeOutput.order());
                            nodeOutputDefinitionList.add(nodeIODefinition);
                        }
                    }
                }else if (!callMethod.getReturnType().equals(Void.class)){
                    NodeIODefinition nodeIODefinition = new NodeIODefinition();
                    nodeIODefinition.setType(callMethod.getReturnType());
                    nodeIODefinition.setName("main");
                    nodeIODefinition.setLabel("main");
                    nodeIODefinition.setOrder(0);
                    nodeOutputDefinitionList.add(nodeIODefinition);
                }

            } catch (NoSuchMethodException e) {
                throw new IllegalNodeConfigurationException("RunnableNode class must have a call() method with no parameter");
            }
            for (Method method : nodeClass.getMethods()) {
                NodeInput nodeInput = method.getAnnotation(NodeInput.class);
                String varName = getVarName(method);
                if (nodeInput != null) {
                    if (method.getParameterCount() == 1) {
                        inputMethods.put(varName, method);
                        NodeIODefinition nodeIODefinition = new NodeIODefinition();
                        nodeIODefinition.setType(method.getParameters()[0].getType());
                        nodeIODefinition.setName(varName);
                        nodeIODefinition.setLabel(!nodeInput.label().isEmpty() ? nodeInput.label() : getVarName(method));
                        nodeIODefinition.setOrder(nodeInput.order());
                        nodeInputDefinitionList.add(nodeIODefinition);
                        continue;
                    } else {
                        throw new IllegalNodeConfigurationException("Input setter method must have exactly one parameter");
                    }
                }
                NodeConfig nodeConfig = method.getAnnotation(NodeConfig.class);
                if (nodeConfig != null) {
                    if (method.getParameterCount() == 1) {
                        configMethods.put(varName, method);
                        NodeConfigurationDefinition nodeConfigurationDefinition = new NodeConfigurationDefinition();
                        nodeConfigurationDefinition.setType(method.getParameters()[0].getType());
                        nodeConfigurationDefinition.setName(varName);
                        nodeConfigurationDefinition.setLabel(!nodeConfig.label().isEmpty() ? nodeConfig.label() : getVarName(method));
                        nodeConfigurationDefinition.setOrder(nodeConfig.order());
                        nodeConfigurationDefinitionList.add(nodeConfigurationDefinition);
                        continue;
                    } else {
                        throw new IllegalNodeConfigurationException("Config setter method must have exactly one parameter");
                    }
                }
            }
            Collections.sort(nodeInputDefinitionList, NodeIODDefComparator.INSTANCE);
            Collections.sort(nodeOutputDefinitionList, NodeIODDefComparator.INSTANCE);
            Collections.sort(nodeConfigurationDefinitionList, NodeIODDefComparator.INSTANCE);
            nodeDefinition.setInputs(nodeInputDefinitionList);
            nodeDefinition.setOutputs(nodeOutputDefinitionList);
            nodeDefinition.setConfigurations(nodeConfigurationDefinitionList);
            return new NodeInfo(nodeClass, nodeDefinition, inputMethods, configMethods);
        }
        throw new IllegalNodeConfigurationException("RunnableNode type must have @NodeEntry annotation");
    }

    public static String getVarName(Method method){
        String methodName = method.getName();
        return Introspector.decapitalize(methodName.substring(methodName.startsWith("is") ? 2 : 3));
    }
}
