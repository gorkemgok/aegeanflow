package com.aegeanflow.rest;

/**
 * Created by gorkem on 23.01.2018.
 */
public class ErrorProxy {

    private String message;

    public ErrorProxy() {
    }

    public ErrorProxy(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
