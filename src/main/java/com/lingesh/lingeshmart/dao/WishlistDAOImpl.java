package com.lingesh.lingeshmart.dao;

import com.lingesh.lingeshmart.exception.DatabaseException;
import com.lingesh.lingeshmart.model.Product;
import com.lingesh.lingeshmart.model.WishlistItem;
import com.lingesh.lingeshmart.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAOImpl implements WishlistDAO {

    @Override
    public boolean addToWishlist(Long userId, Long productId) {
        String sql = "INSERT INTO wishlist (user_id, product_id) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Already exists or DB error
            return false;
        }
    }

    @Override
    public List<WishlistItem> findByUserId(Long userId) {
        String sql = "SELECT w.id AS wish_id, w.user_id, w.product_id, w.created_at, " +
                     "p.name AS prod_name, p.description AS prod_desc, p.price AS prod_price, " +
                     "p.stock_qty AS prod_stock, p.category AS prod_cat, p.image_url AS prod_img, p.seller_id " +
                     "FROM wishlist w JOIN products p ON w.product_id = p.id " +
                     "WHERE w.user_id = ? ORDER BY w.id DESC";

        List<WishlistItem> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WishlistItem item = new WishlistItem();
                    item.setId(rs.getLong("wish_id"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setCreatedAt(rs.getTimestamp("created_at"));

                    Product p = new Product();
                    p.setId(rs.getLong("product_id"));
                    p.setSellerId(rs.getLong("seller_id"));
                    p.setName(rs.getString("prod_name"));
                    p.setDescription(rs.getString("prod_desc"));
                    p.setPrice(rs.getBigDecimal("prod_price"));
                    p.setStockQty(rs.getInt("prod_stock"));
                    p.setCategory(rs.getString("prod_cat"));
                    p.setImageUrl(rs.getString("prod_img"));
                    item.setProduct(p);

                    list.add(item);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding wishlist items for user ID: " + userId, e);
        }
        return list;
    }

    @Override
    public boolean removeFromWishlist(Long userId, Long productId) {
        String sql = "DELETE FROM wishlist WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error removing item from wishlist", e);
        }
    }

    @Override
    public boolean isInWishlist(Long userId, Long productId) {
        String sql = "SELECT COUNT(*) FROM wishlist WHERE user_id = ? AND product_id = ?";
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
            throw new DatabaseException("Error checking wishlist status", e);
        }
        return false;
    }
}
