package com.example.it;

import com.example.jms.HelloSender;
import com.example.jms.JmsResources;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.*;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class HelloJmsTest {
    private final static Logger LOGGER = Logger.getLogger(HelloJmsTest.class.getName());
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(JmsResources.class)
                // .addClass(HelloConsumer.class)
                .addClass(HelloSender.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Inject
    HelloSender sender;
    
    @Inject
    JMSContext jmsContext;
    
    @Resource(lookup = "java:app/jms/HelloQueue")
    private Destination helloQueue;
    
    @Test
    public void testHelloQueue() throws JMSException {
        JMSConsumer consumer = jmsContext.createConsumer(helloQueue);
        sender.sayHellFromJms();
        Message message = consumer.receive(1000);
        assertTrue(message instanceof TextMessage);
        String text = message.getBody(String.class);
        LOGGER.info("message text:" + text);
        assertTrue(text.startsWith("Hello JMS"));
        consumer.close();
    }
}
