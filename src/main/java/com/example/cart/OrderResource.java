package com.example.cart;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.Response.ok;

@Path("orders")
@RequestScoped
public class OrderResource {
    
    @Inject
    private OrderDao orderDao;
    
    @Transactional
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response allOrders() {
        return ok(this.orderDao.findAll().stream().map(OrderDtoAssembler::toDto).collect(toList())).build();
    }
}
