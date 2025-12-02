package com.nm.novamart.Dto;

import com.nm.novamart.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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
