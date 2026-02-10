package com.nm.novamart.Mapper;

import com.nm.novamart.Dto.Review.ReviewResponseDto;
import com.nm.novamart.Entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReviewMapper {
    public ReviewResponseDto toResponse(Review review){

        return ReviewResponseDto.builder()
                .reviewId(review.getId())
                .productId(review.getProduct().getId())
                .UserId(review.getUser().getId())
                .Username(review.getUser().getUserName())
                .ratings(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();

    }

}
