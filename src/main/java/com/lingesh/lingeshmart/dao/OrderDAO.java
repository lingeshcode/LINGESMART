package com.lingesh.lingeshmart.dao;

import com.lingesh.lingeshmart.model.Order;
import com.lingesh.lingeshmart.model.OrderItem;
import java.util.List;
import java.util.Optional;

public interface OrderDAO {
    Order createOrder(Order order, List<OrderItem> items);
    Optional<Order> findById(Long id);
    List<Order> findByBuyerId(Long buyerId);
    List<Order> findIncomingOrdersForSeller(Long sellerId);
    List<Order> findAll();
    boolean updateStatus(Long orderId, Order.Status newStatus);
}
