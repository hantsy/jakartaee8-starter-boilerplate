package com.example.cart;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class PurchaseOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String customerId;
    
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    List<OrderItem> items = new ArrayList<>();
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public List<OrderItem> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseOrder that = (PurchaseOrder) o;
        return customerId.equals(that.customerId) && items.equals(that.items);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(customerId, items);
    }
    
    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "id=" + id +
                ", customerId='" + customerId + '\'' +
                ", items=" + items +
                '}';
    }
    
    public void addItem(OrderItem item) {
        item.setOrder(this);
        this.items.add(item);
    }
}
