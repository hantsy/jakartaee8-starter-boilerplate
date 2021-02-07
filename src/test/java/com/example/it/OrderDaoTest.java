package com.example.it;

import com.example.cart.OrderDao;
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
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class OrderDaoTest {
    private final static Logger LOGGER = Logger.getLogger(OrderDaoTest.class.getName());
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(OrderDao.class)
                .addClass(PurchaseOrder.class)
                .addClass(OrderItem.class)
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @EJB
    OrderDao orderDao;
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Resource(lookup = "java:comp/DefaultDataSource")
    DataSource dataSource;
    
    @Inject
    UserTransaction utx;
    
    @Test
    public void testNewOrder() throws Exception {
        utx.begin();
        LOGGER.log(Level.INFO, " Running test:: Basket :: new order... ");
        PurchaseOrder po = new PurchaseOrder();
        po.setCustomerId("test");
        po.addItem(new OrderItem("Apple", 3));
        po.addItem(new OrderItem("Orange", 4));
        
        orderDao.store(po);
        utx.commit();
        
        assertCount("PurchaseOrder", 1);
        assertCount("OrderItem", 2);
        PurchaseOrder saved = entityManager.find(PurchaseOrder.class, po.getId());
        assertNotNull(saved);
        assertEquals("test", saved.getCustomerId());
    }
    
    public void assertCount(String tableName, int count) throws SQLException {
        Connection conn = dataSource.getConnection();
        ResultSet rs = conn.prepareStatement("select count(*) from " + tableName + "")
                .executeQuery();
        int rowCount = 0;
        if (rs.next()) {
            rowCount = rs.getInt(1);
        }
        assertEquals(count, rowCount);
    }
}
