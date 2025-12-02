package com.nm.novamart.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderBuyNowDto {
    private UUID userId;
    private UUID productId;
    private int quantity;
    private String comment;

}
