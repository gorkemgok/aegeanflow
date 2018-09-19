package com.aegeanflow.core.node.flowtest;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.box.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

@NodeEntry
public class Box3 extends AbstractAnnotatedBox<StringIntegerPairOutput> {

    private String repeatedText;

    private Integer repeatCount;

    private String seedText;

    private Integer length;

    @Override
    public Exchange<StringIntegerPairOutput> call() throws Exception {
        String text = repeatedText.substring(0, repeatedText.length() / repeatCount);
        return Exchange.createNonpersistent(new StringIntegerPairOutput(seedText + "," + text, length));
    }

    @NodeInput
    public void setLength(Integer length) {
        this.length = length;
    }

    @NodeInput
    public void setRepeatedText(String repeatedText) {
        this.repeatedText = repeatedText;
    }

    @NodeInput
    public void setRepeatCount(Integer repeatCount) {
        this.repeatCount = repeatCount;
    }

    @NodeInput
    public void setSeedText(String seedText) {
        this.seedText = seedText;
    }

}
