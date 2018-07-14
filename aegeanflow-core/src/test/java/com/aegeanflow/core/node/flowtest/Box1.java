package com.aegeanflow.core.node.flowtest;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.annotation.NodeInput;
import com.aegeanflow.core.spi.box.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeConfig;
import com.aegeanflow.core.spi.annotation.NodeEntry;

@NodeEntry
public class Box1 extends AbstractAnnotatedBox<String> {

    private String seedText;

    @Override
    public Exchange<String> call() throws Exception {
        return Exchange.of(seedText);
    }

    @NodeInput
    public void setSeedText(String seedText) {
        this.seedText = seedText;
    }
}
