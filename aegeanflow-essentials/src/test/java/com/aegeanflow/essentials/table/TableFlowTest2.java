package com.aegeanflow.essentials.table;

import com.aegeanflow.core.ioc.AegeanFlowCoreModule;
import com.aegeanflow.core.route.RouteOptions;
import com.aegeanflow.core.route.Router;
import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.spi.node.Node;
import com.aegeanflow.essentials.database.DatabaseConnectionNode;
import com.aegeanflow.essentials.database.DatabaseReaderNode;
import com.aegeanflow.essentials.etl.SubstractTablesNode;
import com.aegeanflow.essentials.resource.*;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

@Guice(modules = AegeanFlowCoreModule.class)
public class TableFlowTest2 {

    @Inject
    Provider<Session> sessionFactory;

    @Inject
    Injector injector;

    @Test
    public void test() throws UnsupportedEncodingException {
        Session session = sessionFactory.get();

        Router router = session.getRouter();
        Node connection1 = session.newNode(UUID.randomUUID(), DatabaseConnectionNode.class);
        Node connection2 = session.newNode(UUID.randomUUID(), DatabaseConnectionNode.class);
        Node reader1 = session.newNode(UUID.randomUUID(), DatabaseReaderNode.class);
        Node reader2 = session.newNode(UUID.randomUUID(), DatabaseReaderNode.class);
        Node substracter = session.newNode(UUID.randomUUID(), SubstractTablesNode.class);
        Node printer = session.newNode(UUID.randomUUID(), TableStdOutNode.class);

        router.connect(connection1, DatabaseConnectionNode.CONNECTION, reader1, DatabaseReaderNode.CONNECTION, new RouteOptions());
        router.connect(connection2, DatabaseConnectionNode.CONNECTION, reader2, DatabaseReaderNode.CONNECTION, new RouteOptions());
        router.connect(reader1, DatabaseReaderNode.TABLE, substracter, SubstractTablesNode.TABLE_INPUT_1, new RouteOptions());
        router.connect(reader2, DatabaseReaderNode.TABLE, substracter, SubstractTablesNode.TABLE_INPUT_2, new RouteOptions());
        router.connect(substracter, SubstractTablesNode.DIFFERENCE_TABLE_OUTPUT, printer, TableStdOutNode.TABLE_INPUT, new RouteOptions());

        session.setInput(connection1.getId(), DatabaseConnectionNode.DRIVER_CLASS, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        session.setInput(connection1.getId(), DatabaseConnectionNode.JDBC_URL, "jdbc:sqlserver://195.214.147.187:2533;loginTimeout=30;");
        session.setInput(connection1.getId(), DatabaseConnectionNode.USERNAME, "13TE3YEx");
        session.setInput(connection1.getId(), DatabaseConnectionNode.PASSWORD, "5d14Bh06xff0%Pw");
        session.setInput(reader1.getId(), DatabaseReaderNode.QUERY, "EXEC INTEGRATION.dbo.COREBI_CEGID_SALES_DAILY_CREATE '2017-01-01','2019-01-01';");

        session.setInput(connection2.getId(), DatabaseConnectionNode.DRIVER_CLASS, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        session.setInput(connection2.getId(), DatabaseConnectionNode.JDBC_URL, "jdbc:presto://presto-coordinator.farm.corebi.com:1083?" +
                "SSL=true&" +
                "SSLTrustStorePassword=" + URLEncoder.encode("QwfOh4NLwwMHgwXXdhgVqdeov3M+tGQPJEB/6lK3alo=", "utf8") + "&" +
                "SSLTrustStorePath=" + URLEncoder.encode("C:\\Users\\gorkem.gok\\iCloudDrive\\Documents\\proje\\presto.jks", "utf8"));
        session.setInput(connection2.getId(), DatabaseConnectionNode.USERNAME, "_");
        session.setInput(connection2.getId(), DatabaseConnectionNode.PASSWORD, "_");
        session.setInput(reader2.getId(), DatabaseReaderNode.QUERY, "select cast(date_trunc('day', creation_date) as date) as creation_date, sum(quantity) as quantity, sum(transaction_value_net_ex) as transaction_value_net_ex from data.flormar_dev.cegid_sales_raw where creation_date >= timestamp '2017-01-01' and creation_date < timestamp '2019-01-01' and import_session_id not like 'noncegidsales' group by 1");

        session.run();
        session.awaitCompletion();
    }
}
