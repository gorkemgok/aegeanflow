package com.aegeanflow.core;

import com.aegeanflow.core.exception.NodePreconditionException;
import com.aegeanflow.core.spi.Node;

import static java.lang.String.format;

/**
 * Created by gorkem on 23.01.2018.
 */
public class Precondition {

    public static void checkNotNullInput(Object obj, String name, Node node) throws NodePreconditionException{
        if (obj == null) {
            throw new NodePreconditionException(format("%s must be set", name), node.getUUID());
        }
    }
}
