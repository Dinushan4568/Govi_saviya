package com.s23010535.govisaviya.data;

import com.s23010535.govisaviya.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDataManager {
    
    private static ProductDataManager instance;
    private List<Product> allProducts;
    private List<Product> featuredProducts;

    private ProductDataManager() {
        initializeSampleData();
    }

    public static ProductDataManager getInstance() {
        if (instance == null) {
            instance = new ProductDataManager();
        }
        return instance;
    }

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
    }

    // Get all products
    public List<Product> getAllProducts() {
        return new ArrayList<>(allProducts);
    }

    // Get featured products
    public List<Product> getFeaturedProducts() {
        return new ArrayList<>(featuredProducts);
    }

    // Get products by category
    public List<Product> getProductsByCategory(String category) {
        List<Product> categoryProducts = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                categoryProducts.add(product);
            }
        }
        return categoryProducts;
    }

    // Search products
    public List<Product> searchProducts(String query) {
        List<Product> searchResults = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(lowerQuery) ||
                product.getDescription().toLowerCase().contains(lowerQuery) ||
                product.getSellerName().toLowerCase().contains(lowerQuery) ||
                (product.getTags() != null && product.getTags().toLowerCase().contains(lowerQuery))) {
                searchResults.add(product);
            }
        }
        return searchResults;
    }

    // Get product by ID
    public Product getProductById(int id) {
        for (Product product : allProducts) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    // Add new product (for future database integration)
    public void addProduct(Product product) {
        product.setId(allProducts.size() + 1); // Simple ID generation
        allProducts.add(product);
        if (product.isFeatured()) {
            featuredProducts.add(product);
        }
    }

    // Update product (for future database integration)
    public void updateProduct(Product updatedProduct) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == updatedProduct.getId()) {
                allProducts.set(i, updatedProduct);
                break;
            }
        }
    }

    // Delete product (for future database integration)
    public void deleteProduct(int productId) {
        allProducts.removeIf(product -> product.getId() == productId);
        featuredProducts.removeIf(product -> product.getId() == productId);
    }

    // Get products by price range
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) {
        List<Product> priceFilteredProducts = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getPrice() >= minPrice && product.getPrice() <= maxPrice) {
                priceFilteredProducts.add(product);
            }
        }
        return priceFilteredProducts;
    }

    // Get products by location
    public List<Product> getProductsByLocation(String location) {
        List<Product> locationFilteredProducts = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getSellerLocation() != null && 
                product.getSellerLocation().toLowerCase().contains(location.toLowerCase())) {
                locationFilteredProducts.add(product);
            }
        }
        return locationFilteredProducts;
    }
} 