package com.example.it;

import com.example.GreetingMessage;
import com.example.GreetingService;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FakeGreetingService implements GreetingService {
    @Override
    public GreetingMessage buildGreetingMessage(String name) {
        return GreetingMessage.of("fake message");
    }
}
