package com.example.cart;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class OrderItem implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private PurchaseOrder order;
    
    private String productName;
    private Integer num;
    
    public OrderItem() {
    }
    
    public OrderItem(String productName, Integer num) {
        this.productName = productName;
        this.num = num;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public Integer getNum() {
        return num;
    }
    
    public void setNum(Integer num) {
        this.num = num;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public PurchaseOrder getOrder() {
        return order;
    }
    
    public void setOrder(PurchaseOrder order) {
        this.order = order;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return productName.equals(orderItem.productName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(productName);
    }
    
    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", num=" + num +
                '}';
    }
}
