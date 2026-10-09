package com.lingesh.lingeshmart.dao;

import com.lingesh.lingeshmart.exception.DatabaseException;
import com.lingesh.lingeshmart.model.Review;
import com.lingesh.lingeshmart.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOImpl implements ReviewDAO {

    @Override
    public Review create(Review review) {
        String sql = "INSERT INTO reviews (product_id, user_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setLong(1, review.getProductId());
            ps.setLong(2, review.getUserId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    review.setId(rs.getLong(1));
                }
            }
            return review;
        } catch (SQLException e) {
            throw new DatabaseException("Error creating product review", e);
        }
    }

    @Override
    public List<Review> findByProductId(Long productId) {
        String sql = "SELECT r.*, u.name AS user_name FROM reviews r JOIN users u ON r.user_id = u.id WHERE r.product_id = ? ORDER BY r.id DESC";
        List<Review> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review();
                    r.setId(rs.getLong("id"));
                    r.setProductId(rs.getLong("product_id"));
                    r.setUserId(rs.getLong("user_id"));
                    r.setRating(rs.getInt("rating"));
                    r.setComment(rs.getString("comment"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    r.setUserName(rs.getString("user_name"));
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching reviews for product ID: " + productId, e);
        }
        return list;
    }

    @Override
    public double getAverageRating(Long productId) {
        String sql = "SELECT AVG(CAST(rating AS DOUBLE)) FROM reviews WHERE product_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error calculating average rating", e);
        }
        return 0.0;
    }

    @Override
    public boolean hasUserPurchasedProduct(Long userId, Long productId) {
        String sql = "SELECT COUNT(*) FROM orders o JOIN order_items oi ON o.id = oi.order_id WHERE o.buyer_id = ? AND oi.product_id = ? AND o.status != 'CANCELLED'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error checking user purchase history", e);
        }
        return false;
    }
}
