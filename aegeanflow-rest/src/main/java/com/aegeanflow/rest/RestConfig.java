package com.aegeanflow.rest;

import com.gorkemgok.annoconf.annotation.ConfigBean;
import com.gorkemgok.annoconf.annotation.ConfigParam;

/**
 * Created by gorkem on 15.01.2018.
 */
@ConfigBean
public class RestConfig {

    public final int port;

    public RestConfig(@ConfigParam(key = "rest.port") int port) {
        this.port = port;
    }
}
