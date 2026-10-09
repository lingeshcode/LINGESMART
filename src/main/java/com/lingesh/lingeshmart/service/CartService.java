package com.lingesh.lingeshmart.service;

import com.lingesh.lingeshmart.model.CartItem;
import java.util.List;

public interface CartService {
    void addToCart(Long userId, Long productId, int quantity);
    List<CartItem> getUserCart(Long userId);
    boolean updateItemQuantity(Long cartItemId, int quantity);
    boolean removeItemFromCart(Long cartItemId, Long userId);
    void clearUserCart(Long userId);
}
