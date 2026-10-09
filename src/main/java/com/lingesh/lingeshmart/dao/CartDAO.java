package com.lingesh.lingeshmart.dao;

import com.lingesh.lingeshmart.model.CartItem;
import java.util.List;

public interface CartDAO {
    void addOrUpdateCartItem(Long userId, Long productId, int quantity);
    List<CartItem> findByUserId(Long userId);
    boolean updateQuantity(Long cartItemId, int quantity);
    boolean removeCartItem(Long cartItemId, Long userId);
    void clearCart(Long userId);
}
