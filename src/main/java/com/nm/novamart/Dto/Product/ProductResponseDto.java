package com.nm.novamart.Dto.Product;

import com.nm.novamart.Dto.Review.ProductReviewResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductResponseDto {

    private UUID id;

    private String name;

    private String description;

    private double price;

    private int quantity;

    private String category;

    private double rating;

    private List<ProductReviewResponseDto>  reviews;


}
