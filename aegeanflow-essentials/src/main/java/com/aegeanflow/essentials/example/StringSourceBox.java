package com.aegeanflow.essentials.example;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.exchange.StringExchange;
import com.aegeanflow.core.spi.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeConfig;
import com.aegeanflow.core.spi.annotation.NodeEntry;

/**
 * Created by gorkem on 12.01.2018.
 */
@NodeEntry
public class StringSourceBox extends AbstractAnnotatedBox<String> {

    private String prefix;

    @Override
    public Exchange<String> call() throws Exception {
        return new StringExchange(prefix + "-string");
    }

    @NodeConfig
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
