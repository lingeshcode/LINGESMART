package com.lingesh.lingeshmart.service;

import com.lingesh.lingeshmart.model.Product;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    Product createProduct(Long sellerId, String name, String description, BigDecimal price, Integer stockQty, String category, String imageUrl);
    Product getProductById(Long id);
    List<Product> getAllProducts();
    List<Product> searchProducts(String keyword, String category);
    List<Product> getProductsBySeller(Long sellerId);
    boolean updateProduct(Long id, Long sellerId, String name, String description, BigDecimal price, Integer stockQty, String category, String imageUrl);
    boolean deleteProduct(Long id);
}
