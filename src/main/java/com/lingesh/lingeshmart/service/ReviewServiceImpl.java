package com.lingesh.lingeshmart.service;

import com.lingesh.lingeshmart.dao.ReviewDAO;
import com.lingesh.lingeshmart.exception.ValidationException;
import com.lingesh.lingeshmart.model.Review;
import com.lingesh.lingeshmart.util.ValidationUtil;
import java.util.List;

public class ReviewServiceImpl implements ReviewService {

    private final ReviewDAO reviewDAO;

    public ReviewServiceImpl(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    @Override
    public Review addReview(Long productId, Long userId, Integer rating, String comment) {
        if (productId == null || userId == null) {
            throw new ValidationException("Product ID and User ID are required");
        }
        if (rating == null || rating < 1 || rating > 5) {
            throw new ValidationException("Rating must be an integer between 1 and 5 stars");
        }
        ValidationUtil.validateNotEmpty("Review Comment", comment);

        if (!reviewDAO.hasUserPurchasedProduct(userId, productId)) {
            throw new ValidationException("You can only submit reviews for products you have purchased in a completed order.");
        }

        Review r = new Review();
        r.setProductId(productId);
        r.setUserId(userId);
        r.setRating(rating);
        r.setComment(comment.trim());

        return reviewDAO.create(r);
    }

    @Override
    public List<Review> getProductReviews(Long productId) {
        return reviewDAO.findByProductId(productId);
    }

    @Override
    public double getAverageRating(Long productId) {
        return reviewDAO.getAverageRating(productId);
    }
}
