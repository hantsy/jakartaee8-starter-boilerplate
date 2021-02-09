package com.example.cart;

public class OrderItemDtoAssembler {
    static OrderItemDto toDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setNum(item.getNum());
        dto.setProductName(item.getProductName());
        
        return dto;
    }
}
