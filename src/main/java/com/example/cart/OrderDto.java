package com.example.cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderDto implements Serializable {
    
    private Long id;
    private String customerId;
    List<OrderItemDto> items = new ArrayList<>();
    
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
    
    public List<OrderItemDto> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }
    
    public void addItem(OrderItemDto itemDto){
        this.items.add(itemDto);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return Objects.equals(id, orderDto.id)
                && Objects.equals(customerId, orderDto.customerId)
                && Objects.equals(items, orderDto.items);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, items);
    }
}
