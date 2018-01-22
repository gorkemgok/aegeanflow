package com.aegeanflow.core.example;

import com.aegeanflow.core.*;
import com.aegeanflow.core.engine.DataFlowEngine;
import com.aegeanflow.core.engine.DataFlowEngineFactory;
import com.aegeanflow.core.exception.NoSuchNodeException;
import com.aegeanflow.core.exception.NodeRuntimeException;
import com.aegeanflow.core.flow.*;
import com.aegeanflow.core.node.DatabaseConnectionNode;
import com.aegeanflow.core.node.DatabaseReaderNode;
/**
 * Created by gorkem on 12.01.2018.
 */
public class MainFlowDatabase {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchNodeException, NodeRuntimeException {
        Flow flow = new FlowBuilder()
                .start(new FlowNodeBuilder(DatabaseConnectionNode.class)
                        .addConfig("jdbcClass", "com.mysql.jdbc.Driver")
                        .addConfig("jdbcUrl", "jdbc:mysql://localhost:3306/corebi_application")
                        .addConfig("user", "root")
                        .addConfig("password", "root")
                        .build())
                .to("connection", new FlowNodeBuilder(DatabaseReaderNode.class)
                        .addConfig("query", "SELECT * FROM subject")
                        .build()).build();

        AegeanFlow aegeanFlow = AegeanFlow.start();
        DataFlowEngineFactory factory = aegeanFlow.createEngineFactory();
        DataFlowEngine engine = factory.create(flow);
        engine.getResult();
    }
}
