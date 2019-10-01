package com.example;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class GreetingService {

    public GreetingMessage buildGreetingMessage(String name) {
        return GreetingMessage.of( name + " at " + LocalDateTime.now());
    }
}
