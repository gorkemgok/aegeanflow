package com.aegeanflow.core.spi.parameter;

import com.google.inject.TypeLiteral;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class ParameterImplTest {

    @Test
    public void testIsAssignable() {
        Input<String> stringInput1 = Parameter.input("stringInput", String.class);
        Input<String> stringInput2 = Parameter.input("stringInput", String.class);
        assertTrue(stringInput1.isAssignable(stringInput2));
        Input<List<String>> stringListInput1 = Parameter.input("stringListInput", new TypeLiteral<List<String>>(){});
        Input<List<String>> stringListInput2 = Parameter.input("stringListInput", new TypeLiteral<List<String>>(){});
        Input<List<Integer>> integerListInput = Parameter.input("stringListInput", new TypeLiteral<List<Integer>>(){});
        Input<List> listInput = Parameter.input("stringListInput", new TypeLiteral<List>(){});
        assertTrue(stringListInput1.isAssignable(stringListInput2));
        assertFalse(stringListInput1.isAssignable(integerListInput));
        assertFalse(stringListInput1.isAssignable(listInput));

        Input<Map> mapInput = Parameter.input("mapInput", new TypeLiteral<Map>() {});
        Input<Map<String, String>> mapInput1 = Parameter.input("mapInput", new TypeLiteral<Map<String, String>>() {});
        Input<Map<String, String>> mapInput2 = Parameter.input("mapInput", new TypeLiteral<Map<String, String>>() {});
        Input<Map<String, Integer>> mapInput3 = Parameter.input("mapInput", new TypeLiteral<Map<String, Integer>>() {});
        assertFalse(mapInput.isAssignable(mapInput1));
        assertFalse(mapInput2.isAssignable(mapInput3));
        assertTrue(mapInput1.isAssignable(mapInput2));
    }
}