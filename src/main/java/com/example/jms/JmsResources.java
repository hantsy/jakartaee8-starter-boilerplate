package com.example.jms;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;

@JMSDestinationDefinitions({
        @JMSDestinationDefinition(
                name = "java:app/jms/HelloQueue",
                // resourceAdapter = "jmsra",
                interfaceName = "javax.jms.Queue",
                destinationName = "HelloQueue")
})
@Singleton
@Startup
public class JmsResources {
}
