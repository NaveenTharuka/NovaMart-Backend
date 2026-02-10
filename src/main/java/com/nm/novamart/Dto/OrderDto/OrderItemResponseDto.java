package com.nm.novamart.Dto.OrderDto;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {

    private Long id;

    private String productName;
    private Double unitPrice;

    private int quantity;
    private Double price;

}
