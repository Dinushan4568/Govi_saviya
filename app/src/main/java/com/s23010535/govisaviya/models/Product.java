package com.s23010535.govisaviya.models;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable {
    private int id;
    private String name;
    private String description;
    private double price;
    private String currency;
    private String category;
    private String subCategory;
    private String sellerId;
    private String sellerName;
    private String sellerLocation;
    private String imageUrl;
    private int stockQuantity;
    private String unit; // kg, piece, day, etc.
    private double rating;
    private int reviewCount;
    private boolean isAvailable;
    private boolean isFeatured;
    private Date createdAt;
    private Date updatedAt;
    private String condition; // new, used, etc.
    private String deliveryOptions; // pickup, delivery, both
    private double deliveryCost;
    private String tags; // comma separated tags for search

    // Default constructor
    public Product() {
        this.currency = "LKR";
        this.unit = "piece";
        this.isAvailable = true;
        this.isFeatured = false;
        this.rating = 0.0;
        this.reviewCount = 0;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Constructor with essential parameters
    public Product(String name, String description, double price, String category, String sellerId, String sellerName) {
        this();
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubCategory() { return subCategory; }
    public void setSubCategory(String subCategory) { this.subCategory = subCategory; }

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public String getSellerLocation() { return sellerLocation; }
    public void setSellerLocation(String sellerLocation) { this.sellerLocation = sellerLocation; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public boolean isFeatured() { return isFeatured; }
    public void setFeatured(boolean featured) { isFeatured = featured; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getDeliveryOptions() { return deliveryOptions; }
    public void setDeliveryOptions(String deliveryOptions) { this.deliveryOptions = deliveryOptions; }

    public double getDeliveryCost() { return deliveryCost; }
    public void setDeliveryCost(double deliveryCost) { this.deliveryCost = deliveryCost; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    // Helper methods
    public String getFormattedPrice() {
        return String.format("%s %.2f", currency, price);
    }

    public String getFormattedRating() {
        return String.format("%.1f (%d)", rating, reviewCount);
    }

    public String getStockStatus() {
        if (stockQuantity <= 0) {
            return "Out of Stock";
        } else if (stockQuantity < 10) {
            return "Low Stock";
        } else {
            return "In Stock";
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", sellerName='" + sellerName + '\'' +
                '}';
    }
} 