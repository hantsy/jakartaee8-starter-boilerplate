package com.example.it;

import com.example.JaxrsActivator;
import com.example.cart.*;
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
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class OrderResourceTest {
    private final static Logger LOGGER = Logger.getLogger(OrderResourceTest.class.getName());
    
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(OrderDao.class)
                .addClass(PurchaseOrder.class)
                .addClass(OrderItem.class)
                .addClass(SampleDataGenerator.class)
                .addClasses(OrderResource.class, JaxrsActivator.class)
                .addClasses(OrderDto.class, OrderItemDto.class, OrderDtoAssembler.class, OrderItemDtoAssembler.class)
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                // Enable CDI
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @ArquillianResource
    private URL base;
    
    private Client client;
    
    @Before
    public void setup() {
        this.client = ClientBuilder.newClient();
        try {
            LOGGER.log(Level.INFO, " Registering 'com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider' in OpenLiberty/CXF JAX-RS Client ");
            Class<?> clazz = Class.forName("com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider");
            this.client.register(clazz);
        } catch (ClassNotFoundException e) {
            LOGGER.warning("Failed to register 'com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider'. OpenLiberty/CXF does not register a json provider automatically. Please ignore this warning for none OpenLiberty Servers.");
        }
        
    }
    
    @After
    public void teardown() {
        if (this.client != null) {
            this.client.close();
        }
    }
    
    @Test
    public void shouldGetAllOrders() throws MalformedURLException {
        LOGGER.log(Level.INFO, " Running test:: OrderResourceTest#shouldGetAllOrders ... ");
        final WebTarget allOrdersTarget = client.target(new URL(base, "api/orders").toExternalForm());
        try (final Response getAllOrdersResponse = allOrdersTarget.request()
                .accept(MediaType.APPLICATION_JSON)
                .get()) {
            assertEquals("response status is ok", 200, getAllOrdersResponse.getStatus());
            assertEquals(1, getAllOrdersResponse.readEntity(new GenericType<List<PurchaseOrder>>() { }).size());
        }
    }
}
