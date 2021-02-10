package com.example.cart;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class OrderDao {
    
    @PersistenceContext
    EntityManager entityManager;
    
    public List<PurchaseOrder> findAll() {
        return this.entityManager.createQuery("select po from PurchaseOrder po", PurchaseOrder.class)
                .getResultList();
    }
    
    public void store(PurchaseOrder data) {
        this.entityManager.persist(data);
    }
    
}
