package com.example;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class SimpleGreetingService implements GreetingService {

    @Override
    public GreetingMessage buildGreetingMessage(String name) {
        return GreetingMessage.of("Say Hello to " + name + " at " + LocalDateTime.now());
    }
}
