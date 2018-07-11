package com.aegeanflow.essentials.example;

import com.aegeanflow.core.flow.Flow;
import com.aegeanflow.core.flow.FlowConnection;
import com.aegeanflow.core.flow.FlowNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
public class MainFlow {
    public static void main(String[] args) throws JsonProcessingException {
        FlowNode node1 = new FlowNode();
        node1.setUUID(UUID.randomUUID());
        node1.setNodeClass(TestNode.class);
        Map<String, Object> config = new HashMap<>();
        config.put("testC", "testConfig");
        node1.setConfiguration(config);

        FlowNode node2 = new FlowNode();
        node2.setUUID(UUID.randomUUID());
        node2.setNodeClass(TestNode.class);
        node2.setConfiguration(config);

        FlowConnection connection1 = new FlowConnection();
        connection1.setFromUUID(node1.getUUID());
        connection1.setToUUID(node2.getUUID());
        connection1.setInputName("inputTestFlow");

        Flow flow = new Flow();
        flow.setNodeList(Arrays.asList(node1, node2));
        flow.setConnectionList(Arrays.asList(connection1));

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(flow));
    }
}
