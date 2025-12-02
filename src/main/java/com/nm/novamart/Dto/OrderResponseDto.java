package com.nm.novamart.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    private String orderId;
    private String orderStatus;
    private String customerName;
    private String customerEmail;
    private String orderDate;
//    private String orderTime;
    private List<OrderItemResponseDto> orderItems;
    private String orderTotal;


}
