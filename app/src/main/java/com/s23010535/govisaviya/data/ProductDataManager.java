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
                // Fallback to sample data
                initializeSampleData();
                // Populate database with sample data
                databaseManager.initializeSampleData();
                Log.d("ProductDataManager", "Sample data initialized and saved to database");
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

        // Sample Direct Food Products
        Product tomato = new Product("Fresh Organic Tomatoes", "Freshly harvested organic tomatoes from our farm", 120.0, "direct_food", "seller001", "Farmer John");
        tomato.setSellerLocation("Colombo");
        tomato.setStockQuantity(50);
        tomato.setUnit("kg");
        tomato.setRating(4.5);
        tomato.setReviewCount(24);
        tomato.setFeatured(true);
        tomato.setCondition("Fresh");
        tomato.setDeliveryOptions("Pickup, Delivery");
        tomato.setDeliveryCost(50.0);
        tomato.setTags("organic, fresh, vegetables, tomatoes");
        allProducts.add(tomato);
        featuredProducts.add(tomato);

        Product rice = new Product("Red Rice", "Traditional red rice from Anuradhapura", 180.0, "direct_food", "seller002", "Rice Farmer");
        rice.setSellerLocation("Anuradhapura");
        rice.setStockQuantity(100);
        rice.setUnit("kg");
        rice.setRating(4.8);
        rice.setReviewCount(15);
        rice.setFeatured(true);
        rice.setCondition("Fresh");
        rice.setDeliveryOptions("Pickup, Delivery");
        rice.setDeliveryCost(100.0);
        rice.setTags("rice, traditional, red rice, grains");
        allProducts.add(rice);
        featuredProducts.add(rice);

        // Sample Pesticides
        Product pesticide = new Product("Organic Neem Oil", "Natural pest control solution", 850.0, "pesticides", "seller003", "Agro Supplies");
        pesticide.setSellerLocation("Kandy");
        pesticide.setStockQuantity(25);
        pesticide.setUnit("liter");
        pesticide.setRating(4.2);
        pesticide.setReviewCount(8);
        pesticide.setCondition("New");
        pesticide.setDeliveryOptions("Pickup, Delivery");
        pesticide.setDeliveryCost(75.0);
        pesticide.setTags("organic, neem, pest control, natural");
        allProducts.add(pesticide);

        // Sample Equipment Rental
        Product tractor = new Product("Tractor Rental", "Modern tractor for farming operations", 2500.0, "rental", "seller004", "Equipment Rentals");
        tractor.setSellerLocation("Galle");
        tractor.setStockQuantity(3);
        tractor.setUnit("day");
        tractor.setRating(4.6);
        tractor.setReviewCount(12);
        tractor.setFeatured(true);
        tractor.setCondition("Used");
        tractor.setDeliveryOptions("Pickup only");
        tractor.setDeliveryCost(0.0);
        tractor.setTags("tractor, rental, equipment, farming");
        allProducts.add(tractor);
        featuredProducts.add(tractor);

        // Sample Seeds
        Product seeds = new Product("Hybrid Corn Seeds", "High-yield hybrid corn seeds", 450.0, "seeds", "seller005", "Seed Company");
        seeds.setSellerLocation("Jaffna");
        seeds.setStockQuantity(200);
        seeds.setUnit("packet");
        seeds.setRating(4.4);
        seeds.setReviewCount(18);
        seeds.setCondition("New");
        seeds.setDeliveryOptions("Pickup, Delivery");
        seeds.setDeliveryCost(60.0);
        seeds.setTags("seeds, corn, hybrid, high yield");
        allProducts.add(seeds);

        // Sample Labor Services
        Product labor = new Product("Harvesting Services", "Professional harvesting team", 1500.0, "labor", "seller006", "Harvest Team");
        labor.setSellerLocation("Polonnaruwa");
        labor.setStockQuantity(10);
        labor.setUnit("day");
        labor.setRating(4.7);
        labor.setReviewCount(6);
        labor.setCondition("Service");
        labor.setDeliveryOptions("On-site service");
        labor.setDeliveryCost(0.0);
        labor.setTags("harvesting, labor, services, professional");
        allProducts.add(labor);

        // Sample Other Items
        Product fertilizer = new Product("Organic Compost", "Rich organic compost for better yields", 350.0, "others", "seller007", "Organic Farm");
        fertilizer.setSellerLocation("Matara");
        fertilizer.setStockQuantity(75);
        fertilizer.setUnit("bag");
        fertilizer.setRating(4.3);
        fertilizer.setReviewCount(11);
        fertilizer.setCondition("New");
        fertilizer.setDeliveryOptions("Pickup, Delivery");
        fertilizer.setDeliveryCost(80.0);
        fertilizer.setTags("compost, organic, fertilizer, soil");
        allProducts.add(fertilizer);

        Product tools = new Product("Garden Tool Set", "Complete set of essential garden tools", 1200.0, "others", "seller008", "Tool Shop");
        tools.setSellerLocation("Kurunegala");
        tools.setStockQuantity(15);
        tools.setUnit("set");
        tools.setRating(4.1);
        tools.setReviewCount(9);
        tools.setCondition("New");
        tools.setDeliveryOptions("Pickup, Delivery");
        tools.setDeliveryCost(100.0);
        tools.setTags("tools, garden, set, essential");
        allProducts.add(tools);

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
} 