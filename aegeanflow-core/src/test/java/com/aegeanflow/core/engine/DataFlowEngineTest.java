package com.aegeanflow.core.engine;

import com.aegeanflow.core.BoxRepository;
import com.aegeanflow.core.proxy.SessionProxy;
import com.aegeanflow.core.proxy.RouteProxy;
import com.aegeanflow.core.proxy.NodeProxy;
import com.aegeanflow.core.node.flowtest.Box1;
import com.aegeanflow.core.node.flowtest.Box2;
import com.aegeanflow.core.node.flowtest.Box3;
import com.aegeanflow.core.spi.AnnotatedBox;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

public class DataFlowEngineTest {

    SessionProxy sessionProxy;
    List<NodeProxy> nodeProxyList = new ArrayList<>();
    List<AnnotatedBox<?>> annotatedBoxList = new ArrayList<>();
    List<RouteProxy> routeProxyList = new ArrayList<>();
    BoxRepository boxRepository;

    @BeforeTest
//    public void setup(){
//        Set<Class<? extends AnnotatedBox>> nodeClasses = ImmutableSet.of(Box1.class, Box2.class, Box3.class);
//        boxRepository = new BoxRepository(Collections.emptySet());
//        boxRepository.init();
//
//        sessionProxy = new SessionProxy();
//        sessionProxy.setUuid(UUID.randomUUID());
//        sessionProxy.setNodeList(nodeProxyList);
//        sessionProxy.setConnectionList(routeProxyList);
//
//        NodeProxy nodeProxy1 = new NodeProxy();
//        nodeProxy1.setType(Box1.class);
//        nodeProxy1.setUUID(UUID.randomUUID());
//        nodeProxy1.setConfiguration(ImmutableMap.of("seedText", "seed text annotatedBox 1"));
//        Box1 node1 = new Box1();
//        node1.setUUID(nodeProxy1.getUUID());
//
//        NodeProxy nodeProxy2 = new NodeProxy();
//        nodeProxy2.setType(Box2.class);
//        nodeProxy2.setUUID(UUID.randomUUID());
//        nodeProxy2.setConfiguration(ImmutableMap.of("text", "text annotatedBox 2", "length", 3));
//        Box2 node2 = new Box2();
//        node2.setUUID(nodeProxy2.getUUID());
//
//        NodeProxy nodeProxy3 = new NodeProxy();
//        nodeProxy3.setType(Box3.class);
//        nodeProxy3.setUUID(UUID.randomUUID());
//        nodeProxy3.setConfiguration(ImmutableMap.of("length", 2));
//        Box3 node3 = new Box3();
//        node3.setUUID(nodeProxy3.getUUID());
//
//        RouteProxy routeProxy1_3 = new RouteProxy();
//        routeProxy1_3.setUuid(UUID.randomUUID());
//        routeProxy1_3.setType(RouteProxy.Type.FLOW);
//        routeProxy1_3.setSource(nodeProxy1.getUUID());
//        routeProxy1_3.setOutput("seedText");
//        routeProxy1_3.setTarget(nodeProxy3.getUUID());
//        routeProxy1_3.setInputName("seedText");
//
//        RouteProxy routeProxy21_3 = new RouteProxy();
//        routeProxy21_3.setUuid(UUID.randomUUID());
//        routeProxy21_3.setType(RouteProxy.Type.FLOW);
//        routeProxy21_3.setSource(nodeProxy2.getUUID());
//        routeProxy21_3.setOutput("text");
//        routeProxy21_3.setTarget(nodeProxy3.getUUID());
//        routeProxy21_3.setInputName("repeatedText");
//
//        RouteProxy routeProxy22_3 = new RouteProxy();
//        routeProxy22_3.setUuid(UUID.randomUUID());
//        routeProxy22_3.setType(RouteProxy.Type.FLOW);
//        routeProxy22_3.setSource(nodeProxy2.getUUID());
//        routeProxy22_3.setOutput("count");
//        routeProxy22_3.setTarget(nodeProxy3.getUUID());
//        routeProxy22_3.setInputName("repeatCount");
//
//        nodeProxyList.add(nodeProxy1);
//        nodeProxyList.add(nodeProxy2);
//        nodeProxyList.add(nodeProxy3);
//
//        annotatedBoxList.add(node1);
//        annotatedBoxList.add(node2);
//        annotatedBoxList.add(node3);
//
//        routeProxyList.add(routeProxy1_3);
//        routeProxyList.add(routeProxy21_3);
//        routeProxyList.add(routeProxy22_3);
//    }

    @Test
    public void testGetIOPairList() throws Exception {
        DataFlowEngine dfe = new DataFlowEngine(sessionProxy, annotatedBoxList, boxRepository, null);
        List<DataFlowEngine.IOPair> ioPairs = dfe.getIOPairList(annotatedBoxList.get(2).getUUID());
        assertEquals(ioPairs.size(), 3);

        FlowFuture future = dfe.getResult(annotatedBoxList.get(2).getUUID());
        Object object = future.get();
        object.toString();
    }

}