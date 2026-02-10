package com.nm.novamart.Dto.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class ReviewResponseDto {

    private UUID reviewId;
    private UUID UserId;
    private String Username;
    private UUID productId;
    private int ratings;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
