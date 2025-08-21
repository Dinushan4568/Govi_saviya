package com.s23010535.govisaviya.database;

import android.content.Context;
import android.util.Log;

import com.s23010535.govisaviya.models.CartItem;
import com.s23010535.govisaviya.models.Order;
import com.s23010535.govisaviya.models.Product;
import com.s23010535.govisaviya.models.User;

import java.util.List;

/**
 * Database Manager class that provides a high-level interface for all database operations
 * This class acts as a facade for the DatabaseHelper and provides business logic
 */
public class DatabaseManager {
    
    private static DatabaseManager instance;
    private DatabaseHelper databaseHelper;
    private Context context;

    private DatabaseManager(Context context) {
        this.context = context.getApplicationContext();
        this.databaseHelper = new DatabaseHelper(this.context);
    }

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    // ============================================================================
    // PRODUCT OPERATIONS
    // ============================================================================

    /**
     * Add a new product to the database
     */
    public long addProduct(Product product) {
        try {
            return databaseHelper.addProduct(product);
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error adding product: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Get a product by ID
     */
    public Product getProduct(int id) {
        try {
            return databaseHelper.getProduct(id);
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting product: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get a product by ID (alias for getProduct)
     */
    public Product getProductById(int productId) {
        return getProduct(productId);
    }

    /**
     * Get products by seller
     */
    public List<Product> getProductsBySeller(String sellerId) {
        try {
            return databaseHelper.getProductsBySeller(sellerId);
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting products by seller: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get all products
     */
    public List<Product> getAllProducts() {
        try {
            return databaseHelper.getAllProducts();
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting all products: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get featured products
     */
    public List<Product> getFeaturedProducts() {
        try {
            return databaseHelper.getFeaturedProducts();
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting featured products: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get products by category
     */
    public List<Product> getProductsByCategory(String category) {
        try {
            return databaseHelper.getProductsByCategory(category);
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting products by category: " + e.getMessage());
            return null;
        }
    }

    /**
     * Search products
     */
    public List<Product> searchProducts(String query) {
        try {
            return databaseHelper.searchProducts(query);
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error searching products: " + e.getMessage());
            return null;
        }
    }

    /**
     * Update a product
     */
    public boolean updateProduct(Product product) {
        try {
            int result = databaseHelper.updateProduct(product);
            return result > 0;
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error updating product: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a product
     */
    public boolean deleteProduct(int productId) {
        try {
            databaseHelper.deleteProduct(productId);
            return true;
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error deleting product: " + e.getMessage());
            return false;
        }
    }


    /**
     * Add a new user to the database
     */
    public long addUser(User user) {
        try {
            return databaseHelper.addUser(user);
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error adding user: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Get a user by ID
     */
    public User getUser(int id) {
        try {
            return databaseHelper.getUser(id);
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting user: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get a user by username
     */
    public User getUserByUsername(String username) {
        try {
            return databaseHelper.getUserByUsername(username);
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting user by username: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get a user by email
     */
    public User getUserByEmail(String email) {
        try {
            return databaseHelper.getUserByEmail(email);
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting user by email: " + e.getMessage());
            return null;
        }
    }

    /**
     * Update a user
     */
    public boolean updateUser(User user) {
        try {
            int result = databaseHelper.updateUser(user);
            return result > 0;
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error updating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Authenticate user login
     */
    public User authenticateUser(String username, String passwordHash) {
        try {
            User user = databaseHelper.getUserByUsername(username);
            if (user != null && user.getPasswordHash().equals(passwordHash)) {
                return user;
            }
            return null;
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error authenticating user: " + e.getMessage());
            return null;
        }
    }

    /**
     * Add item to cart
     */
    public long addToCart(CartItem cartItem) {
        try {
            return databaseHelper.addToCart(cartItem);
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error adding to cart: " + e.getMessage());
            return -1;
        }
    }

} 