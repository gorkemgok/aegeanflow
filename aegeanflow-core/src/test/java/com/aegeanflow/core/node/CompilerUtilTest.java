package com.aegeanflow.core.node;

import com.aegeanflow.core.CompilerUtil;
import com.aegeanflow.core.NodeInfo;
import com.aegeanflow.core.definition.NodeConfigurationDefinition;
import com.aegeanflow.core.definition.NodeIODefinition;

import java.util.List;

import static org.testng.Assert.*;

public class CompilerUtilTest {
    @org.testng.annotations.Test
    public void testCompile() throws Exception {
        NodeInfo nodeInfo1 = CompilerUtil.compile(TestBox2IN1OUT.class);

        assertEquals(nodeInfo1.getNodeClass(), TestBox2IN1OUT.class);
        assertEquals(nodeInfo1.getDefinition().getLabel(), TestBox2IN1OUT.NODE_LABEL);
        List<NodeIODefinition> inputDefs1 = nodeInfo1.getDefinition().getInputs();
        assertEquals(inputDefs1.size(), 2);
        List<NodeIODefinition> outputDefs1 = nodeInfo1.getDefinition().getOutputs();
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

        NodeInfo nodeInfo2 = CompilerUtil.compile(TestBox2IN2OUT.class);

        assertEquals(nodeInfo2.getNodeClass(), TestBox2IN2OUT.class);
        assertEquals(nodeInfo2.getDefinition().getLabel(), "TestBox2IN2OUT");
        List<NodeIODefinition> inputDefs2 = nodeInfo2.getDefinition().getInputs();
        assertEquals(inputDefs2.size(), 2);
        List<NodeIODefinition> outputDefs2 = nodeInfo2.getDefinition().getOutputs();
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

        List<NodeConfigurationDefinition> ncd = nodeInfo2.getDefinition().getConfigurations();
        assertEquals(ncd.size(), 2);

        assertEquals(ncd.get(0).getLabel(), TestBox2IN2OUT.NODE_CONG_NAME);
        assertEquals(ncd.get(0).getName(), "someConfigList");
        assertEquals(ncd.get(0).getType(), List.class);

        assertEquals(ncd.get(1).getLabel(), "someConfig");
        assertEquals(ncd.get(1).getName(), "someConfig");
        assertEquals(ncd.get(1).getType(), Long.class);

    }

}