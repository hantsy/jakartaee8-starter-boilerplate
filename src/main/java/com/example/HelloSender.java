package com.example;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import java.time.Instant;

@ApplicationScoped
public class HelloSender {
    
    @Inject
    JMSContext context;
    
    @Resource(lookup = "java:app/jms/HelloQueue")
    private Destination helloQueue;
    
    public void sayHellFromJms() {
        context.createProducer().send(helloQueue, "Hello JMS at " + Instant.now());
    }
}
