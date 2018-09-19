package com.aegeanflow.core.table;

import com.aegeanflow.core.ioc.CoreModule;
import com.aegeanflow.core.resource.RandomTableGeneratorNode;
import com.aegeanflow.core.resource.TableStdOutNode;
import com.aegeanflow.core.route.RouteOptions;
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
    public void test() throws InterruptedException {
        Session session1 = sessionFactory.create();
        Session session2 = sessionFactory.create();
        Assert.assertNotEquals(session1.getUuid(), session2.getUuid());

        Node randomTableNode = session1.newNode(UUID.randomUUID(), RandomTableGeneratorNode.class);
        Node tableStdOutNode = session1.newNode(UUID.randomUUID(), TableStdOutNode.class);
        session1.getRouter().connect(
            randomTableNode, RandomTableGeneratorNode.MAIN_OUTPUT, tableStdOutNode, TableStdOutNode.TABLE_INPUT,
            new RouteOptions());
        session1.run();

        session1.awaitCompletion();
    }
}
