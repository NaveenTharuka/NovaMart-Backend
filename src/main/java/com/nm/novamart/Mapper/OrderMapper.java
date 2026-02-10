package com.nm.novamart.Mapper;

import com.nm.novamart.Dto.OrderDto.OrderResponseDto;
import com.nm.novamart.Entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    public OrderResponseDto toResponse(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .orderStatus(order.getStatus().toString())
                .customerName(order.getUser().getUserName())
                .customerEmail(order.getUser().getEmail())
                .orderDate(order.getOrderDate().toString())
                .orderItems(orderItemMapper.toResponse(order.getOrderItems()))
                .orderTotal(order.getTotalAmount())
                .shippingAddress(order.getAddress())
                .build();
    }
}
