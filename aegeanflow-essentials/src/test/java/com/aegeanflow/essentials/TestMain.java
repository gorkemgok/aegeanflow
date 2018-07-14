package com.aegeanflow.essentials;

import com.aegeanflow.core.*;
import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.aegeanflow.essentials.box.ConvertToStringBox;
import com.aegeanflow.essentials.node.SomeNode;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.UUID;

public class TestMain {

    public static void main(String[] args) throws IllegalNodeConfigurationException, ClassNotFoundException {
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

        session.getRouter().connect(uuidGeneratorNode, uuidMainOut, toStringNode, toStringInput);

        session.getRouter().connect(toStringNode, Parameter.output("main", String.class), someNode2, SomeNode.STRING_INPUT);

        uuidGeneratorNode.execute();

        someNode2.acceptAndExecute(SomeNode.INTEGER_INPUT, 2);




    }
}
