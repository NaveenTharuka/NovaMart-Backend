package com.nm.novamart.Dto.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReviewResponseDto {

    private String comment;
    private double rating;
    private String username;
    private LocalDateTime createdAt;

}
