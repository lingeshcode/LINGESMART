package com.lingesh.lingeshmart.service;

import com.lingesh.lingeshmart.dao.CartDAO;
import com.lingesh.lingeshmart.dao.OrderDAO;
import com.lingesh.lingeshmart.dao.ProductDAO;
import com.lingesh.lingeshmart.dto.SellerDashboardDTO;
import com.lingesh.lingeshmart.exception.ResourceNotFoundException;
import com.lingesh.lingeshmart.exception.ValidationException;
import com.lingesh.lingeshmart.model.CartItem;
import com.lingesh.lingeshmart.model.Order;
import com.lingesh.lingeshmart.model.OrderItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public OrderServiceImpl(OrderDAO orderDAO, CartDAO cartDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    @Override
    public Order placeOrderFromCart(Long buyerId) {
        List<CartItem> cartItems = cartDAO.findByUserId(buyerId);
        if (cartItems.isEmpty()) {
            throw new ValidationException("Cannot place order with an empty shopping cart!");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem ci : cartItems) {
            if (ci.getProduct() == null) continue;

            if (ci.getProduct().getStockQty() < ci.getQuantity()) {
                throw new ValidationException("Product '" + ci.getProduct().getName() + "' does not have sufficient stock (" + ci.getProduct().getStockQty() + " remaining).");
            }

            BigDecimal itemPrice = ci.getProduct().getPrice();
            totalAmount = totalAmount.add(itemPrice.multiply(BigDecimal.valueOf(ci.getQuantity())));

            OrderItem oi = new OrderItem();
            oi.setProductId(ci.getProductId());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(itemPrice);
            orderItems.add(oi);
        }

        Order order = new Order();
        order.setBuyerId(buyerId);
        order.setStatus(Order.Status.CONFIRMED);
        order.setTotalAmount(totalAmount);

        Order createdOrder = orderDAO.createOrder(order, orderItems);

        // Clear cart after checkout
        cartDAO.clearCart(buyerId);

        return createdOrder;
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderDAO.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
    }

    @Override
    public List<Order> getBuyerOrders(Long buyerId) {
        return orderDAO.findByBuyerId(buyerId);
    }

    @Override
    public List<Order> getSellerIncomingOrders(Long sellerId) {
        return orderDAO.findIncomingOrdersForSeller(sellerId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    @Override
    public boolean updateOrderStatus(Long orderId, Order.Status status) {
        getOrderById(orderId); // Validate existence
        return orderDAO.updateStatus(orderId, status);
    }

    @Override
    public SellerDashboardDTO getSellerDashboardMetrics(Long sellerId) {
        int totalProducts = productDAO.findBySellerId(sellerId).size();
        List<Order> incomingOrders = orderDAO.findIncomingOrdersForSeller(sellerId);
        int totalOrders = incomingOrders.size();

        int pendingCount = 0;
        BigDecimal revenue = BigDecimal.ZERO;

        for (Order o : incomingOrders) {
            if (o.getStatus() == Order.Status.PENDING) {
                pendingCount++;
            }
            if (o.getStatus() != Order.Status.CANCELLED) {
                for (OrderItem item : o.getItems()) {
                    revenue = revenue.add(item.getSubtotal());
                }
            }
        }

        return new SellerDashboardDTO(totalProducts, totalOrders, revenue, pendingCount);
    }
}
