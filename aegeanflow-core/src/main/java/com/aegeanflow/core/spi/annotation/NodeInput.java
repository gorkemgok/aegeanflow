package com.aegeanflow.core.spi.annotation;

import com.aegeanflow.core.box.definition.BoxIODefinition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface NodeInput {

    String label() default "";

    int order() default 0;

    BoxIODefinition.InputType inputType() default BoxIODefinition.InputType.INPUT;
}
