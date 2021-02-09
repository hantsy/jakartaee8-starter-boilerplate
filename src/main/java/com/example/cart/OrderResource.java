package com.example.cart;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.ok;

@Path("orders")
@RequestScoped
public class OrderResource {
    
    @Inject
    private OrderDao orderDao;
    
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response allOrders() {
        return ok(this.orderDao.findAll()).build();
    }
}
