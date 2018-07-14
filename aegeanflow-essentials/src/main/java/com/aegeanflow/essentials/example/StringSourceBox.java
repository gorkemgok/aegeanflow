package com.aegeanflow.essentials.example;

import com.aegeanflow.core.box.definition.BoxIODefinition;
import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.exchange.StringExchange;
import com.aegeanflow.core.spi.annotation.NodeInput;
import com.aegeanflow.core.spi.box.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeConfig;
import com.aegeanflow.core.spi.annotation.NodeEntry;

import static com.aegeanflow.core.box.definition.BoxIODefinition.InputType.CONGIF;

/**
 * Created by gorkem on 12.01.2018.
 */
@NodeEntry
public class StringSourceBox extends AbstractAnnotatedBox<String> {

    private String prefix;

    @Override
    public Exchange<String> call() throws Exception {
        return Exchange.of(prefix + "-string");
    }

    @NodeInput(inputType = CONGIF)
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
