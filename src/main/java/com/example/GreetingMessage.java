package com.example;

import java.io.Serializable;

public class GreetingMessage implements Serializable {
    private String message;

    public static GreetingMessage of(String s) {
        final GreetingMessage message = new GreetingMessage();
        message.setMessage("Say Hello to " +s);
        return message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
