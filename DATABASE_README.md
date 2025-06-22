# Govi Saviya - MySQLite Database Documentation

## Overview
This document describes the complete MySQLite database implementation for the Govi Saviya Android application. The database is designed to support a comprehensive agricultural marketplace with user management, product catalog, shopping cart, and order processing.

## Database Architecture

### Core Components
1. **DatabaseHelper** - Low-level SQLite operations
2. **DatabaseManager** - High-level business logic interface
3. **Model Classes** - Data transfer objects (DTOs)
4. **Sample Data** - Initial data for testing and demonstration

## Database Schema

### 1. Products Table
```sql
CREATE TABLE products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    price REAL NOT NULL,
    currency TEXT DEFAULT 'LKR',
    category TEXT NOT NULL,
    sub_category TEXT,
    seller_id TEXT NOT NULL,
    seller_name TEXT NOT NULL,
    seller_location TEXT,
    image_url TEXT,
    stock_quantity INTEGER DEFAULT 0,
    unit TEXT DEFAULT 'piece',
    rating REAL DEFAULT 0.0,
    review_count INTEGER DEFAULT 0,
    is_available INTEGER DEFAULT 1,
    is_featured INTEGER DEFAULT 0,
    condition TEXT,
    delivery_options TEXT,
    delivery_cost REAL DEFAULT 0.0,
    tags TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

**Product Categories:**
- `direct_food` - Fresh produce and food items
- `pesticides` - Pest control products
- `seeds` - Seeds and seedlings
- `rental` - Equipment rental services
- `labor` - Labor and harvesting services
- `others` - Miscellaneous agricultural items

### 2. Users Table
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    full_name TEXT,
    phone TEXT,
    location TEXT,
    user_type TEXT DEFAULT 'buyer',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

**User Types:**
- `buyer` - Customers purchasing products
- `seller` - Farmers and suppliers selling products
- `admin` - System administrators

### 3. Orders Table
```sql
CREATE TABLE orders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    total_price REAL NOT NULL,
    status TEXT DEFAULT 'pending',
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    delivery_address TEXT,
    payment_method TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

**Order Statuses:**
- `pending` - Order placed, awaiting confirmation
- `confirmed` - Order confirmed by seller
- `shipped` - Order shipped/delivered
- `delivered` - Order successfully delivered
- `cancelled` - Order cancelled

### 4. Cart Table
```sql
CREATE TABLE cart (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

## Model Classes

### 1. Product.java
Complete product model with all necessary fields for marketplace functionality.

**Key Features:**
- Comprehensive product information
- Stock management
- Rating and review system
- Featured product support
- Delivery options and costs
- Search-friendly tags

### 2. User.java
User management model with authentication support.

**Key Features:**
- User authentication
- Role-based access control
- Profile management
- Contact information

### 3. Order.java
Order processing model with status tracking.

**Key Features:**
- Order lifecycle management
- Status tracking
- Payment method support
- Delivery address management

### 4. CartItem.java
Shopping cart functionality model.

**Key Features:**
- Cart item management
- Quantity control
- Subtotal calculation
- Stock validation

## Database Operations

### DatabaseManager Class
The `DatabaseManager` provides a high-level interface for all database operations with error handling and business logic.

#### Product Operations
```java
// Add new product
long productId = databaseManager.addProduct(product);

// Get all products
List<Product> products = databaseManager.getAllProducts();

// Get featured products
List<Product> featuredProducts = databaseManager.getFeaturedProducts();

// Search products
List<Product> searchResults = databaseManager.searchProducts("tomato");

// Get products by category
List<Product> categoryProducts = databaseManager.getProductsByCategory("direct_food");

// Update product
boolean success = databaseManager.updateProduct(product);

// Delete product
boolean deleted = databaseManager.deleteProduct(productId);
```

#### User Operations
```java
// Add new user
long userId = databaseManager.addUser(user);

// Authenticate user
User authenticatedUser = databaseManager.authenticateUser(username, passwordHash);

// Get user by username
User user = databaseManager.getUserByUsername(username);

// Get user by email
User user = databaseManager.getUserByEmail(email);

// Update user
boolean success = databaseManager.updateUser(user);

// Delete user
boolean deleted = databaseManager.deleteUser(userId);
```

#### Order Operations
```java
// Add new order
long orderId = databaseManager.addOrder(order);

// Get user orders
List<Order> userOrders = databaseManager.getOrdersByUser(userId);

// Get all orders
List<Order> allOrders = databaseManager.getAllOrders();

