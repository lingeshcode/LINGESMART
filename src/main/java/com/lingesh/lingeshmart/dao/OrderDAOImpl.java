package com.lingesh.lingeshmart.dao;

import com.lingesh.lingeshmart.exception.DatabaseException;
import com.lingesh.lingeshmart.model.Order;
import com.lingesh.lingeshmart.model.OrderItem;
import com.lingesh.lingeshmart.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public Order createOrder(Order order, List<OrderItem> items) {
        String insertOrderSql = "INSERT INTO orders (buyer_id, status, total_amount) VALUES (?, ?, ?)";
        String insertItemSql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        String updateStockSql = "UPDATE products SET stock_qty = stock_qty - ? WHERE id = ? AND stock_qty >= ?";

        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false); // Transaction start

            // 1. Insert Order Header
            try (PreparedStatement ps = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, order.getBuyerId());
                ps.setString(2, order.getStatus().name());
                ps.setBigDecimal(3, order.getTotalAmount());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        order.setId(rs.getLong(1));
                    }
                }
            }

            // 2. Insert Order Items & Deduct Stock
            try (PreparedStatement psItem = conn.prepareStatement(insertItemSql);
                 PreparedStatement psStock = conn.prepareStatement(updateStockSql)) {
                
                for (OrderItem item : items) {
                    item.setOrderId(order.getId());
                    psItem.setLong(1, item.getOrderId());
                    psItem.setLong(2, item.getProductId());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setBigDecimal(4, item.getUnitPrice());
                    psItem.addBatch();

                    psStock.setInt(1, item.getQuantity());
                    psStock.setLong(2, item.getProductId());
                    psStock.setInt(3, item.getQuantity());
                    int updatedRows = psStock.executeUpdate();
                    if (updatedRows == 0) {
                        throw new SQLException("Insufficient stock for product ID: " + item.getProductId());
                    }
                }
                psItem.executeBatch();
            }

            conn.commit(); // Transaction commit
            order.setItems(items);
            return order;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    // Ignored
                }
            }
            throw new DatabaseException("Failed to place order: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    // Ignored
                }
            }
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        String orderSql = "SELECT o.*, u.name AS buyer_name FROM orders o JOIN users u ON o.buyer_id = u.id WHERE o.id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(orderSql)) {
            
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapRowToOrder(rs);
                    order.setItems(findItemsByOrderId(conn, id));
                    return Optional.of(order);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding order by ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findByBuyerId(Long buyerId) {
        String sql = "SELECT o.*, u.name AS buyer_name FROM orders o JOIN users u ON o.buyer_id = u.id WHERE o.buyer_id = ? ORDER BY o.id DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapRowToOrder(rs);
                    order.setItems(findItemsByOrderId(conn, order.getId()));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding orders for buyer ID: " + buyerId, e);
        }
        return list;
    }

    @Override
    public List<Order> findIncomingOrdersForSeller(Long sellerId) {
        String sql = "SELECT DISTINCT o.*, u.name AS buyer_name FROM orders o " +
                "JOIN order_items oi ON o.id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.id " +
                "JOIN users u ON o.buyer_id = u.id " +
                "WHERE p.seller_id = ? ORDER BY o.id DESC";
        
        List<Order> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapRowToOrder(rs);
                    order.setItems(findItemsByOrderIdForSeller(conn, order.getId(), sellerId));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding incoming orders for seller ID: " + sellerId, e);
        }
        return list;
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT o.*, u.name AS buyer_name FROM orders o JOIN users u ON o.buyer_id = u.id ORDER BY o.id DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Order order = mapRowToOrder(rs);
                order.setItems(findItemsByOrderId(conn, order.getId()));
                list.add(order);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all orders", e);
        }
        return list;
    }

    @Override
    public boolean updateStatus(Long orderId, Order.Status newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newStatus.name());
            ps.setLong(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating status for order ID: " + orderId, e);
        }
    }

    private List<OrderItem> findItemsByOrderId(Connection conn, Long orderId) throws SQLException {
        String sql = "SELECT oi.*, p.name AS product_name, p.image_url FROM order_items oi JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?";
        List<OrderItem> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToOrderItem(rs));
                }
            }
        }
        return list;
    }

    private List<OrderItem> findItemsByOrderIdForSeller(Connection conn, Long orderId, Long sellerId) throws SQLException {
        String sql = "SELECT oi.*, p.name AS product_name, p.image_url FROM order_items oi JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ? AND p.seller_id = ?";
        List<OrderItem> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setLong(2, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToOrderItem(rs));
                }
            }
        }
        return list;
    }

    private Order mapRowToOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setBuyerId(rs.getLong("buyer_id"));
        o.setStatus(Order.Status.valueOf(rs.getString("status")));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            o.setBuyerName(rs.getString("buyer_name"));
        } catch (SQLException ignored) {}
        return o;
    }

    private OrderItem mapRowToOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setProductId(rs.getLong("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setProductName(rs.getString("product_name"));
        item.setProductImageUrl(rs.getString("image_url"));
        return item;
    }
}
