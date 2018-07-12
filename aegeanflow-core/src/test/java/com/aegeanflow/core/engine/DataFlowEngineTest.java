package com.aegeanflow.core.engine;

import com.aegeanflow.core.NodeRepository;
import com.aegeanflow.core.flow.Flow;
import com.aegeanflow.core.flow.FlowConnection;
import com.aegeanflow.core.flow.FlowNode;
import com.aegeanflow.core.node.flowtest.Box1;
import com.aegeanflow.core.node.flowtest.Box2;
import com.aegeanflow.core.node.flowtest.Box3;
import com.aegeanflow.core.spi.AnnotatedBox;
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
    List<AnnotatedBox<?>> annotatedBoxList = new ArrayList<>();
    List<FlowConnection> flowConnectionList = new ArrayList<>();
    NodeRepository nodeRepository;

    @BeforeTest
    public void setup(){
        Set<Class<? extends AnnotatedBox>> nodeClasses = ImmutableSet.of(Box1.class, Box2.class, Box3.class);
        nodeRepository = new NodeRepository(nodeClasses);
        nodeRepository.init();

        flow = new Flow();
        flow.setUuid(UUID.randomUUID());
        flow.setNodeList(flowNodeList);
        flow.setConnectionList(flowConnectionList);

        FlowNode flowNode1 = new FlowNode();
        flowNode1.setNodeClass(Box1.class);
        flowNode1.setUUID(UUID.randomUUID());
        flowNode1.setConfiguration(ImmutableMap.of("seedText", "seed text annotatedBox 1"));
        Box1 node1 = new Box1();
        node1.setUUID(flowNode1.getUUID());

        FlowNode flowNode2 = new FlowNode();
        flowNode2.setNodeClass(Box2.class);
        flowNode2.setUUID(UUID.randomUUID());
        flowNode2.setConfiguration(ImmutableMap.of("text", "text annotatedBox 2", "length", 3));
        Box2 node2 = new Box2();
        node2.setUUID(flowNode2.getUUID());

        FlowNode flowNode3 = new FlowNode();
        flowNode3.setNodeClass(Box3.class);
        flowNode3.setUUID(UUID.randomUUID());
        flowNode3.setConfiguration(ImmutableMap.of("length", 2));
        Box3 node3 = new Box3();
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

        annotatedBoxList.add(node1);
        annotatedBoxList.add(node2);
        annotatedBoxList.add(node3);

        flowConnectionList.add(flowConnection1_3);
        flowConnectionList.add(flowConnection21_3);
        flowConnectionList.add(flowConnection22_3);
    }

    @Test
    public void testGetIOPairList() throws Exception {
        DataFlowEngine dfe = new DataFlowEngine(flow, annotatedBoxList,  nodeRepository, null);
        List<DataFlowEngine.IOPair> ioPairs = dfe.getIOPairList(annotatedBoxList.get(2).getUUID());
        assertEquals(ioPairs.size(), 3);

        FlowFuture future = dfe.getResult(annotatedBoxList.get(2).getUUID());
        Object object = future.get();
        object.toString();
    }

}