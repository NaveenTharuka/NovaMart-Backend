package com.nm.novamart.Dto.Product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateReqDto {

    @NotNull
    private UUID Id;

    @NotNull(message = "Name required")
    private String name;

    @NotNull(message = "Description required")
    private String description;

    @NotNull(message = "Price required")
    private double price;

    @NotNull(message = "Quantity required")
    private int quantity;

    @NotNull
    private String category;
//    private String productImageUrl;

}
