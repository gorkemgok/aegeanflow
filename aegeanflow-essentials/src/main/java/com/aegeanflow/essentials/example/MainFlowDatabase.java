package com.aegeanflow.essentials.example;

import com.aegeanflow.core.*;
import com.aegeanflow.core.engine.DataFlowEngine;
import com.aegeanflow.core.engine.DataFlowEngineManager;
import com.aegeanflow.core.exception.NoSuchNodeException;
import com.aegeanflow.core.exception.NodeRuntimeException;
import com.aegeanflow.core.flow.*;
import com.aegeanflow.essentials.node.DatabaseConnectionBox;
import com.aegeanflow.essentials.node.DatabaseReaderBox;

/**
 * Created by gorkem on 12.01.2018.
 */
public class MainFlowDatabase {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchNodeException, NodeRuntimeException {
        Flow flow = new FlowBuilder()
                .start(new FlowNodeBuilder(DatabaseConnectionBox.class)
                        .addConfig("jdbcClass", "com.mysql.jdbc.Driver")
                        .addConfig("jdbcUrl", "jdbc:mysql://localhost:3306/corebi_application")
                        .addConfig("user", "root")
                        .addConfig("password", "root")
                        .build())
                .to("connection", new FlowNodeBuilder(DatabaseReaderBox.class)
                        .addConfig("query", "SELECT * FROM subject")
                        .build()).build();

        AegeanFlow aegeanFlow = AegeanFlow.start();
        DataFlowEngineManager factory = aegeanFlow.createEngineFactory();
        DataFlowEngine engine = factory.create(flow, true);
        engine.getResultList();
    }
}
