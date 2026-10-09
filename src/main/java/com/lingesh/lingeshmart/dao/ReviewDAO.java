package com.lingesh.lingeshmart.dao;

import com.lingesh.lingeshmart.model.Review;
import java.util.List;

public interface ReviewDAO {
    Review create(Review review);
    List<Review> findByProductId(Long productId);
    double getAverageRating(Long productId);
    boolean hasUserPurchasedProduct(Long userId, Long productId);
}
