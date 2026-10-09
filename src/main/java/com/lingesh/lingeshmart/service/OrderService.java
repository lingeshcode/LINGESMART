package com.lingesh.lingeshmart.service;

import com.lingesh.lingeshmart.dto.SellerDashboardDTO;
import com.lingesh.lingeshmart.model.Order;
import java.util.List;

public interface OrderService {
    Order placeOrderFromCart(Long buyerId);
    Order getOrderById(Long orderId);
    List<Order> getBuyerOrders(Long buyerId);
    List<Order> getSellerIncomingOrders(Long sellerId);
    List<Order> getAllOrders();
    boolean updateOrderStatus(Long orderId, Order.Status status);
    SellerDashboardDTO getSellerDashboardMetrics(Long sellerId);
}
