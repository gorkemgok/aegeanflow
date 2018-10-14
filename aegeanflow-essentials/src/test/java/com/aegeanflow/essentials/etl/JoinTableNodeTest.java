package com.aegeanflow.essentials.etl;

import com.aegeanflow.core.ioc.AegeanFlowCoreModule;
import com.aegeanflow.core.route.RouteOptions;
import com.aegeanflow.core.session.Session;
import com.aegeanflow.essentials.resource.TableStdOutNode;
import com.aegeanflow.essentials.table.*;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

@Guice(modules = AegeanFlowCoreModule.class)
public class JoinTableNodeTest {

    @Inject
    Provider<Session> sessionProvider;

    @Test
    public void testRun() {
        Session session = sessionProvider.get();
        JoinTableNode joinTableNode = session.newNode(UUID.randomUUID(), JoinTableNode.class);

        List<Field> fieldList1 = Arrays.asList(
                new Field("date", Date.class),
                new Field("quantity", Integer.class),
                new Field("price", Double.class)
        );
        List<Field> fieldList2 = Arrays.asList(
                new Field("NATIVEDATE", Date.class),
                new Field("QUANTITY", Integer.class),
                new Field("TRANSACTION_VALUE_NET_EX", Double.class)
        );
        Table table1 = new RandomAccessTable(new Schema(fieldList1));
        Table table2 = new RandomAccessTable(new Schema(fieldList2));

        table1.add(new Row(new Date(0), 3, 3.0));
        table1.add(new Row(new Date(TimeUnit.DAYS.toMillis(1)), 4, 4.0));
        table1.add(new Row(new Date(TimeUnit.DAYS.toMillis(2)), 5, 5.0));
        table1.close();

        table2.add(new Row(new Date(0), 30, 30.0));
        table2.add(new Row(new Date(TimeUnit.DAYS.toMillis(1)), 40, 40.0));
        table2.add(new Row(new Date(TimeUnit.DAYS.toMillis(2)), 50, 50.0));
        table2.close();

        List<String> joinColumns1 = Arrays.asList("NATIVEDATE");
        List<String> joinColumns2 = Arrays.asList("date");

        session.setInput(joinTableNode.getId(), JoinTableNode.IN_JOIN_COLUMNS_1, joinColumns1);
        session.setInput(joinTableNode.getId(), JoinTableNode.IN_JOIN_COLUMNS_2, joinColumns2);
        session.setInput(joinTableNode.getId(), JoinTableNode.IN_TABLE_1, table1);
        session.setInput(joinTableNode.getId(), JoinTableNode.IN_TABLE_2, table2);

        TableStdOutNode stdOutNode = session.newNode(UUID.randomUUID(), TableStdOutNode.class);

        session.getRouter().connect(joinTableNode, JoinTableNode.OUT_MERGED_TABLE, stdOutNode, TableStdOutNode.TABLE_INPUT, new RouteOptions());

        session.run();
        session.awaitCompletion();
    }
}