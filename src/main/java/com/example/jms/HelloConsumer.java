package com.example.jms;


import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(
                        propertyName = "destinationType",
                        propertyValue = "javax.jms.Queue"),
                @ActivationConfigProperty(
                        propertyName = "destinationLookup",
                        propertyValue = "java:app/jms/HelloQueue")
        }
)
public class HelloConsumer implements MessageListener {
    private static final Logger LOGGER = Logger.getLogger(HelloConsumer.class.getName());
    
    @Override
    public void onMessage(Message message) {
        try {
            LOGGER.log(Level.INFO, "received message: {0}", message.getBody(String.class));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
