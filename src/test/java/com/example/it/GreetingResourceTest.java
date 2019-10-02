package com.example.it;

import com.example.GreetingMessage;
import com.example.GreetingResource;
import com.example.GreetingService;
import com.example.JaxrsActivator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class GreetingResourceTest {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(GreetingMessage.class)
                .addClass(GreetingService.class)
                .addClasses(GreetingResource.class, JaxrsActivator.class)
                // Enable CDI
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    private URL base;

    private Client client;

    @Before
    public void setup() {
        this.client = ClientBuilder.newClient();
    }

    @After
    public void teardown() {
        if(this.client != null) {
            this.client.close();
        }
    }

    @Test
    public void should_create_greeting() throws MalformedURLException {
        final WebTarget greetingTarget = client.target(URI.create(new URL(base, "api/greeting/JatartaEE").toExternalForm()));
        try (final Response greetingGetResponse = greetingTarget.request()
                .accept(MediaType.APPLICATION_JSON)
                .get()) {
            assertTrue("status is ok" , greetingGetResponse.getStatus()== 200);
            assertTrue("message should start with \"Say Hello to JatartaEE at \"",
                    greetingGetResponse.readEntity(GreetingMessage.class).getMessage().startsWith("Say Hello to JatartaEE"));

        }
    }
}
