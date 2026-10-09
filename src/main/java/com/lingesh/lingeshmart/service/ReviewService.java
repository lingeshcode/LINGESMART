package com.lingesh.lingeshmart.service;

import com.lingesh.lingeshmart.model.Review;
import java.util.List;

public interface ReviewService {
    Review addReview(Long productId, Long userId, Integer rating, String comment);
    List<Review> getProductReviews(Long productId);
    double getAverageRating(Long productId);
}
