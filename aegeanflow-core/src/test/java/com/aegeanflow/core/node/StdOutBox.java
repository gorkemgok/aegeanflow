package com.aegeanflow.core.node;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

/**
 * Created by gorkem on 30.01.2018.
 */
@NodeEntry
public class StdOutBox extends AbstractAnnotatedBox<Void> {

    private Object value;

    @Override
    public Exchange<Void> call() throws Exception {
        System.out.println(value.toString());
        return null;
    }

    @NodeInput
    public void setText(Object value) {
        this.value = value;
    }
}
