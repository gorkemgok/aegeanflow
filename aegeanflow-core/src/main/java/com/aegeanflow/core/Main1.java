package com.aegeanflow.core;

import com.aegeanflow.core.definition.Parser;

import java.util.UUID;

public class Main1 {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("com.aegeanflow.core.TestNode");
        Node node = (Node)clazz.newInstance();
        node.setUUID(UUID.randomUUID());
        NodeCompiler nodeCompiler = new NodeCompiler();
        nodeCompiler.compile(new TestNode());

        Parser parser = new Parser();
        System.out.printf(parser.parse(TestNode.class));
    }
}
