package com.lingesh.lingeshmart.dao;

import com.lingesh.lingeshmart.exception.DatabaseException;
import com.lingesh.lingeshmart.model.CartItem;
import com.lingesh.lingeshmart.model.Product;
import com.lingesh.lingeshmart.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDAOImpl implements CartDAO {

    @Override
    public void addOrUpdateCartItem(Long userId, Long productId, int quantity) {
        String sql = "MERGE INTO cart_items (user_id, product_id, quantity) KEY (user_id, product_id) VALUES (?, ?, COALESCE((SELECT quantity FROM cart_items WHERE user_id = ? AND product_id = ?), 0) + ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ps.setLong(3, userId);
            ps.setLong(4, productId);
            ps.setInt(5, quantity);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding/updating cart item", e);
        }
    }

    @Override
    public List<CartItem> findByUserId(Long userId) {
        String sql = "SELECT c.id AS cart_id, c.user_id, c.product_id, c.quantity, " +
                     "p.name AS prod_name, p.description AS prod_desc, p.price AS prod_price, " +
                     "p.stock_qty AS prod_stock, p.category AS prod_cat, p.image_url AS prod_img, p.seller_id " +
                     "FROM cart_items c JOIN products p ON c.product_id = p.id " +
                     "WHERE c.user_id = ? ORDER BY c.id ASC";

        List<CartItem> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getLong("cart_id"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));

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
            throw new DatabaseException("Error finding cart items for user ID: " + userId, e);
        }
        return list;
    }

    @Override
    public boolean updateQuantity(Long cartItemId, int quantity) {
        if (quantity <= 0) {
            String deleteSql = "DELETE FROM cart_items WHERE id = ?";
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(deleteSql)) {
                ps.setLong(1, cartItemId);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DatabaseException("Error removing cart item ID: " + cartItemId, e);
            }
        }

        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, quantity);
            ps.setLong(2, cartItemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating cart item quantity", e);
        }
    }

    @Override
    public boolean removeCartItem(Long cartItemId, Long userId) {
        String sql = "DELETE FROM cart_items WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, cartItemId);
            ps.setLong(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error removing cart item ID: " + cartItemId, e);
        }
    }

    @Override
    public void clearCart(Long userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error clearing cart for user ID: " + userId, e);
        }
    }
}
