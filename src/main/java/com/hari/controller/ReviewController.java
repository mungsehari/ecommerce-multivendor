package com.hari.controller;

import com.hari.model.Product;
import com.hari.model.Review;
import com.hari.model.User;
import com.hari.request.CreateReviewRequest;
import com.hari.response.ApiResponse;
import com.hari.service.ProductService;
import com.hari.service.ReviewService;
import com.hari.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviewByProductId(@PathVariable Long productId){
        List<Review> reviews=reviewService.getReviewByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<Review> writeReview(
            @RequestBody CreateReviewRequest request,
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Product product=productService.findProductById(productId);
        Review review=reviewService.createReview(request,user,product);
        return ResponseEntity.ok(review);

    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long reviewId,
            @RequestBody CreateReviewRequest request,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
       User user=userService.findUserByJwtToken(jwt);
       Review review=reviewService.updateReview(
               reviewId,
               request.getReviewText(),
               request.getReviewRating(),
               user.getId()
       );
       return ResponseEntity.ok(review);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        reviewService.deleteReview(reviewId,user.getId());
        ApiResponse response=new ApiResponse();
        response.setMessage("review deleted successfully");

        return ResponseEntity.ok().build();
    }
}
