package com.s23010535.govisaviya.data;

import android.content.Context;
import android.util.Log;

import com.s23010535.govisaviya.database.DatabaseManager;
import com.s23010535.govisaviya.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * ProductDataManager - Manages product data with database integration
 * This class provides a bridge between the UI and the database system
 */
public class ProductDataManager {
    
    private static ProductDataManager instance;
    private DatabaseManager databaseManager;
    private List<Product> allProducts;
    private List<Product> featuredProducts;
    private boolean isInitialized = false;

    private ProductDataManager(Context context) {
        this.databaseManager = DatabaseManager.getInstance(context);
        initializeData();
    }

    public static ProductDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProductDataManager(context);
        }
        return instance;
    }

    /**
     * Initialize data from database or fallback to sample data
     */
    private void initializeData() {
        try {
            // Try to get data from database first
            List<Product> dbProducts = databaseManager.getAllProducts();
            if (dbProducts != null && !dbProducts.isEmpty()) {
                allProducts = dbProducts;
                featuredProducts = databaseManager.getFeaturedProducts();
                if (featuredProducts == null) {
                    featuredProducts = new ArrayList<>();
                }
                isInitialized = true;
                Log.d("ProductDataManager", "Data loaded from database: " + allProducts.size() + " products");
            } else {
                // Fallback to empty data
                initializeSampleData();
                Log.d("ProductDataManager", "Empty data initialized");
            }
        } catch (Exception e) {
            Log.e("ProductDataManager", "Error initializing data: " + e.getMessage());
            // Fallback to sample data
            initializeSampleData();
        }
    }

    /**
     * Initialize sample data (fallback method)
     */
    private void initializeSampleData() {
        allProducts = new ArrayList<>();
        featuredProducts = new ArrayList<>();
        isInitialized = true;
    }

    /**
     * Refresh data from database
     */
    public void refreshData() {
        try {
            List<Product> dbProducts = databaseManager.getAllProducts();
            if (dbProducts != null) {
                allProducts = dbProducts;
                featuredProducts = databaseManager.getFeaturedProducts();
                if (featuredProducts == null) {
                    featuredProducts = new ArrayList<>();
                }
                Log.d("ProductDataManager", "Data refreshed from database: " + allProducts.size() + " products");
            }
        } catch (Exception e) {
            Log.e("ProductDataManager", "Error refreshing data: " + e.getMessage());
        }
    }

    // Get all products
    public List<Product> getAllProducts() {
        if (!isInitialized) {
            refreshData();
        }
        return new ArrayList<>(allProducts);
    }

    // Get products by category
    public List<Product> getProductsByCategory(String category) {
        try {
            // Try database first
            List<Product> categoryProducts = databaseManager.getProductsByCategory(category);
            if (categoryProducts != null) {
                return categoryProducts;
            }
        } catch (Exception e) {
            Log.e("ProductDataManager", "Error getting products by category from database: " + e.getMessage());
        }

        // Fallback to local filtering
        List<Product> categoryProducts = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                categoryProducts.add(product);
            }
        }
        return categoryProducts;
    }

    // Add new product (with database integration)
    public boolean addProduct(Product product) {
        try {
            // Add to database
            long productId = databaseManager.addProduct(product);
            if (productId > 0) {
                product.setId((int) productId);
                
                // Add to local lists
                allProducts.add(product);
                if (product.isFeatured()) {
                    featuredProducts.add(product);
                }
                
                Log.d("ProductDataManager", "Product added successfully: " + product.getName());
                return true;
            }
        } catch (Exception e) {
            Log.e("ProductDataManager", "Error adding product to database: " + e.getMessage());
        }

        // Fallback to local only
        product.setId(allProducts.size() + 1);
        allProducts.add(product);
        if (product.isFeatured()) {
            featuredProducts.add(product);
        }
        return false;
    }

    // Update existing product
    public boolean updateProduct(Product product) {
        try {
            // Update in database
            boolean dbSuccess = databaseManager.updateProduct(product);
            if (dbSuccess) {
                // Update in local lists
                for (int i = 0; i < allProducts.size(); i++) {
                    if (allProducts.get(i).getId() == product.getId()) {
                        allProducts.set(i, product);
                        break;
                    }
                }
                
                // Update in featured products if needed
                if (product.isFeatured()) {
                    boolean foundInFeatured = false;
                    for (int i = 0; i < featuredProducts.size(); i++) {
                        if (featuredProducts.get(i).getId() == product.getId()) {
                            featuredProducts.set(i, product);
                            foundInFeatured = true;
                            break;
                        }
                    }
                    if (!foundInFeatured) {
                        featuredProducts.add(product);
                    }
                } else {
                    // Remove from featured if not featured anymore
                    featuredProducts.removeIf(p -> p.getId() == product.getId());
                }
                
                Log.d("ProductDataManager", "Product updated successfully: " + product.getName());
                return true;
            }
        } catch (Exception e) {
            Log.e("ProductDataManager", "Error updating product in database: " + e.getMessage());
        }
        return false;
    }

    // Delete product
    public boolean deleteProduct(int productId) {
        try {
            // Delete from database
            boolean dbSuccess = databaseManager.deleteProduct(productId);
            if (dbSuccess) {
                // Remove from local lists
                allProducts.removeIf(p -> p.getId() == productId);
                featuredProducts.removeIf(p -> p.getId() == productId);
                
                Log.d("ProductDataManager", "Product deleted successfully: ID " + productId);
                return true;
            }
        } catch (Exception e) {
            Log.e("ProductDataManager", "Error deleting product from database: " + e.getMessage());
        }
        return false;
    }

    // Get product by ID
    public Product getProductById(int productId) {
        try {
            // Try database first
            Product dbProduct = databaseManager.getProductById(productId);
            if (dbProduct != null) {
                return dbProduct;
            }
        } catch (Exception e) {
            Log.e("ProductDataManager", "Error getting product by ID from database: " + e.getMessage());
        }

        // Fallback to local search
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    // Get products by seller
    public List<Product> getProductsBySeller(String sellerId) {
        try {
            // Try database first
            List<Product> sellerProducts = databaseManager.getProductsBySeller(sellerId);
            if (sellerProducts != null) {
                return sellerProducts;
            }
        } catch (Exception e) {
            Log.e("ProductDataManager", "Error getting products by seller from database: " + e.getMessage());
        }

        // Fallback to local filtering
        List<Product> sellerProducts = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getSellerId().equals(sellerId)) {
                sellerProducts.add(product);
            }
        }
        return sellerProducts;
    }

    // Search products
    public List<Product> searchProducts(String query) {
        List<Product> searchResults = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(lowerQuery) ||
                product.getDescription().toLowerCase().contains(lowerQuery) ||
                (product.getTags() != null && product.getTags().toLowerCase().contains(lowerQuery))) {
                searchResults.add(product);
            }
        }
        return searchResults;
    }
} 