package com.aegeanflow.core.table;

import com.aegeanflow.core.ioc.CoreModule;
import com.aegeanflow.core.resource.*;
import com.aegeanflow.core.route.RouteOptions;
import com.aegeanflow.core.route.Router;
import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.session.SessionFactory;
import com.aegeanflow.core.spi.node.Node;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.UUID;

@Guice(modules = CoreModule.class)
public class TableFlowTest {

    @Inject
    SessionFactory sessionFactory;

    @Inject
    Injector injector;

    @Test
    public void test() {
        Session session1 = sessionFactory.create();
        Session session2 = sessionFactory.create();
        Assert.assertNotEquals(session1.getUuid(), session2.getUuid());

        Node randomTableNode = session1.newNode(UUID.randomUUID(), RandomTableGeneratorNode.class);
        Node tableStdOutNode = session1.newNode(UUID.randomUUID(), TableStdOutNode.class);
        session1.getRouter().connect(
            randomTableNode, RandomTableGeneratorNode.MAIN_OUTPUT, tableStdOutNode, TableStdOutNode.TABLE_INPUT,
            new RouteOptions());
//        session1.run();
//        session1.awaitCompletion();

        Router router = session2.getRouter();
        Node connection = session2.newNode(UUID.randomUUID(), DatabaseConnectionNode.class);
        Node reader = session2.newNode(UUID.randomUUID(), TableReaderNode.class);
        Node filter = session2.newNode(UUID.randomUUID(), TableFilterNode.class);
        Node groupBy = session2.newNode(UUID.randomUUID(), TableGroupByNode.class);
        Node printer = session2.newNode(UUID.randomUUID(), TableStdOutNode.class);
        router.connect(connection, DatabaseConnectionNode.CONNECTION, reader, TableReaderNode.CONNECTION, new RouteOptions());
        router.connect(reader, TableReaderNode.TABLE, filter, TableFilterNode.INPUT_TABLE, new RouteOptions());
        router.connect(filter, TableFilterNode.OUTPUT_TABLE, groupBy, TableGroupByNode.INPUT_TABLE, new RouteOptions());
        router.connect(groupBy, TableGroupByNode.OUTPUT_TABLE, printer, TableStdOutNode.TABLE_INPUT, new RouteOptions());
        session2.setInput(connection.getUUID(), DatabaseConnectionNode.DRIVER_CLASS, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        session2.setInput(connection.getUUID(), DatabaseConnectionNode.JDBC_URL, "jdbc:sqlserver://195.214.147.187:2533;loginTimeout=30;");
        session2.setInput(connection.getUUID(), DatabaseConnectionNode.USERNAME, "13TE3YEx");
        session2.setInput(connection.getUUID(), DatabaseConnectionNode.PASSWORD, "5d14Bh06xff0%Pw");
        session2.setInput(reader.getUUID(), TableReaderNode.QUERY, "exec INTEGRATION.dbo.COREBI_CEGID_COGS_NEW 2017, 2020");

        session2.run();
        session2.awaitCompletion();
    }
}
