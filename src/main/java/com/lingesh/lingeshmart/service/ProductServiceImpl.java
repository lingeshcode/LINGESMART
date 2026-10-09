package com.lingesh.lingeshmart.service;

import com.lingesh.lingeshmart.dao.ProductDAO;
import com.lingesh.lingeshmart.exception.ResourceNotFoundException;
import com.lingesh.lingeshmart.exception.ValidationException;
import com.lingesh.lingeshmart.model.Product;
import com.lingesh.lingeshmart.util.ValidationUtil;
import java.math.BigDecimal;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public Product createProduct(Long sellerId, String name, String description, BigDecimal price, Integer stockQty, String category, String imageUrl) {
        if (sellerId == null) {
            throw new ValidationException("Seller ID is required");
        }
        ValidationUtil.validateNotEmpty("Product Name", name);
        ValidationUtil.validatePositivePrice(price);
        ValidationUtil.validateNonNegativeQuantity(stockQty);
        ValidationUtil.validateNotEmpty("Category", category);

        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            imageUrl = "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600";
        }

        Product p = new Product();
        p.setSellerId(sellerId);
        p.setName(name.trim());
        p.setDescription(description != null ? description.trim() : "");
        p.setPrice(price);
        p.setStockQty(stockQty);
        p.setCategory(category.trim());
        p.setImageUrl(imageUrl.trim());

        return productDAO.create(p);
    }

    @Override
    public Product getProductById(Long id) {
        return productDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    @Override
    public List<Product> searchProducts(String keyword, String category) {
        return productDAO.search(keyword, category);
    }

    @Override
    public List<Product> getProductsBySeller(Long sellerId) {
        return productDAO.findBySellerId(sellerId);
    }

    @Override
    public boolean updateProduct(Long id, Long sellerId, String name, String description, BigDecimal price, Integer stockQty, String category, String imageUrl) {
        ValidationUtil.validateNotEmpty("Product Name", name);
        ValidationUtil.validatePositivePrice(price);
        ValidationUtil.validateNonNegativeQuantity(stockQty);
        ValidationUtil.validateNotEmpty("Category", category);

        Product p = getProductById(id);
        if (!p.getSellerId().equals(sellerId)) {
            throw new ValidationException("Seller does not own this product listing");
        }

        p.setName(name.trim());
        p.setDescription(description != null ? description.trim() : "");
        p.setPrice(price);
        p.setStockQty(stockQty);
        p.setCategory(category.trim());
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            p.setImageUrl(imageUrl.trim());
        }

        return productDAO.update(p);
    }

    @Override
    public boolean deleteProduct(Long id) {
        // Ensures product exists before deletion
        getProductById(id);
        return productDAO.delete(id);
    }
}
