package com.nm.novamart.Dto;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private Double unitPrice;

    private int quantity;
    private Double price;

}
