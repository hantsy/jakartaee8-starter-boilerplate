package com.example.cart;

public class OrderDtoAssembler {
    
    static OrderDto toDto(PurchaseOrder order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId());
        order.getItems().stream().map(OrderItemDtoAssembler::toDto).forEach(dto::addItem);
        return  dto;
    }
}
