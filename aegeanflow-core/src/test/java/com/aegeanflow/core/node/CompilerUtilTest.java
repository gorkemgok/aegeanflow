package com.aegeanflow.core.node;

import com.aegeanflow.core.CompilerUtil;
import com.aegeanflow.core.BoxInfo;
import com.aegeanflow.core.definition.BoxIODefinition;

import java.util.List;

import static org.testng.Assert.*;

public class CompilerUtilTest {
    @org.testng.annotations.Test
    public void testCompile() throws Exception {
        BoxInfo boxInfo1 = CompilerUtil.compile(TestBox2IN1OUT.class);

        assertEquals(boxInfo1.getNodeClass(), TestBox2IN1OUT.class);
        assertEquals(boxInfo1.getDefinition().getName(), TestBox2IN1OUT.NODE_LABEL);
        List<BoxIODefinition> inputDefs1 = boxInfo1.getDefinition().getInputs();
        assertEquals(inputDefs1.size(), 2);
        List<BoxIODefinition> outputDefs1 = boxInfo1.getDefinition().getOutputs();
        assertEquals(outputDefs1.size(), 1);

        assertEquals(inputDefs1.get(0).getLabel(), TestBox2IN1OUT.COUNT_IN_LABEL);
        assertEquals(inputDefs1.get(0).getName(), "count");
        assertEquals(inputDefs1.get(0).getType(), Integer.class);

        assertEquals(inputDefs1.get(1).getLabel(), "text");
        assertEquals(inputDefs1.get(1).getName(), "text");
        assertEquals(inputDefs1.get(1).getType(), String.class);

        assertEquals(outputDefs1.get(0).getLabel(), "main");
        assertEquals(outputDefs1.get(0).getName(), "main");
        assertEquals(outputDefs1.get(0).getType(), String.class);

        BoxInfo boxInfo2 = CompilerUtil.compile(TestBox2IN2OUT.class);

        assertEquals(boxInfo2.getNodeClass(), TestBox2IN2OUT.class);
        assertEquals(boxInfo2.getDefinition().getName(), "TestBox2IN2OUT");
        List<BoxIODefinition> inputDefs2 = boxInfo2.getDefinition().getInputs();
        assertEquals(inputDefs2.size(), 2);
        List<BoxIODefinition> outputDefs2 = boxInfo2.getDefinition().getOutputs();
        assertEquals(outputDefs2.size(), 2);


        assertEquals(inputDefs2.get(0).getLabel(), "text");
        assertEquals(inputDefs2.get(0).getName(), "text");
        assertEquals(inputDefs2.get(0).getType(), String.class);

        assertEquals(inputDefs2.get(1).getLabel(), "count");
        assertEquals(inputDefs2.get(1).getName(), "count");
        assertEquals(inputDefs2.get(1).getType(), Integer.class);

        assertEquals(outputDefs2.get(0).getLabel(), "second");
        assertEquals(outputDefs2.get(0).getName(), "second");
        assertEquals(outputDefs2.get(0).getType(), String.class);

        assertEquals(outputDefs2.get(1).getLabel(), "first");
        assertEquals(outputDefs2.get(1).getName(), "first");
        assertEquals(outputDefs2.get(1).getType(), String.class);

        List<BoxIODefinition> ncd = boxInfo2.getDefinition().getConfigurations();
        assertEquals(ncd.size(), 2);

        assertEquals(ncd.get(0).getLabel(), TestBox2IN2OUT.NODE_CONG_NAME);
        assertEquals(ncd.get(0).getName(), "someConfigList");
        assertEquals(ncd.get(0).getType(), List.class);

        assertEquals(ncd.get(1).getLabel(), "someConfig");
        assertEquals(ncd.get(1).getName(), "someConfig");
        assertEquals(ncd.get(1).getType(), Long.class);

    }

}