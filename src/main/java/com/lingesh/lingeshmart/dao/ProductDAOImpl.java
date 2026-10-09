package com.lingesh.lingeshmart.dao;

import com.lingesh.lingeshmart.exception.DatabaseException;
import com.lingesh.lingeshmart.model.Product;
import com.lingesh.lingeshmart.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public Product create(Product product) {
        String sql = "INSERT INTO products (seller_id, name, description, price, stock_qty, category, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setLong(1, product.getSellerId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getPrice());
            ps.setInt(5, product.getStockQty());
            ps.setString(6, product.getCategory());
            ps.setString(7, product.getImageUrl());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    product.setId(rs.getLong(1));
                }
            }
            return product;
        } catch (SQLException e) {
            throw new DatabaseException("Error creating product: " + product.getName(), e);
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        String sql = "SELECT p.*, u.name AS seller_name FROM products p JOIN users u ON p.seller_id = u.id WHERE p.id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToProduct(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding product by ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT p.*, u.name AS seller_name FROM products p JOIN users u ON p.seller_id = u.id ORDER BY p.id DESC";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapRowToProduct(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all products", e);
        }
        return list;
    }

    @Override
    public List<Product> findByCategory(String category) {
        String sql = "SELECT p.*, u.name AS seller_name FROM products p JOIN users u ON p.seller_id = u.id WHERE p.category = ? ORDER BY p.id DESC";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToProduct(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding products by category: " + category, e);
        }
        return list;
    }

    @Override
    public List<Product> search(String keyword, String category) {
        StringBuilder sql = new StringBuilder("SELECT p.*, u.name AS seller_name FROM products p JOIN users u ON p.seller_id = u.id WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?)");
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            params.add(pattern);
            params.add(pattern);
        }

        if (category != null && !category.trim().isEmpty() && !"ALL".equalsIgnoreCase(category)) {
            sql.append(" AND p.category = ?");
            params.add(category.trim());
        }

        sql.append(" ORDER BY p.id DESC");

        List<Product> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToProduct(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error searching products", e);
        }
        return list;
    }

    @Override
    public List<Product> findBySellerId(Long sellerId) {
        String sql = "SELECT p.*, u.name AS seller_name FROM products p JOIN users u ON p.seller_id = u.id WHERE p.seller_id = ? ORDER BY p.id DESC";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToProduct(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding products by seller ID: " + sellerId, e);
        }
        return list;
    }

    @Override
    public boolean update(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock_qty = ?, category = ?, image_url = ? WHERE id = ? AND seller_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getStockQty());
            ps.setString(5, product.getCategory());
            ps.setString(6, product.getImageUrl());
            ps.setLong(7, product.getId());
            ps.setLong(8, product.getSellerId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating product ID: " + product.getId(), e);
        }
    }

    @Override
    public boolean updateStock(Long productId, int quantityChange) {
        String sql = "UPDATE products SET stock_qty = stock_qty + ? WHERE id = ? AND (stock_qty + ?) >= 0";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, quantityChange);
            ps.setLong(2, productId);
            ps.setInt(3, quantityChange);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating stock for product ID: " + productId, e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting product ID: " + id, e);
        }
    }

    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setSellerId(rs.getLong("seller_id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStockQty(rs.getInt("stock_qty"));
        p.setCategory(rs.getString("category"));
        p.setImageUrl(rs.getString("image_url"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            p.setSellerName(rs.getString("seller_name"));
        } catch (SQLException ignored) {}
        return p;
    }
}
