package com.aegeanflow.essentials;

import com.aegeanflow.core.AnnotatedNode;
import com.aegeanflow.core.Parameter;
import com.aegeanflow.core.Router;
import com.aegeanflow.core.SomeNode;
import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.aegeanflow.essentials.node.ConvertToStringBox;
import com.aegeanflow.essentials.node.UUIDGeneratorBox;

import java.util.ArrayList;
import java.util.UUID;

public class TestMain {

    public static void main(String[] args) throws IllegalNodeConfigurationException {
        Router router = new Router(new ArrayList<>());

        SomeNode someNode = new SomeNode();
        someNode.initialize(UUID.randomUUID(),router);

        SomeNode someNode2 = new SomeNode();
        someNode2.initialize(UUID.randomUUID(), router);

        UUIDGeneratorBox uuidGeneratorBox = new UUIDGeneratorBox();
        uuidGeneratorBox.setUUID(UUID.randomUUID());
        AnnotatedNode<UUID> uuidGeneratorNode = new AnnotatedNode<>();
        uuidGeneratorNode.initialize(uuidGeneratorBox, router);

        ConvertToStringBox convertToStringBox = new ConvertToStringBox();
        convertToStringBox.setUUID(UUID.randomUUID());
        AnnotatedNode<String> toStringNode = new AnnotatedNode<>();
        toStringNode.initialize(convertToStringBox, router);


        router.connect(uuidGeneratorNode, Parameter.output("main", UUID.class),
                toStringNode, Parameter.input("input", Object.class));

        router.connect(toStringNode, Parameter.output("main", String.class), someNode2, SomeNode.STRING_INPUT);

        uuidGeneratorNode.run();

        someNode2.acceptAndRun(SomeNode.INTEGER_INPUT, 2);




    }
}
