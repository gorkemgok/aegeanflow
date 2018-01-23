package com.aegeanflow.core.flow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkem on 12.01.2018.
 */
public class FlowBuilder {

    private final List<FlowNode> flowNodeList;

    private final List<FlowConnection> flowConnectionList;

    private FlowNode lastNode;

    public FlowBuilder() {
        flowNodeList = new ArrayList<>();
        flowConnectionList = new ArrayList<>();
    }

    public FlowBuilder start(FlowNode flowNode){
        flowNodeList.add(flowNode);
        lastNode = flowNode;
        return this;
    }

    public FlowBuilder to(String inputName, FlowNode flowNode){
        flowNodeList.add(flowNode);
        FlowConnection flowConnection = new FlowConnection();
        flowConnection.setFromUUID(lastNode.getUUID());
        flowConnection.setToUUID(flowNode.getUUID());
        flowConnection.setInputName(inputName);
        flowConnectionList.add(flowConnection);
        lastNode = flowNode;
        return this;
    }

    public Flow build() {
        Flow flow = new Flow();
        flow.setNodeList(flowNodeList);
        flow.setConnectionList(flowConnectionList);
        return flow;
    }
}