// Update order status
boolean updated = databaseManager.updateOrderStatus(orderId, "confirmed");

// Delete order
boolean deleted = databaseManager.deleteOrder(orderId);
```

#### Cart Operations
```java
// Add to cart
long cartItemId = databaseManager.addToCart(cartItem);

// Get cart items
List<CartItem> cartItems = databaseManager.getCartItems(userId);

// Update quantity
boolean updated = databaseManager.updateCartItemQuantity(cartItemId, newQuantity);

// Remove from cart
boolean removed = databaseManager.removeFromCart(cartItemId);

// Clear cart
boolean cleared = databaseManager.clearUserCart(userId);
```

#### Business Logic Methods
```java
// Create order from cart
boolean orderCreated = databaseManager.createOrderFromCart(userId, deliveryAddress, paymentMethod);

// Get cart total
double total = databaseManager.getCartTotal(userId);

// Check if product is in cart
boolean inCart = databaseManager.isProductInCart(userId, productId);

// Initialize sample data
databaseManager.initializeSampleData();
```

## Usage Examples

### 1. Initialize Database
```java
// In your Activity or Application class
DatabaseManager databaseManager = DatabaseManager.getInstance(this);

// Initialize with sample data (first time only)
databaseManager.initializeSampleData();
```

### 2. Product Management
```java
// Add a new product
Product newProduct = new Product("Fresh Carrots", "Organic carrots from local farm", 150.0, "direct_food", "seller001", "Farmer John");
newProduct.setSellerLocation("Colombo");
newProduct.setStockQuantity(25);
newProduct.setUnit("kg");
newProduct.setFeatured(true);
long productId = databaseManager.addProduct(newProduct);

// Search for products
List<Product> searchResults = databaseManager.searchProducts("organic");
```

### 3. User Authentication
```java
// User login
User user = databaseManager.authenticateUser("john_doe", "hashed_password");
if (user != null) {
    // Login successful
    // Store user session
} else {
    // Login failed
}
```

### 4. Shopping Cart
```java
// Add item to cart
CartItem cartItem = new CartItem(userId, productId, 2);
long cartItemId = databaseManager.addToCart(cartItem);

// Get cart total
double total = databaseManager.getCartTotal(userId);
```

### 5. Order Processing
```java
// Create order from cart
boolean success = databaseManager.createOrderFromCart(userId, "123 Main St, Colombo", "Cash on Delivery");
if (success) {
    // Order created successfully
    // Cart cleared automatically
}
```

## Sample Data

The database comes with pre-populated sample data including:

### Sample Users
- **John Doe** (buyer) - john@example.com
- **Jane Smith** (seller) - jane@example.com

### Sample Products
- Fresh Organic Tomatoes (Featured)
- Red Rice (Featured)
- Organic Neem Oil
- Tractor Rental (Featured)
- Hybrid Corn Seeds
- Harvesting Services
- Organic Compost
- Garden Tool Set

## Database Migration

### Version Management
The database uses version management for schema updates:

```java
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop existing tables
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
    
    // Recreate tables
    onCreate(db);
}
```

### Future Enhancements
- Add indexes for better performance
- Implement foreign key constraints
- Add more complex queries for analytics
- Support for product images and file storage
- User preferences and favorites
- Review and rating system
- Notification system

## Error Handling

All database operations include comprehensive error handling:

```java
try {
    // Database operation
    long result = databaseManager.addProduct(product);
    if (result > 0) {
        // Success
    } else {
        // Failed
    }
} catch (Exception e) {
    Log.e("Database", "Error: " + e.getMessage());
    // Handle error appropriately
}
```

## Performance Considerations

1. **Use DatabaseManager** instead of direct DatabaseHelper calls
2. **Close database connections** properly
3. **Use transactions** for bulk operations
4. **Implement pagination** for large datasets
5. **Cache frequently accessed data**
6. **Use indexes** for search operations

## Security Considerations

1. **Hash passwords** before storing
2. **Validate input data** before database operations
3. **Use parameterized queries** to prevent SQL injection
4. **Implement proper access control**
5. **Encrypt sensitive data** if needed

## Testing

The database implementation includes:
- Comprehensive CRUD operations
- Error handling and logging
- Sample data for testing
- Business logic validation
- Transaction support

## Conclusion

This MySQLite database implementation provides a solid foundation for the Govi Saviya agricultural marketplace. It supports all essential features including user management, product catalog, shopping cart, and order processing. The modular design allows for easy extension and maintenance.

For questions or issues, refer to the code comments and this documentation. 