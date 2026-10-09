package com.lingesh.lingeshmart.dao;

import com.lingesh.lingeshmart.model.WishlistItem;
import java.util.List;

public interface WishlistDAO {
    boolean addToWishlist(Long userId, Long productId);
    List<WishlistItem> findByUserId(Long userId);
    boolean removeFromWishlist(Long userId, Long productId);
    boolean isInWishlist(Long userId, Long productId);
}
