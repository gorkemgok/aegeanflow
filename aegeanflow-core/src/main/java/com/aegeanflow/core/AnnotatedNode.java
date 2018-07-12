package com.aegeanflow.core;

import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.aegeanflow.core.spi.AnnotatedBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotatedNode<T> extends AbstractNode{

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotatedNode.class);

    private AnnotatedBox<T> annotatedBox;

    private NodeInfo nodeInfo;

    @Override
    public void run() {
        try {
            Exchange<T> exchange = annotatedBox.call();
            nodeInfo.getDefinition().getOutputs()
                    .forEach(nodeIODefinition -> {
                        try {
                            Output output = Parameter.output(nodeIODefinition.getName(), nodeIODefinition.getType());
                            Exchange ex;
                            if (nodeIODefinition.getMethod() != null) {
                                ex = (Exchange) nodeIODefinition.getMethod().invoke(exchange.get());
                            } else {
                                ex = exchange;
                            }
                            router.next(output, ex);
                        } catch (IllegalAccessException e) {
                            LOGGER.error("Illegal configured class {}", annotatedBox.getNodeClass());
                        } catch (InvocationTargetException e) {
                            LOGGER.error("Illegal invocation of class {}", annotatedBox.getNodeClass());
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public Set<Input<?>> listInputs() {
        Map<String, Method> inputMethods = new HashMap<>(nodeInfo.getInputMethods());
        inputMethods.putAll(nodeInfo.getConfigMethods());
        return inputMethods.entrySet()
                .stream()
                .map(entry -> Parameter.input(entry.getKey(), entry.getValue().getParameterTypes()[0]))
                .collect(Collectors.toSet());
    }

    public void initialize(AnnotatedBox<T> annotatedBox, Router router) throws IllegalNodeConfigurationException {
        super.initialize(annotatedBox.getUUID(), router);
        this.annotatedBox = annotatedBox;
        NodeInfo nodeInfo = CompilerUtil.compile(annotatedBox.getNodeClass());
        this.nodeInfo = nodeInfo;
    }

    @Override
    protected <T> void setInput(Input<T> input, T value) {
        Method inputMethod = nodeInfo.getInputMethods().get(input.name());
        if (inputMethod != null) {
            try {
                inputMethod.invoke(annotatedBox, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
