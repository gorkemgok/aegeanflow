package com.aegeanflow.core.example;

import com.aegeanflow.core.CoreModule;
import com.aegeanflow.core.engine.DataFlowEngine;
import com.aegeanflow.core.engine.DataFlowEngineFactory;
import com.aegeanflow.core.engine.FlowFuture;
import com.aegeanflow.core.exception.NoSuchNodeException;
import com.aegeanflow.core.exception.NodeRuntimeException;
import com.aegeanflow.core.flow.Flow;
import com.aegeanflow.core.flow.FlowConnection;
import com.aegeanflow.core.flow.FlowNode;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by gorkem on 12.01.2018.
 */
public class MainFlow2 {
    public static void main(String[] args) {
        Flow flow = new Flow();

        FlowNode stringSourceNode = new FlowNode();
        stringSourceNode.setUuid(UUID.randomUUID());
        stringSourceNode.setNodeClass(StringSourceNode.class.getTypeName());
        Map<String, Object> stringSourceNodeConfig = new HashMap<>();
        stringSourceNodeConfig.put("prefix", "Test1");
        stringSourceNode.setConfiguration(stringSourceNodeConfig);

        FlowNode stringSuffixNode = new FlowNode();
        stringSuffixNode.setUuid(UUID.randomUUID());
        stringSuffixNode.setNodeClass(StringSuffixNode.class.getTypeName());
        Map<String, Object> stringSuffixNodeConfig = new HashMap<>();
        stringSuffixNodeConfig.put("suffix", "Test2");
        stringSuffixNode.setConfiguration(stringSuffixNodeConfig);

        FlowConnection flowConnection = new FlowConnection();
        flowConnection.setFromUUID(stringSourceNode.getUUID());
        flowConnection.setToUUID(stringSuffixNode.getUUID());
        flowConnection.setToInput("input");

        flow.setNodeList(Arrays.asList(stringSourceNode, stringSuffixNode));
        flow.setConnectionList(Arrays.asList(flowConnection));

        Injector injector = Guice.createInjector(new CoreModule());
        DataFlowEngineFactory dataFlowEngineFactory = injector.getInstance(DataFlowEngineFactory.class);
        try {
            DataFlowEngine dataFlowEngine = dataFlowEngineFactory.create(flow);
            FlowFuture future = dataFlowEngine.getResult(stringSuffixNode.getUUID());
            System.out.println(future.get());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NodeRuntimeException e) {
            e.printStackTrace();
        } catch (NoSuchNodeException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
