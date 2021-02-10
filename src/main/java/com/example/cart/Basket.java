package com.example.cart;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.SynchronizationType;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateful
public class Basket {
    private static final Logger LOGGER = Logger.getLogger(Basket.class.getName());
    
    @PersistenceContext(type = PersistenceContextType.EXTENDED,
            synchronization = SynchronizationType.UNSYNCHRONIZED)
    private EntityManager entityManager;
    
    private PurchaseOrder purchaseOrder;
    
    public void newOrder() {
        LOGGER.log(Level.INFO, "create a new order");
        final String customerId = UUID.randomUUID().toString();
        this.purchaseOrder = new PurchaseOrder();
        this.purchaseOrder.setCustomerId(customerId);
        this.entityManager.persist(this.purchaseOrder);
    }
    
    public void add(String product, int num) {
        LOGGER.log(Level.INFO, "add product to basket:: {0}, {1}", new Object[]{product, num});
        OrderItem item = new OrderItem(product, num);
        item.setOrder(this.purchaseOrder);
        this.entityManager.persist(item);
    }
    
    public void checkout() {
        LOGGER.log(Level.INFO, "checkout purchase order details:: {0}", new Object[]{this.purchaseOrder});
        // Manually sync is a must when synchronization = SynchronizationType.UNSYNCHRONIZED)
        // You have to join a transaction firstly to attach the EntityManager to the current transaction context.
        this.entityManager.joinTransaction();
        this.entityManager.flush();
       
    }
}
