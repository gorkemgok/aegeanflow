package com.aegeanflow.essentials.data;

/**
 * Created by gorkem on 18.01.2018.
 */
public interface Convertor<I, O> {

    O convert(I input) throws Exception;
}
