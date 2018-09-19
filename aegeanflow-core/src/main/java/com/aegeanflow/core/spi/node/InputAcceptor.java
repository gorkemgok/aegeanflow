package com.aegeanflow.core.spi.node;

import com.aegeanflow.core.spi.parameter.Input;

public interface InputAcceptor {

    <T> void accept(Input<T> input, T value);

}
