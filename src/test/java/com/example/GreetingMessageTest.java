package com.example;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GreetingMessageTest {

    @Test
    public void testGreetingMessage() {
        GreetingMessage message = GreetingMessage.of("Hantsy");
        assertTrue("message should start with Say Hello to Hantsy",
                "Say Hello to Hantsy".equals(message.getMessage()
                ));
    }
}
