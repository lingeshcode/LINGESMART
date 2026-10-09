package com.lingesh.lingeshmart.service;

import com.lingesh.lingeshmart.dao.WishlistDAO;
import com.lingesh.lingeshmart.exception.ValidationException;
import com.lingesh.lingeshmart.model.WishlistItem;
import java.util.List;

public class WishlistServiceImpl implements WishlistService {

    private final WishlistDAO wishlistDAO;

    public WishlistServiceImpl(WishlistDAO wishlistDAO) {
        this.wishlistDAO = wishlistDAO;
    }

    @Override
    public boolean addToWishlist(Long userId, Long productId) {
        if (userId == null || productId == null) {
            throw new ValidationException("User ID and Product ID are required");
        }
        return wishlistDAO.addToWishlist(userId, productId);
    }

    @Override
    public List<WishlistItem> getUserWishlist(Long userId) {
        return wishlistDAO.findByUserId(userId);
    }

    @Override
    public boolean removeFromWishlist(Long userId, Long productId) {
        return wishlistDAO.removeFromWishlist(userId, productId);
    }

    @Override
    public boolean isItemInWishlist(Long userId, Long productId) {
        return wishlistDAO.isInWishlist(userId, productId);
    }
}
