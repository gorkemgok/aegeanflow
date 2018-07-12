package com.aegeanflow.essentials.example;

import com.aegeanflow.core.Exchange;
import com.aegeanflow.core.StringExchange;
import com.aegeanflow.core.spi.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeConfig;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

/**
 * Created by gorkem on 12.01.2018.
 */
@NodeEntry
public class StringSuffixBox extends AbstractAnnotatedBox<String> {

    private String suffix;

    private String input;

    @Override
    public Exchange<String> call() throws Exception {
        return new StringExchange(input + "-" + suffix);
    }

    @NodeInput
    public void setInput(String input) {
        this.input = input;
    }

    @NodeConfig
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
