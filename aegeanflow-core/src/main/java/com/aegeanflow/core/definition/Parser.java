package com.aegeanflow.core.definition;

import com.aegeanflow.core.Node;
import com.aegeanflow.core.annotation.NodeConfig;
import com.aegeanflow.core.annotation.NodeEntry;
import com.aegeanflow.core.annotation.NodeInput;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    public String parse(Class<? extends Node> nodeClass) throws Exception {
        NodeEntry nodeEntry = nodeClass.getAnnotation(NodeEntry.class);
        if (nodeEntry != null) {
            NodeDefinition nodeDefinition = new NodeDefinition();
            nodeDefinition.setType(nodeClass);
            nodeDefinition.setLabel(!nodeEntry.label().isEmpty() ? nodeEntry.label() : nodeClass.getSimpleName());
            List<NodeInputDefinition> nodeInputDefinitionList = new ArrayList<>();
            List<NodeConfigurationDefinition> nodeConfigurationDefinitionList = new ArrayList<>();
            for (Method method : nodeClass.getMethods()) {
                NodeInput nodeInput = method.getAnnotation(NodeInput.class);
                if (nodeInput != null) {
                    if (method.getParameterCount() == 1) {
                        NodeInputDefinition nodeInputDefinition = new NodeInputDefinition();
                        nodeInputDefinition.setType(method.getParameters()[0].getType());
                        nodeInputDefinition.setName(method.getName());
                        nodeInputDefinition.setLabel(!nodeInput.label().isEmpty() ? nodeInput.label() : method.getParameters()[0].getName());
                        nodeInputDefinitionList.add(nodeInputDefinition);
                    } else {
                        throw new Exception("Input setter method must have exactly one parameter");
                    }
                }
                NodeConfig nodeConfig = method.getAnnotation(NodeConfig.class);
                if (nodeConfig != null) {
                    if (method.getParameterCount() == 1) {
                        NodeConfigurationDefinition nodeConfigurationDefinition = new NodeConfigurationDefinition();
                        nodeConfigurationDefinition.setType(method.getParameters()[0].getType());
                        nodeConfigurationDefinition.setName(method.getName());
                        nodeConfigurationDefinition.setLabel(!nodeConfig.label().isEmpty() ? nodeConfig.label() : method.getParameters()[0].getName());
                        nodeConfigurationDefinitionList.add(nodeConfigurationDefinition);
                    } else {
                        throw new Exception("Config setter method must have exactly one parameter");
                    }
                }
                nodeDefinition.setInputs(nodeInputDefinitionList);
                nodeDefinition.setConfigurations(nodeConfigurationDefinitionList);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(nodeDefinition);
        }
        return "{}";
    }
}
