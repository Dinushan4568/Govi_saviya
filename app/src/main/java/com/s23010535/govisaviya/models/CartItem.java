package com.s23010535.govisaviya.models;

import java.io.Serializable;
import java.util.Date;

public class CartItem implements Serializable {
    private int id;
    private int userId;
    private int productId;
    private int quantity;
    private Date createdAt;

    // Additional fields for better cart management
    private Product product; // For displaying product details

    // Default constructor
    public CartItem() {
        this.createdAt = new Date();
    }

    // Constructor with essential parameters
    public CartItem(int userId, int productId, int quantity) {
        this();
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    // Helper methods
    public double getSubtotal() {
        if (product != null) {
            return product.getPrice() * quantity;
        }
        return 0.0;
    }

    public String getFormattedSubtotal() {
        return String.format("LKR %.2f", getSubtotal());
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }

    public boolean isValidQuantity() {
        return quantity > 0 && (product == null || quantity <= product.getStockQuantity());
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
} 