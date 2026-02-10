package com.nm.novamart.Dto.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class    ReviewReqDto {

    private UUID productId;
    private UUID userId;
    private String comment;
    private int rating;
}
