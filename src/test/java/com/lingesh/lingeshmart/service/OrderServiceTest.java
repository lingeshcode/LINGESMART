package com.lingesh.lingeshmart.service;

import com.lingesh.lingeshmart.dao.CartDAO;
import com.lingesh.lingeshmart.dao.OrderDAO;
import com.lingesh.lingeshmart.dao.ProductDAO;
import com.lingesh.lingeshmart.exception.ValidationException;
import com.lingesh.lingeshmart.model.CartItem;
import com.lingesh.lingeshmart.model.Product;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private ProductDAO productDAO;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderDAO, cartDAO, productDAO);
    }

    @Test
    void testPlaceOrderEmptyCartThrowsValidationException() {
        when(cartDAO.findByUserId(1L)).thenReturn(Collections.emptyList());

        assertThrows(ValidationException.class, () -> orderService.placeOrderFromCart(1L));
    }

    @Test
    void testPlaceOrderInsufficientStockThrowsValidationException() {
        Product p = new Product();
        p.setId(10L);
        p.setName("Test Laptop");
        p.setPrice(new BigDecimal("50000.00"));
        p.setStockQty(1); // Only 1 in stock

        CartItem item = new CartItem(1L, 1L, 10L, 3); // Buyer wants 3
        item.setProduct(p);

        when(cartDAO.findByUserId(1L)).thenReturn(List.of(item));

        assertThrows(ValidationException.class, () -> orderService.placeOrderFromCart(1L));
    }
}
