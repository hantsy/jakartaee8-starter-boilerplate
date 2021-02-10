package com.example.jms;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
@Path("hellojms")
public class HelloJmsResource {
    private static final Logger LOGGER = Logger.getLogger(HelloJmsResource.class.getName());
    
    @Inject
    private HelloSender sender;
    
    @GET
    @Path("")
    public String sayHello() {
        LOGGER.log(Level.INFO, "sayHello from HelloJmsResource");
        sender.sayHellFromJms();
        return "sent";
    }
}
