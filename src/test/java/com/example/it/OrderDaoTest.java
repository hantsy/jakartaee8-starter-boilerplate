package com.example.it;

import com.example.cart.OrderDao;
import com.example.cart.OrderItem;
import com.example.cart.PurchaseOrder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class OrderDaoTest {
    private final static Logger LOGGER = Logger.getLogger(OrderDaoTest.class.getName());
    
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test-OrderDaoTest.war")
                .addClass(OrderDao.class)
                .addClass(PurchaseOrder.class)
                .addClass(OrderItem.class)
                .addClass(DbUtil.class)
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @EJB
    OrderDao orderDao;
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Resource(lookup = "java:comp/DefaultDataSource")
    DataSource dataSource;
    
    @Inject
    UserTransaction utx;
    
    private DbUtil dbUtil;
    
    @Before()
    public void setup() throws Exception {
        utx.begin();
        dbUtil = new DbUtil(dataSource);
        dbUtil.clearTables();
        utx.commit();
    }
    
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
        
        dbUtil.assertCount("PurchaseOrder", 1);
        dbUtil.assertCount("OrderItem", 2);
        PurchaseOrder saved = entityManager.find(PurchaseOrder.class, po.getId());
        assertNotNull(saved);
        assertEquals("test", saved.getCustomerId());
    }
    
    
}
