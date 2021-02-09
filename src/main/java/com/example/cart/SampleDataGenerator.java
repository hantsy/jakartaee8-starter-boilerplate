package com.example.cart;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@Singleton
public class SampleDataGenerator {
    private static final Logger LOGGER = Logger.getLogger(SampleDataGenerator.class.getName());
    
    @Inject
    OrderDao orderDao;
    
    @PostConstruct
    public void initialize() {
        LOGGER.log(Level.INFO, "starting persisting sample data...");
        PurchaseOrder po = new PurchaseOrder();
        po.setCustomerId("test");
        po.addItem(new OrderItem("Apple", 3));
        po.addItem(new OrderItem("Orange", 4));
        orderDao.store(po);
        
        orderDao.findAll().forEach(purchaseOrder -> LOGGER.log(Level.INFO, "saved purchase order: {0}", purchaseOrder));
    }
}
