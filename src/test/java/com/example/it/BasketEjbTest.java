package com.example.it;

import com.example.cart.Basket;
import com.example.cart.OrderItem;
import com.example.cart.PurchaseOrder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class BasketEjbTest {
    private final static Logger LOGGER = Logger.getLogger(BasketEjbTest.class.getName());
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(Basket.class)
                .addClass(PurchaseOrder.class)
                .addClass(OrderItem.class)
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @EJB
    Basket basket;
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Resource(lookup = "java:comp/DefaultDataSource")
    DataSource dataSource;

//    @Test
//    @InSequence(1)
//    public void testNewOrder() throws Exception {
//        LOGGER.log(Level.INFO, " Running test:: Basket :: new order ... ");
//        basket.newOrder();
//        assertCount(0);
//        assertEquals(0, this.entityManager.createQuery("select po from PurchaseOrder po", PurchaseOrder.class)
//                .getResultList().size());
//    }
//
//    @Test
//    @InSequence(2)
//    public void testAddProductsToCarts() throws Exception {
//        LOGGER.log(Level.INFO, " Running test:: Basket :: add products... ");
//        basket.add("Apple", 4);
//        basket.add("Orange", 5);
//        assertCount(0);
//        assertEquals(0, this.entityManager.createQuery("select po from PurchaseOrder po", PurchaseOrder.class)
//                .getResultList().size());
//    }
//
//    @Test
//    @InSequence(3)
//    public void testCheckout() throws Exception {
//        LOGGER.log(Level.INFO, " Running test:: Basket:: checkout ... ");
//        basket.checkout();
//        assertCount(1);
//        assertEquals(1, this.entityManager.createQuery("select po from PurchaseOrder po", PurchaseOrder.class)
//                .getResultList().size());
//    }
    
    @Test
    public void testNewOrder() throws Exception {
        LOGGER.log(Level.INFO, " Running test:: Basket :: new order... ");
        basket.newOrder();
        assertCount("PurchaseOrder",0);
        assertCount("OrderItem",0);
        
        LOGGER.log(Level.INFO, " Running test:: Basket :: add products to basket... ");
        basket.add("Apple", 4);
        basket.add("Orange", 5);
        assertCount("PurchaseOrder",0);
        assertCount("OrderItem",0);
        
        LOGGER.log(Level.INFO, " Running test:: Basket :: checkout... ");
        basket.checkout();
        assertCount("PurchaseOrder",1);
        assertCount("OrderItem",2);
    }
    
    public void assertCount(String tableName, int count) throws SQLException {
        Connection conn = dataSource.getConnection();
        ResultSet rs = conn.prepareStatement("select count(*) from "+tableName+"")
                .executeQuery();
        int rowCount = 0;
        if (rs.next()) {
            rowCount = rs.getInt(1);
        }
        assertEquals(count, rowCount);
    }
}
