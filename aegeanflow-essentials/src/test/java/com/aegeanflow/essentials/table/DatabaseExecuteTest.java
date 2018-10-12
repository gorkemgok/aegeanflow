package com.aegeanflow.essentials.table;

import com.aegeanflow.core.ioc.AegeanFlowCoreModule;
import com.aegeanflow.core.route.RouteOptions;
import com.aegeanflow.core.route.Router;
import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.spi.node.Node;
import com.aegeanflow.essentials.database.DatabaseExecuteNode;
import com.aegeanflow.essentials.database.DatabaseConnectionNode;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

@Guice(modules = AegeanFlowCoreModule.class)
public class DatabaseExecuteTest {

    @Inject
    Provider<Session> sessionFactory;

    @Inject
    Injector injector;

    @Test
    public void test() throws UnsupportedEncodingException {
        Session session = sessionFactory.get();

        Router router = session.getRouter();
        Node connectionNode = session.newNode(UUID.randomUUID(), DatabaseConnectionNode.class);
        Node dropNode = session.newNode(UUID.randomUUID(), DatabaseExecuteNode.class);
        Node dropTempNode = session.newNode(UUID.randomUUID(), DatabaseExecuteNode.class);
        Node createNode = session.newNode(UUID.randomUUID(), DatabaseExecuteNode.class);
        Node renameNode = session.newNode(UUID.randomUUID(), DatabaseExecuteNode.class);

        router.connect(connectionNode, DatabaseConnectionNode.CONNECTION, dropNode, DatabaseExecuteNode.CONNECTION, new RouteOptions());
        router.connect(connectionNode, DatabaseConnectionNode.CONNECTION, dropTempNode, DatabaseExecuteNode.CONNECTION, new RouteOptions());
        router.connect(connectionNode, DatabaseConnectionNode.CONNECTION, createNode, DatabaseExecuteNode.CONNECTION, new RouteOptions());
        router.connect(connectionNode, DatabaseConnectionNode.CONNECTION, renameNode, DatabaseExecuteNode.CONNECTION, new RouteOptions());

        router.precedence(createNode, dropNode);
        router.precedence(dropNode, renameNode);
        router.precedence(renameNode, dropTempNode);

        session.setInput(connectionNode.getId(), DatabaseConnectionNode.DRIVER_CLASS, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        session.setInput(connectionNode.getId(), DatabaseConnectionNode.JDBC_URL, "jdbc:presto://presto-coordinator.farm.corebi.com:1083?" +
                "SSL=true&" +
                "SSLTrustStorePassword=" + URLEncoder.encode("QwfOh4NLwwMHgwXXdhgVqdeov3M+tGQPJEB/6lK3alo=", "utf8") + "&" +
                "SSLTrustStorePath=" + URLEncoder.encode("C:\\Users\\gorkem.gok\\iCloudDrive\\Documents\\proje\\presto.jks", "utf8"));
        session.setInput(connectionNode.getId(), DatabaseConnectionNode.USERNAME, "_");
        session.setInput(connectionNode.getId(), DatabaseConnectionNode.PASSWORD, "_");

        session.setInput(createNode.getId(), DatabaseExecuteNode.QUERY, "CREATE TABLE data.sdk_test.temp_af_test_1 AS SELECT 1 AS f1, 'ab' AS f2");
        session.setInput(dropNode.getId(), DatabaseExecuteNode.QUERY, "DROP TABLE IF EXISTS data.sdk_test.af_test_1");
        session.setInput(renameNode.getId(), DatabaseExecuteNode.QUERY, "ALTER TABLE data.sdk_test.temp_af_test_1 RENAME TO data.sdk_test.af_test_1");
        session.setInput(dropTempNode.getId(), DatabaseExecuteNode.QUERY, "DROP TABLE IF EXISTS data.sdk_test.temp_af_test_1");

        session.run();
        session.awaitCompletion();
    }
}
