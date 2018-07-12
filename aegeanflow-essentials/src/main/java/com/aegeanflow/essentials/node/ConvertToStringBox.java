package com.aegeanflow.essentials.node;

import com.aegeanflow.core.Exchange;
import com.aegeanflow.core.StringExchange;
import com.aegeanflow.core.spi.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

/**
 * Created by gorkem on 29.01.2018.
 */
@NodeEntry(label = "Convert to String")
public class ConvertToStringBox extends AbstractAnnotatedBox<String> {

    private Object input;

    @Override
    public Exchange<String> call() throws Exception {
        return new StringExchange(input.toString());
    }

    @NodeInput
    public void setInput(Object input) {
        this.input = input;
    }
}
