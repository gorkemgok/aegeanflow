package com.aegeanflow.essentials.table;

import com.aegeanflow.core.ioc.CoreModule;
import com.aegeanflow.essentials.database.DatabaseReaderNode;
import com.aegeanflow.essentials.resource.*;
import com.aegeanflow.core.route.RouteOptions;
import com.aegeanflow.core.route.Router;
import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.spi.node.Node;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.UUID;

@Guice(modules = CoreModule.class)
public class TableFlowTest1 {

    @Inject
    Provider<Session> sessionFactory;

    @Inject
    Injector injector;

    @Test
    public void test() {
        Session session1 = sessionFactory.get();
        Session session2 = sessionFactory.get();
        Assert.assertNotEquals(session1.getId(), session2.getId());

        Node randomTableNode = session1.newNode(UUID.randomUUID(), RandomTableGeneratorNode.class);
        Node tableStdOutNode = session1.newNode(UUID.randomUUID(), TableStdOutNode.class);
        session1.getRouter().connect(
            randomTableNode, RandomTableGeneratorNode.MAIN_OUTPUT, tableStdOutNode, TableStdOutNode.TABLE_INPUT,
            new RouteOptions());
//        session1.run();
//        session1.awaitCompletion();

        Router router = session2.getRouter();
        Node connection = session2.newNode(UUID.randomUUID(), DatabaseConnectionNode.class);
        Node reader = session2.newNode(UUID.randomUUID(), DatabaseReaderNode.class);
        Node filter = session2.newNode(UUID.randomUUID(), TableFilterNode.class);
        Node groupBy = session2.newNode(UUID.randomUUID(), TableGroupByNode.class);
        Node printer = session2.newNode(UUID.randomUUID(), TableStdOutNode.class);
        router.connect(connection, DatabaseConnectionNode.CONNECTION, reader, DatabaseReaderNode.CONNECTION, new RouteOptions());
        router.connect(reader, DatabaseReaderNode.TABLE, filter, TableFilterNode.INPUT_TABLE, new RouteOptions());
        router.connect(filter, TableFilterNode.OUTPUT_TABLE, groupBy, TableGroupByNode.INPUT_TABLE, new RouteOptions());
        router.connect(groupBy, TableGroupByNode.OUTPUT_TABLE, printer, TableStdOutNode.TABLE_INPUT, new RouteOptions());
        session2.setInput(connection.getId(), DatabaseConnectionNode.DRIVER_CLASS, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        session2.setInput(connection.getId(), DatabaseConnectionNode.JDBC_URL, "jdbc:sqlserver://195.214.147.187:2533;loginTimeout=30;");
        session2.setInput(connection.getId(), DatabaseConnectionNode.USERNAME, "13TE3YEx");
        session2.setInput(connection.getId(), DatabaseConnectionNode.PASSWORD, "5d14Bh06xff0%Pw");
        session2.setInput(reader.getId(), DatabaseReaderNode.QUERY, "exec INTEGRATION.dbo.COREBI_CEGID_COGS_NEW 2017, 2020");

        session2.run();
        session2.awaitCompletion();
    }
}
