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

    // ============================================================================
    // USER OPERATIONS
    // ============================================================================

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
     * Get all users
     */
    public List<User> getAllUsers() {
        try {
            return databaseHelper.getAllUsers();
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting all users: " + e.getMessage());
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
     * Delete a user
     */
    public boolean deleteUser(int userId) {
        try {
            databaseHelper.deleteUser(userId);
            return true;
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error deleting user: " + e.getMessage());
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

    // ============================================================================
    // ORDER OPERATIONS
    // ============================================================================

    /**
     * Add a new order to the database
     */
    public long addOrder(Order order) {
        try {
            return databaseHelper.addOrder(order);
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error adding order: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Get an order by ID
     */
    public Order getOrder(int id) {
        try {
            return databaseHelper.getOrder(id);
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting order: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get orders by user ID
     */
    public List<Order> getOrdersByUser(int userId) {
        try {
            return databaseHelper.getOrdersByUser(userId);
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting orders by user: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get all orders
     */
    public List<Order> getAllOrders() {
        try {
            return databaseHelper.getAllOrders();
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting all orders: " + e.getMessage());
            return null;
        }
    }

    /**
     * Update order status
     */
    public boolean updateOrderStatus(int orderId, String status) {
        try {
            int result = databaseHelper.updateOrderStatus(orderId, status);
            return result > 0;
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error updating order status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete an order
     */
    public boolean deleteOrder(int orderId) {
        try {
            databaseHelper.deleteOrder(orderId);
            return true;
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error deleting order: " + e.getMessage());
            return false;
        }
    }

    // ============================================================================
    // CART OPERATIONS
    // ============================================================================

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

    /**
     * Get cart items for a user
     */
    public List<CartItem> getCartItems(int userId) {
        try {
            return databaseHelper.getCartItems(userId);
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting cart items: " + e.getMessage());
            return null;
        }
    }

    /**
     * Update cart item quantity
     */
    public boolean updateCartItemQuantity(int cartItemId, int quantity) {
        try {
            int result = databaseHelper.updateCartItemQuantity(cartItemId, quantity);
            return result > 0;
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error updating cart item quantity: " + e.getMessage());
            return false;
        }
    }

    /**
     * Remove item from cart
     */
    public boolean removeFromCart(int cartItemId) {
        try {
            databaseHelper.removeFromCart(cartItemId);
            return true;
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error removing from cart: " + e.getMessage());
            return false;
        }
    }

    /**
     * Clear user's cart
     */
    public boolean clearUserCart(int userId) {
        try {
            databaseHelper.clearUserCart(userId);
            return true;
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error clearing user cart: " + e.getMessage());
            return false;
        }
    }

    // ============================================================================
    // BUSINESS LOGIC METHODS
    // ============================================================================

    /**
     * Create an order from cart items
     */
    public boolean createOrderFromCart(int userId, String deliveryAddress, String paymentMethod) {
        try {
            List<CartItem> cartItems = getCartItems(userId);
            if (cartItems == null || cartItems.isEmpty()) {
                return false;
            }

            boolean success = true;
            for (CartItem cartItem : cartItems) {
                Product product = getProduct(cartItem.getProductId());
                if (product != null && product.getStockQuantity() >= cartItem.getQuantity()) {
                    // Create order
                    Order order = new Order(userId, cartItem.getProductId(), cartItem.getQuantity(), 
                            product.getPrice() * cartItem.getQuantity());
                    order.setDeliveryAddress(deliveryAddress);
                    order.setPaymentMethod(paymentMethod);
                    
                    long orderId = addOrder(order);
                    if (orderId > 0) {
                        // Update product stock
                        product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
                        updateProduct(product);
                    } else {
                        success = false;
                    }
                } else {
                    success = false;
                }
            }

            if (success) {
                clearUserCart(userId);
            }

            return success;
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error creating order from cart: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get cart total for a user
     */
    public double getCartTotal(int userId) {
        try {
            List<CartItem> cartItems = getCartItems(userId);
            if (cartItems == null || cartItems.isEmpty()) {
                return 0.0;
            }

            double total = 0.0;
            for (CartItem cartItem : cartItems) {
                Product product = getProduct(cartItem.getProductId());
                if (product != null) {
                    total += product.getPrice() * cartItem.getQuantity();
                }
            }
            return total;
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error calculating cart total: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Check if product is in user's cart
     */
    public boolean isProductInCart(int userId, int productId) {
        try {
            List<CartItem> cartItems = getCartItems(userId);
            if (cartItems == null) {
                return false;
            }

            for (CartItem cartItem : cartItems) {
                if (cartItem.getProductId() == productId) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error checking if product is in cart: " + e.getMessage());
            return false;
        }
    }

    /**
     * Initialize database with sample data
     */
    public void initializeSampleData() {
        try {
            databaseHelper.populateSampleData();
            Log.d("DatabaseManager", "Sample data initialized successfully");
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error initializing sample data: " + e.getMessage());
        }
    }

    /**
     * Close database connection
     */
    public void closeDatabase() {
        try {
            if (databaseHelper != null) {
                databaseHelper.close();
            }
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error closing database: " + e.getMessage());
        }
    }
} 