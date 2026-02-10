package com.nm.novamart.Controller;

import com.nm.novamart.Dto.Review.ReviewReqDto;
import com.nm.novamart.Dto.Review.ReviewResponseDto;
import com.nm.novamart.Service.ReviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/review")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewServiceImpl reviewService;


    @PostMapping
    public ResponseEntity<?> addReview(@RequestBody ReviewReqDto reviewDto) {
        reviewService.addReview(reviewDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        return ResponseEntity.ok().body(reviewService.getAllReviews());
    }

}
