package com.aegeanflow.essentials.box;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.exchange.StringExchange;
import com.aegeanflow.core.spi.box.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

/**
 * Created by gorkem on 29.01.2018.
 */
@NodeEntry(name = "Convert to String")
public class ConvertToStringBox extends AbstractAnnotatedBox<String> {

    private Object input;

    @Override
    public Exchange<String> call() throws Exception {
        return Exchange.of(input.toString());
    }

    @NodeInput
    public void setInput(Object input) {
        this.input = input;
    }
}
