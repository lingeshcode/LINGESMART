package com.lingesh.lingeshmart.service;

import com.lingesh.lingeshmart.model.WishlistItem;
import java.util.List;

public interface WishlistService {
    boolean addToWishlist(Long userId, Long productId);
    List<WishlistItem> getUserWishlist(Long userId);
    boolean removeFromWishlist(Long userId, Long productId);
    boolean isItemInWishlist(Long userId, Long productId);
}
