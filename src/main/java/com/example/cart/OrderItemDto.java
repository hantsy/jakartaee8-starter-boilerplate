package com.example.cart;

import java.io.Serializable;
import java.util.Objects;

public class OrderItemDto implements Serializable {
    private String productName;
    private Integer num;
    
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemDto that = (OrderItemDto) o;
        return Objects.equals(productName, that.productName) && Objects.equals(num, that.num);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(productName, num);
    }
}
