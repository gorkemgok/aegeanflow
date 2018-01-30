package com.aegeanflow.core.engine;

import com.aegeanflow.core.flow.Flow;
import com.aegeanflow.core.flow.FlowConnection;
import com.aegeanflow.core.flow.FlowNode;
import com.aegeanflow.core.node.NodeRepository;
import com.aegeanflow.core.node.flowtest.Node1;
import com.aegeanflow.core.node.flowtest.Node2;
import com.aegeanflow.core.node.flowtest.Node3;
import com.aegeanflow.core.spi.RunnableNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.testng.Assert.*;

public class DataFlowEngineTest {

    Flow flow;
    List<FlowNode> flowNodeList = new ArrayList<>();
    List<RunnableNode<?>> runnableNodeList = new ArrayList<>();
    List<FlowConnection> flowConnectionList = new ArrayList<>();
    NodeRepository nodeRepository;

    @BeforeTest
    public void setup(){
        Set<Class<? extends RunnableNode>> nodeClasses = ImmutableSet.of(Node1.class, Node2.class, Node3.class);
        nodeRepository = new NodeRepository(nodeClasses);
        nodeRepository.init();

        flow = new Flow();
        flow.setUuid(UUID.randomUUID());
        flow.setNodeList(flowNodeList);
        flow.setConnectionList(flowConnectionList);

        FlowNode flowNode1 = new FlowNode();
        flowNode1.setNodeClass(Node1.class);
        flowNode1.setUUID(UUID.randomUUID());
        flowNode1.setConfiguration(ImmutableMap.of("seedText", "seed text runnableNode 1"));
        Node1 node1 = new Node1();
        node1.setUUID(flowNode1.getUUID());

        FlowNode flowNode2 = new FlowNode();
        flowNode2.setNodeClass(Node2.class);
        flowNode2.setUUID(UUID.randomUUID());
        flowNode2.setConfiguration(ImmutableMap.of("text", "text runnableNode 2", "length", 3));
        Node2 node2 = new Node2();
        node2.setUUID(flowNode2.getUUID());

        FlowNode flowNode3 = new FlowNode();
        flowNode3.setNodeClass(Node3.class);
        flowNode3.setUUID(UUID.randomUUID());
        flowNode3.setConfiguration(ImmutableMap.of("length", 2));
        Node3 node3 = new Node3();
        node3.setUUID(flowNode3.getUUID());

        FlowConnection flowConnection1_3 = new FlowConnection();
        flowConnection1_3.setUuid(UUID.randomUUID());
        flowConnection1_3.setType(FlowConnection.Type.FLOW);
        flowConnection1_3.setFromUUID(flowNode1.getUUID());
        flowConnection1_3.setOutputName("seedText");
        flowConnection1_3.setToUUID(flowNode3.getUUID());
        flowConnection1_3.setInputName("seedText");

        FlowConnection flowConnection21_3 = new FlowConnection();
        flowConnection21_3.setUuid(UUID.randomUUID());
        flowConnection21_3.setType(FlowConnection.Type.FLOW);
        flowConnection21_3.setFromUUID(flowNode2.getUUID());
        flowConnection21_3.setOutputName("text");
        flowConnection21_3.setToUUID(flowNode3.getUUID());
        flowConnection21_3.setInputName("repeatedText");

        FlowConnection flowConnection22_3 = new FlowConnection();
        flowConnection22_3.setUuid(UUID.randomUUID());
        flowConnection22_3.setType(FlowConnection.Type.FLOW);
        flowConnection22_3.setFromUUID(flowNode2.getUUID());
        flowConnection22_3.setOutputName("count");
        flowConnection22_3.setToUUID(flowNode3.getUUID());
        flowConnection22_3.setInputName("repeatCount");

        flowNodeList.add(flowNode1);
        flowNodeList.add(flowNode2);
        flowNodeList.add(flowNode3);

        runnableNodeList.add(node1);
        runnableNodeList.add(node2);
        runnableNodeList.add(node3);

        flowConnectionList.add(flowConnection1_3);
        flowConnectionList.add(flowConnection21_3);
        flowConnectionList.add(flowConnection22_3);
    }

    @Test
    public void testGetIOPairList() throws Exception {
        DataFlowEngine dfe = new DataFlowEngine(flow, runnableNodeList,  nodeRepository, null);
        List<DataFlowEngine.IOPair> ioPairs = dfe.getIOPairList(runnableNodeList.get(2).getUUID());
        assertEquals(ioPairs.size(), 3);

        FlowFuture future = dfe.getResult(runnableNodeList.get(2).getUUID());
        Object object = future.get();
        object.toString();
    }

}