package com.lingesh.lingeshmart.dto;

import java.math.BigDecimal;

public class SellerDashboardDTO {

    private int totalProducts;
    private int totalOrders;
    private BigDecimal totalRevenue;
    private int pendingOrdersCount;

    public SellerDashboardDTO() {}

    public SellerDashboardDTO(int totalProducts, int totalOrders, BigDecimal totalRevenue, int pendingOrdersCount) {
        this.totalProducts = totalProducts;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.pendingOrdersCount = pendingOrdersCount;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getPendingOrdersCount() {
        return pendingOrdersCount;
    }

    public void setPendingOrdersCount(int pendingOrdersCount) {
        this.pendingOrdersCount = pendingOrdersCount;
    }
}
