package com.aegeanflow.essentials;

import com.aegeanflow.core.spi.node.AbstractSynchronizedNode;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.spi.parameter.Parameter;
import com.facebook.presto.jdbc.internal.guava.collect.Streams;
import com.github.mustachejava.Code;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringMustacheNode extends AbstractSynchronizedNode {

    public static final Input<String> STRING_INPUT = Parameter.input("rawString", String.class);

    public static final Output<String> STRING_OUTPUT = Parameter.output("parsedString", String.class);

    private String string;

    private Map<String, String> codeMap = new HashMap<>();

    private List<Input<String>> codeInputs;

    private Mustache mustache;

    @Override
    protected void run() {
        StringWriter stringWriter = new StringWriter();
        mustache.execute(stringWriter, codeMap);
        stringWriter.flush();
        router.next(STRING_OUTPUT, stringWriter.toString());
    }

    @Override
    protected <T> void setInput(Input<T> input, T value) {
        if (input.equals(STRING_INPUT)) {
            string = (String) value;
            mustache = new DefaultMustacheFactory().compile(new StringReader(string), "rawString");
            codeInputs = new ArrayList<>();
            codeInputs = Stream.of(mustache.getCodes())
                    .map(Code::getName)
                    .filter(Objects::nonNull)
                    .map(code -> Parameter.input(code, String.class))
                    .collect(Collectors.toList());
        } else {
            if(codeInputs != null) {
                for (Input<String> codeInput : codeInputs) {
                    if (input.equals(codeInput)) {
                        codeMap.put(input.name(), (String) value);
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return "String Mustache";
    }

    @Override
    public Collection<Output<?>> getOutputs() {
        return Arrays.asList(STRING_OUTPUT);
    }

    @Override
    public Collection<Input<?>> getInputs() {
        List<Input<?>> inputs = new ArrayList<>();
        inputs.add(STRING_INPUT);
        inputs.addAll(codeInputs);
        return inputs;
    }
}
