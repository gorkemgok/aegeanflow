package com.aegeanflow.core.definition;

import com.aegeanflow.core.CompiledNodeInfo;
import com.aegeanflow.core.Node;
import com.aegeanflow.core.spi.annotation.NodeConfig;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;
import com.aegeanflow.core.exception.IllegalNodeConfigurationException;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompilerUtil {

    public static CompiledNodeInfo compile(Class<? extends Node> nodeClass) throws IllegalNodeConfigurationException {
        Map<String, Method> inputMethods = new HashMap<>();
        Map<String, Method> configMethods = new HashMap<>();
        NodeEntry nodeEntry = nodeClass.getAnnotation(NodeEntry.class);
        if (nodeEntry != null) {
            NodeDefinition nodeDefinition = new NodeDefinition();
            nodeDefinition.setType(nodeClass);
            nodeDefinition.setLabel(!nodeEntry.label().isEmpty() ? nodeEntry.label() : nodeClass.getSimpleName());
            List<NodeInputDefinition> nodeInputDefinitionList = new ArrayList<>();
            List<NodeConfigurationDefinition> nodeConfigurationDefinitionList = new ArrayList<>();
            for (Method method : nodeClass.getMethods()) {
                if (method.getName().equals("call") && method.getParameterCount() == 0){
                    nodeDefinition.setReturnType(method.getReturnType());
                    break;
                }
                NodeInput nodeInput = method.getAnnotation(NodeInput.class);
                String varName = getVarName(method);
                if (nodeInput != null) {
                    if (method.getParameterCount() == 1) {
                        inputMethods.put(varName, method);
                        NodeInputDefinition nodeInputDefinition = new NodeInputDefinition();
                        nodeInputDefinition.setType(method.getParameters()[0].getType());
                        nodeInputDefinition.setName(varName);
                        nodeInputDefinition.setLabel(!nodeInput.label().isEmpty() ? nodeInput.label() : getVarName(method));
                        nodeInputDefinitionList.add(nodeInputDefinition);
                        break;
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
                        nodeConfigurationDefinitionList.add(nodeConfigurationDefinition);
                        break;
                    } else {
                        throw new IllegalNodeConfigurationException("Config setter method must have exactly one parameter");
                    }
                }
            }
            nodeDefinition.setInputs(nodeInputDefinitionList);
            nodeDefinition.setConfigurations(nodeConfigurationDefinitionList);
            return new CompiledNodeInfo(nodeClass, nodeDefinition, inputMethods, configMethods);
        }
        throw new IllegalNodeConfigurationException("Node type must have @NodeEntry annotation");
    }

    private static String getVarName(Method method){
        String methodName = method.getName();
        return Introspector.decapitalize(methodName.substring(methodName.startsWith("is") ? 2 : 3));
    }
}
