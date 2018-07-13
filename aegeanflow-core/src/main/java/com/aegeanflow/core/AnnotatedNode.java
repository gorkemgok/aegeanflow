package com.aegeanflow.core;

import com.aegeanflow.core.definition.BoxIODefinition;
import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.AnnotatedBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotatedNode<T> extends AbstractNode{

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotatedNode.class);

    private AnnotatedBox<T> annotatedBox;

    private BoxInfo boxInfo;

    private NodeId id;

    @Override
    public void execute() {
        try {
            Exchange<T> exchange = annotatedBox.call();
            boxInfo.getDefinition().getOutputs()
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
    public Collection<Output<?>> getOutputs() {
        List<BoxIODefinition> inputMethods = new ArrayList<>(boxInfo.getDefinition().getOutputs());
        return inputMethods.stream()
                .map(def -> Parameter.output(def.getName(), def.getType()))
                .collect(Collectors.toList());
    }


    @Override
    public Set<Input<?>> getInputs() {
        Map<String, Method> inputMethods = new HashMap<>(boxInfo.getInputMethods());
        inputMethods.putAll(boxInfo.getConfigMethods());
        return inputMethods.entrySet()
                .stream()
                .map(entry -> Parameter.input(entry.getKey(), entry.getValue().getParameterTypes()[0]))
                .collect(Collectors.toSet());
    }

    @Override
    public String getName() {
        return boxInfo.getDefinition().getName();
    }

    @Override
    public NodeId getId() {
        return null;
    }

    public void initialize(AnnotatedBox<T> annotatedBox, Router router) throws IllegalNodeConfigurationException {
        super.initialize(annotatedBox.getUUID(), router);
        this.id = NodeId.of(annotatedBox.getNodeClass());
        this.annotatedBox = annotatedBox;
        BoxInfo boxInfo = CompilerUtil.compile(annotatedBox.getNodeClass());
        this.boxInfo = boxInfo;
    }

    @Override
    protected <T> void setInput(Input<T> input, T value) {
        Method inputMethod = boxInfo.getInputMethods().get(input.name());
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
