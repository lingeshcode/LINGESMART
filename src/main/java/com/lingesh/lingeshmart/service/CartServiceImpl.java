package com.lingesh.lingeshmart.service;

import com.lingesh.lingeshmart.dao.CartDAO;
import com.lingesh.lingeshmart.dao.ProductDAO;
import com.lingesh.lingeshmart.exception.ResourceNotFoundException;
import com.lingesh.lingeshmart.exception.ValidationException;
import com.lingesh.lingeshmart.model.CartItem;
import com.lingesh.lingeshmart.model.Product;
import java.util.List;

public class CartServiceImpl implements CartService {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartServiceImpl(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    @Override
    public void addToCart(Long userId, Long productId, int quantity) {
        if (userId == null || productId == null) {
            throw new ValidationException("User ID and Product ID are required");
        }
        if (quantity <= 0) {
            throw new ValidationException("Quantity must be greater than zero");
        }

        Product product = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (product.getStockQty() < quantity) {
            throw new ValidationException("Only " + product.getStockQty() + " items available in stock");
        }

        cartDAO.addOrUpdateCartItem(userId, productId, quantity);
    }

    @Override
    public List<CartItem> getUserCart(Long userId) {
        return cartDAO.findByUserId(userId);
    }

    @Override
    public boolean updateItemQuantity(Long cartItemId, int quantity) {
        return cartDAO.updateQuantity(cartItemId, quantity);
    }

    @Override
    public boolean removeItemFromCart(Long cartItemId, Long userId) {
        return cartDAO.removeCartItem(cartItemId, userId);
    }

    @Override
    public void clearUserCart(Long userId) {
        cartDAO.clearCart(userId);
    }
}
