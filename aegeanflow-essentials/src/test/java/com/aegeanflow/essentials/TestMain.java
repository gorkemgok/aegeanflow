package com.aegeanflow.essentials;

import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.aegeanflow.core.ioc.CoreModule;
import com.aegeanflow.core.node.AnnotatedNode;
import com.aegeanflow.core.route.RouteOptions;
import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.spi.node.Node;
import com.aegeanflow.core.spi.parameter.Parameter;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.essentials.box.ConvertToStringBox;
import com.aegeanflow.essentials.node.SomeNode;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.UUID;

public class TestMain {

    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new CoreModule(), new EssentialsModule());

        Session session = injector.getInstance(Session.class);

        String someClassName = "com.aegeanflow.essentials.node.SomeNode";
        Class someClazz = Class.forName(someClassName);

        String uuidClassName = "com.aegeanflow.essentials.box.UUIDGeneratorBox";
        Class uuidClass = Class.forName(uuidClassName);

        Node someNode = session.newNode(UUID.randomUUID(), someClazz);

        SomeNode someNode2 = session.newNode(UUID.randomUUID(), SomeNode.class);

        AnnotatedNode uuidGeneratorNode = session.newBox(UUID.randomUUID(), uuidClass);

        AnnotatedNode<String> toStringNode = session.newBox(UUID.randomUUID(), ConvertToStringBox.class);

        Output uuidMainOut = uuidGeneratorNode.getOutput("main").get();

        Input toStringInput = toStringNode.getInput("input").get();

        session.getRouter().connect(uuidGeneratorNode, uuidMainOut, toStringNode, toStringInput, new RouteOptions());

        session.getRouter().connect(toStringNode, Parameter.output("main", String.class), someNode2, SomeNode.STRING_INPUT, new RouteOptions());

        uuidGeneratorNode.execute();

        someNode2.accept(SomeNode.INTEGER_INPUT, 2);
        

    }
}
