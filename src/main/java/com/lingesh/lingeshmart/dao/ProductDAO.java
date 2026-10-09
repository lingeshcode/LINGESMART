package com.lingesh.lingeshmart.dao;

import com.lingesh.lingeshmart.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    Product create(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    List<Product> findByCategory(String category);
    List<Product> search(String keyword, String category);
    List<Product> findBySellerId(Long sellerId);
    boolean update(Product product);
    boolean updateStock(Long productId, int quantityChange);
    boolean delete(Long id);
}
