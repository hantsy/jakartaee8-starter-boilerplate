package com.example.jms;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class HelloSender {
    private static final Logger LOGGER = Logger.getLogger(HelloSender.class.getName());
    
    @Inject
    JMSContext context;
    
    @Resource(lookup = "java:app/jms/HelloQueue")
    private Destination helloQueue;
    
    public void sayHellFromJms() {
        String msg = "Hello JMS at " + Instant.now();
        LOGGER.log(Level.INFO, "sending message from HelloSender: {0}", msg);
        context.createProducer().send(helloQueue, msg);
    }
}
