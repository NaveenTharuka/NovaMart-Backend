package com.nm.novamart.Service;

import com.nm.novamart.Dto.Review.ReviewDelReqDto;
import com.nm.novamart.Dto.Review.ReviewReqDto;
import com.nm.novamart.Dto.Review.ReviewResponseDto;
import com.nm.novamart.Entity.Product;
import com.nm.novamart.Entity.Review;
import com.nm.novamart.Entity.User;
import com.nm.novamart.Exeptions.BaseException;
import com.nm.novamart.Exeptions.ProductNotFoundException;
import com.nm.novamart.Exeptions.ReviewNotFoundException;
import com.nm.novamart.Exeptions.UserNotFoundException;
import com.nm.novamart.Mapper.ReviewMapper;
import com.nm.novamart.Repository.ProductRepository;
import com.nm.novamart.Repository.ReviewRepository;
import com.nm.novamart.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponseDto addReview(ReviewReqDto reviewDto) {
        // Validate input
        validateReviewDto(reviewDto);

        // Check for existing review
//        if (reviewRepository.existsByUserIdAndProductId(
//                reviewDto.getUserId(),
//                reviewDto.getProductId()
//        )) {
//            throw new BaseException("You have already reviewed this product");
//        }

        // Get references (more efficient than findById)
        User user = userRepository.getReferenceById(reviewDto.getUserId());
        Product product = productRepository.getReferenceById(reviewDto.getProductId());

        // Create and save review
        Review review = Review.builder()
                .user(user)
                .product(product)
                .comment(reviewDto.getComment().trim())
                .rating(reviewDto.getRating())
                .createdAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);

        // Convert to DTO and return
        return reviewMapper.toResponse(savedReview);
    }

    private void validateReviewDto(ReviewReqDto reviewDto) {
        if (reviewDto.getUserId() == null) {
            throw new BaseException("User id is required");
        }
        if (reviewDto.getProductId() == null) {
            throw new BaseException("Product id is required");
        }
        if (reviewDto.getRating() < 1 || reviewDto.getRating() > 5) {
            throw new BaseException("Rating must be between 1 and 5");
        }
        if (reviewDto.getComment() == null || reviewDto.getComment().trim().isEmpty()) {
            throw new BaseException("Comment is required");
        }
    }

    public List<ReviewResponseDto> getAllReviews() {

        List<Review> reviews = reviewRepository.findAll();
        List<ReviewResponseDto> responses = new ArrayList<>();
        for (Review review : reviews) {
            responses.add(reviewMapper.toResponse(review));
        }
        return responses;
    }

    public void deleteReview(ReviewDelReqDto deleteReq){

        if(!reviewRepository.existsById(deleteReq.getId())){
            throw new ReviewNotFoundException("Review not found");
        }

        reviewRepository.deleteById(deleteReq.getId());
    }
}
