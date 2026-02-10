package com.nm.novamart.Dto.OrderDto;

import lombok.Data;

@Data
public class UpdateOrderStatusDto {
    private String orderId;
    private String status;
    private String cancellationReason;
}
