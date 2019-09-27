package com.example;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;

@ApplicationScoped
public class GreetingService {

    public String buildGreetingMessage(String name) {
        return "Say Hello to " + name + " at" + LocalDate.now();
    }
}
