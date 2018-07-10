package com.aegeanflow.core;

import java.util.ArrayList;

public class TestMain {

    public static void main(String[] args) {
        Router router = new Router(new ArrayList<>());

        SomeNode someNode = new SomeNode();
        someNode.initialize(router);

        SomeNode someNode2 = new SomeNode();
        someNode2.initialize(router);

        router.connect(someNode, SomeNode.STRING_OUTPUT, someNode2, SomeNode.STRING_INPUT);

        someNode.acceptAndRun(SomeNode.STRING_INPUT, "Test");
        someNode.acceptAndRun(SomeNode.INTEGER_INPUT, 4);

        someNode2.acceptAndRun(SomeNode.INTEGER_INPUT, 2);


    }
}
