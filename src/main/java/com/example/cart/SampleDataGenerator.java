package com.example.cart;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@Singleton
public class SampleDataGenerator {
    private static final Logger LOGGER = Logger.getLogger(SampleDataGenerator.class.getName());
    
    @Inject
    OrderDao orderDao;
    
    @PersistenceContext
    EntityManager entityManager;
    
    @PostConstruct
    public void initialize() {
        LOGGER.log(Level.INFO, "start generating sample data...");
        // clear data
        int deleted =entityManager.createQuery("delete from PurchaseOrder").executeUpdate();
        LOGGER.log(Level.INFO, "clear existing data, deleted purchase order: {0}", deleted);
        // add new data.
        PurchaseOrder po = new PurchaseOrder();
        po.setCustomerId("test");
        po.addItem(new OrderItem("Apple", 3));
        po.addItem(new OrderItem("Orange", 4));
        orderDao.store(po);
        
        orderDao.findAll().forEach(purchaseOrder -> LOGGER.log(Level.INFO, "saved purchase order: {0}", purchaseOrder));
    }
}
