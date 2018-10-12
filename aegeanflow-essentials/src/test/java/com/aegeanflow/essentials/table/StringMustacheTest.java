package com.aegeanflow.essentials.table;

import com.aegeanflow.core.ioc.AegeanFlowCoreModule;
import com.aegeanflow.core.route.RouteOptions;
import com.aegeanflow.core.route.Router;
import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.spi.node.Node;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.essentials.StringMustacheNode;
import com.aegeanflow.essentials.resource.StdOutNode;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.UUID;

@Guice(modules = AegeanFlowCoreModule.class)
public class StringMustacheTest {

    @Inject
    Provider<Session> sessionFactory;

    @Inject
    Injector injector;

    @Test
    public void test() {
        Session session = sessionFactory.get();

        Router router = session.getRouter();

        Node mustacheNode = session.newNode(UUID.randomUUID(), StringMustacheNode.class);
        Node stdOut = session.newNode(UUID.randomUUID(), StdOutNode.class);

        router.connect(mustacheNode, StringMustacheNode.STRING_OUTPUT, stdOut, StdOutNode.VALUE, new RouteOptions());

        session.setInput(mustacheNode.getId(), StringMustacheNode.STRING_INPUT, "Deneme {{deneme1}}, {{deneme2}}");
        session.setInput(mustacheNode.getId(), (Input<? super String>) mustacheNode.getInput("deneme1").get(), "DENEME1");
        session.setInput(mustacheNode.getId(), (Input<? super String>) mustacheNode.getInput("deneme2").get(), "DENEME2");

        session.run();
        session.awaitCompletion();
    }
}
