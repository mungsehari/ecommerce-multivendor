package com.hari.service;

import com.hari.model.Product;
import com.hari.model.Review;
import com.hari.model.User;
import com.hari.request.CreateReviewRequest;

import java.util.List;

public interface ReviewService {
    Review createReview(
            CreateReviewRequest request,
            User user,
            Product product
    );
    List<Review> getReviewByProductId(Long productId);

    Review updateReview(Long reviewId,String reviewText,double rating,Long userId) throws Exception;

    void deleteReview(Long reviewId,Long userId) throws Exception;

    Review getReviewById(Long reviewId) throws Exception;
}
