package com.s23010535.govisaviya.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.s23010535.govisaviya.models.CartItem;
import com.s23010535.govisaviya.models.Order;
import com.s23010535.govisaviya.models.Product;
import com.s23010535.govisaviya.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ue3.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_CART = "cart";

    // Common column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    // Products table columns
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_DESCRIPTION = "description";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_CURRENCY = "currency";
    public static final String COLUMN_PRODUCT_CATEGORY = "category";
    public static final String COLUMN_PRODUCT_SUB_CATEGORY = "sub_category";
    public static final String COLUMN_PRODUCT_SELLER_ID = "seller_id";
    public static final String COLUMN_PRODUCT_SELLER_NAME = "seller_name";
    public static final String COLUMN_PRODUCT_SELLER_LOCATION = "seller_location";
    public static final String COLUMN_PRODUCT_IMAGE_URL = "image_url";
    public static final String COLUMN_PRODUCT_STOCK_QUANTITY = "stock_quantity";
    public static final String COLUMN_PRODUCT_UNIT = "unit";
    public static final String COLUMN_PRODUCT_RATING = "rating";
    public static final String COLUMN_PRODUCT_REVIEW_COUNT = "review_count";
    public static final String COLUMN_PRODUCT_IS_AVAILABLE = "is_available";
    public static final String COLUMN_PRODUCT_IS_FEATURED = "is_featured";
    public static final String COLUMN_PRODUCT_CONDITION = "condition";
    public static final String COLUMN_PRODUCT_DELIVERY_OPTIONS = "delivery_options";
    public static final String COLUMN_PRODUCT_DELIVERY_COST = "delivery_cost";
    public static final String COLUMN_PRODUCT_TAGS = "tags";

    // Users table columns
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD_HASH = "password_hash";
    public static final String COLUMN_USER_FULL_NAME = "full_name";
    public static final String COLUMN_USER_PHONE = "phone";
    public static final String COLUMN_USER_LOCATION = "location";
    public static final String COLUMN_USER_TYPE = "user_type";

    // Orders table columns
    public static final String COLUMN_ORDER_USER_ID = "user_id";
    public static final String COLUMN_ORDER_PRODUCT_ID = "product_id";
    public static final String COLUMN_ORDER_QUANTITY = "quantity";
    public static final String COLUMN_ORDER_TOTAL_PRICE = "total_price";
    public static final String COLUMN_ORDER_STATUS = "status";
    public static final String COLUMN_ORDER_DATE = "order_date";
    public static final String COLUMN_ORDER_DELIVERY_ADDRESS = "delivery_address";
    public static final String COLUMN_ORDER_PAYMENT_METHOD = "payment_method";

    // Cart table columns
    public static final String COLUMN_CART_USER_ID = "user_id";
    public static final String COLUMN_CART_PRODUCT_ID = "product_id";
    public static final String COLUMN_CART_QUANTITY = "quantity";

    // Create table statements
    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PRODUCT_NAME + " TEXT NOT NULL,"
            + COLUMN_PRODUCT_DESCRIPTION + " TEXT,"
            + COLUMN_PRODUCT_PRICE + " REAL NOT NULL,"
            + COLUMN_PRODUCT_CURRENCY + " TEXT DEFAULT 'LKR',"
            + COLUMN_PRODUCT_CATEGORY + " TEXT NOT NULL,"
            + COLUMN_PRODUCT_SUB_CATEGORY + " TEXT,"
            + COLUMN_PRODUCT_SELLER_ID + " TEXT NOT NULL,"
            + COLUMN_PRODUCT_SELLER_NAME + " TEXT NOT NULL,"
            + COLUMN_PRODUCT_SELLER_LOCATION + " TEXT,"
            + COLUMN_PRODUCT_IMAGE_URL + " TEXT,"
            + COLUMN_PRODUCT_STOCK_QUANTITY + " INTEGER DEFAULT 0,"
            + COLUMN_PRODUCT_UNIT + " TEXT DEFAULT 'piece',"
            + COLUMN_PRODUCT_RATING + " REAL DEFAULT 0.0,"
            + COLUMN_PRODUCT_REVIEW_COUNT + " INTEGER DEFAULT 0,"
            + COLUMN_PRODUCT_IS_AVAILABLE + " INTEGER DEFAULT 1,"
            + COLUMN_PRODUCT_IS_FEATURED + " INTEGER DEFAULT 0,"
            + COLUMN_PRODUCT_CONDITION + " TEXT,"
            + COLUMN_PRODUCT_DELIVERY_OPTIONS + " TEXT,"
            + COLUMN_PRODUCT_DELIVERY_COST + " REAL DEFAULT 0.0,"
            + COLUMN_PRODUCT_TAGS + " TEXT,"
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COLUMN_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_USERNAME + " TEXT UNIQUE NOT NULL,"
            + COLUMN_USER_EMAIL + " TEXT UNIQUE NOT NULL,"
            + COLUMN_USER_PASSWORD_HASH + " TEXT NOT NULL,"
            + COLUMN_USER_FULL_NAME + " TEXT,"
            + COLUMN_USER_PHONE + " TEXT,"
            + COLUMN_USER_LOCATION + " TEXT,"
            + COLUMN_USER_TYPE + " TEXT DEFAULT 'buyer',"
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COLUMN_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    private static final String CREATE_TABLE_ORDERS = "CREATE TABLE " + TABLE_ORDERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_ORDER_USER_ID + " INTEGER NOT NULL,"
            + COLUMN_ORDER_PRODUCT_ID + " INTEGER NOT NULL,"
            + COLUMN_ORDER_QUANTITY + " INTEGER NOT NULL,"
            + COLUMN_ORDER_TOTAL_PRICE + " REAL NOT NULL,"
            + COLUMN_ORDER_STATUS + " TEXT DEFAULT 'pending',"
            + COLUMN_ORDER_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COLUMN_ORDER_DELIVERY_ADDRESS + " TEXT,"
            + COLUMN_ORDER_PAYMENT_METHOD + " TEXT,"
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COLUMN_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    private static final String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CART_USER_ID + " INTEGER NOT NULL,"
            + COLUMN_CART_PRODUCT_ID + " INTEGER NOT NULL,"
            + COLUMN_CART_QUANTITY + " INTEGER NOT NULL,"
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_ORDERS);
        db.execSQL(CREATE_TABLE_CART);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // Product CRUD operations
    public long addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(COLUMN_PRODUCT_CURRENCY, product.getCurrency());
        values.put(COLUMN_PRODUCT_CATEGORY, product.getCategory());
        values.put(COLUMN_PRODUCT_SUB_CATEGORY, product.getSubCategory());
        values.put(COLUMN_PRODUCT_SELLER_ID, product.getSellerId());
        values.put(COLUMN_PRODUCT_SELLER_NAME, product.getSellerName());
        values.put(COLUMN_PRODUCT_SELLER_LOCATION, product.getSellerLocation());
        values.put(COLUMN_PRODUCT_IMAGE_URL, product.getImageUrl());
        values.put(COLUMN_PRODUCT_STOCK_QUANTITY, product.getStockQuantity());
        values.put(COLUMN_PRODUCT_UNIT, product.getUnit());
        values.put(COLUMN_PRODUCT_RATING, product.getRating());
        values.put(COLUMN_PRODUCT_REVIEW_COUNT, product.getReviewCount());
        values.put(COLUMN_PRODUCT_IS_AVAILABLE, product.isAvailable() ? 1 : 0);
        values.put(COLUMN_PRODUCT_IS_FEATURED, product.isFeatured() ? 1 : 0);
        values.put(COLUMN_PRODUCT_CONDITION, product.getCondition());
        values.put(COLUMN_PRODUCT_DELIVERY_OPTIONS, product.getDeliveryOptions());
        values.put(COLUMN_PRODUCT_DELIVERY_COST, product.getDeliveryCost());
        values.put(COLUMN_PRODUCT_TAGS, product.getTags());

        long id = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return id;
    }

    public Product getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, null, COLUMN_ID + "=?", 
                new String[]{String.valueOf(id)}, null, null, null);

        Product product = null;
        if (cursor != null && cursor.moveToFirst()) {
            product = cursorToProduct(cursor);
            cursor.close();
        }
        db.close();
        return product;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " ORDER BY " + COLUMN_CREATED_AT + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = cursorToProduct(cursor);
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return products;
    }

    public List<Product> getFeaturedProducts() {
        List<Product> products = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + 
                " WHERE " + COLUMN_PRODUCT_IS_FEATURED + "=1 AND " + COLUMN_PRODUCT_IS_AVAILABLE + "=1" +
                " ORDER BY " + COLUMN_CREATED_AT + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = cursorToProduct(cursor);
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return products;
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + 
                " WHERE " + COLUMN_PRODUCT_CATEGORY + "=? AND " + COLUMN_PRODUCT_IS_AVAILABLE + "=1" +
                " ORDER BY " + COLUMN_CREATED_AT + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{category});

        if (cursor.moveToFirst()) {
            do {
                Product product = cursorToProduct(cursor);
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return products;
    }

    public List<Product> searchProducts(String query) {
        List<Product> products = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + 
                " WHERE (" + COLUMN_PRODUCT_NAME + " LIKE ? OR " + 
                COLUMN_PRODUCT_DESCRIPTION + " LIKE ? OR " + 
                COLUMN_PRODUCT_SELLER_NAME + " LIKE ? OR " + 
                COLUMN_PRODUCT_TAGS + " LIKE ?) AND " + COLUMN_PRODUCT_IS_AVAILABLE + "=1" +
                " ORDER BY " + COLUMN_CREATED_AT + " DESC";

        String searchPattern = "%" + query + "%";
        String[] searchArgs = {searchPattern, searchPattern, searchPattern, searchPattern};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, searchArgs);

        if (cursor.moveToFirst()) {
            do {
                Product product = cursorToProduct(cursor);
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return products;
    }

    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(COLUMN_PRODUCT_CURRENCY, product.getCurrency());
        values.put(COLUMN_PRODUCT_CATEGORY, product.getCategory());
        values.put(COLUMN_PRODUCT_SUB_CATEGORY, product.getSubCategory());
        values.put(COLUMN_PRODUCT_SELLER_ID, product.getSellerId());
        values.put(COLUMN_PRODUCT_SELLER_NAME, product.getSellerName());
        values.put(COLUMN_PRODUCT_SELLER_LOCATION, product.getSellerLocation());
        values.put(COLUMN_PRODUCT_IMAGE_URL, product.getImageUrl());
        values.put(COLUMN_PRODUCT_STOCK_QUANTITY, product.getStockQuantity());
        values.put(COLUMN_PRODUCT_UNIT, product.getUnit());
        values.put(COLUMN_PRODUCT_RATING, product.getRating());
        values.put(COLUMN_PRODUCT_REVIEW_COUNT, product.getReviewCount());
        values.put(COLUMN_PRODUCT_IS_AVAILABLE, product.isAvailable() ? 1 : 0);
        values.put(COLUMN_PRODUCT_IS_FEATURED, product.isFeatured() ? 1 : 0);
        values.put(COLUMN_PRODUCT_CONDITION, product.getCondition());
        values.put(COLUMN_PRODUCT_DELIVERY_OPTIONS, product.getDeliveryOptions());
        values.put(COLUMN_PRODUCT_DELIVERY_COST, product.getDeliveryCost());
        values.put(COLUMN_PRODUCT_TAGS, product.getTags());
        values.put(COLUMN_UPDATED_AT, new Date().getTime());

        int result = db.update(TABLE_PRODUCTS, values, COLUMN_ID + "=?", 
                new String[]{String.valueOf(product.getId())});
        db.close();
        return result;
    }

    public void deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_ID + "=?", new String[]{String.valueOf(productId)});
        db.close();
    }

    // User CRUD operations
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_USERNAME, user.getUsername());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD_HASH, user.getPasswordHash());
        values.put(COLUMN_USER_FULL_NAME, user.getFullName());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_LOCATION, user.getLocation());
        values.put(COLUMN_USER_TYPE, user.getUserType());

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_ID + "=?", 
                new String[]{String.valueOf(id)}, null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = cursorToUser(cursor);
            cursor.close();
        }
        db.close();
        return user;
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USER_USERNAME + "=?", 
                new String[]{username}, null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = cursorToUser(cursor);
            cursor.close();
        }
        db.close();
        return user;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USER_EMAIL + "=?", 
                new String[]{email}, null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = cursorToUser(cursor);
            cursor.close();
        }
        db.close();
        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " ORDER BY " + COLUMN_CREATED_AT + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = cursorToUser(cursor);
                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return users;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_USERNAME, user.getUsername());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD_HASH, user.getPasswordHash());
        values.put(COLUMN_USER_FULL_NAME, user.getFullName());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_LOCATION, user.getLocation());
        values.put(COLUMN_USER_TYPE, user.getUserType());
        values.put(COLUMN_UPDATED_AT, new Date().getTime());

        int result = db.update(TABLE_USERS, values, COLUMN_ID + "=?", 
                new String[]{String.valueOf(user.getId())});
        db.close();
        return result;
    }

    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
        db.close();
    }

    // Order CRUD operations
    public long addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ORDER_USER_ID, order.getUserId());
        values.put(COLUMN_ORDER_PRODUCT_ID, order.getProductId());
        values.put(COLUMN_ORDER_QUANTITY, order.getQuantity());
        values.put(COLUMN_ORDER_TOTAL_PRICE, order.getTotalPrice());
        values.put(COLUMN_ORDER_STATUS, order.getStatus());
        values.put(COLUMN_ORDER_DELIVERY_ADDRESS, order.getDeliveryAddress());
        values.put(COLUMN_ORDER_PAYMENT_METHOD, order.getPaymentMethod());

        long id = db.insert(TABLE_ORDERS, null, values);
        db.close();
        return id;
    }

    public Order getOrder(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, null, COLUMN_ID + "=?", 
                new String[]{String.valueOf(id)}, null, null, null);

        Order order = null;
        if (cursor != null && cursor.moveToFirst()) {
            order = cursorToOrder(cursor);
            cursor.close();
        }
        db.close();
        return order;
    }

    public List<Order> getOrdersByUser(int userId) {
        List<Order> orders = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + 
                " WHERE " + COLUMN_ORDER_USER_ID + "=? ORDER BY " + COLUMN_CREATED_AT + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Order order = cursorToOrder(cursor);
                orders.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orders;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + " ORDER BY " + COLUMN_CREATED_AT + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = cursorToOrder(cursor);
                orders.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orders;
    }

    public int updateOrderStatus(int orderId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ORDER_STATUS, status);
        values.put(COLUMN_UPDATED_AT, new Date().getTime());

        int result = db.update(TABLE_ORDERS, values, COLUMN_ID + "=?", 
                new String[]{String.valueOf(orderId)});
        db.close();
        return result;
    }

    public void deleteOrder(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDERS, COLUMN_ID + "=?", new String[]{String.valueOf(orderId)});
        db.close();
    }

    // Cart CRUD operations
    public long addToCart(CartItem cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_CART_USER_ID, cartItem.getUserId());
        values.put(COLUMN_CART_PRODUCT_ID, cartItem.getProductId());
        values.put(COLUMN_CART_QUANTITY, cartItem.getQuantity());

        long id = db.insert(TABLE_CART, null, values);
        db.close();
        return id;
    }

    public List<CartItem> getCartItems(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CART + 
                " WHERE " + COLUMN_CART_USER_ID + "=? ORDER BY " + COLUMN_CREATED_AT + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                CartItem cartItem = cursorToCartItem(cursor);
                cartItems.add(cartItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return cartItems;
    }

    public int updateCartItemQuantity(int cartItemId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_CART_QUANTITY, quantity);

        int result = db.update(TABLE_CART, values, COLUMN_ID + "=?", 
                new String[]{String.valueOf(cartItemId)});
        db.close();
        return result;
    }

    public void removeFromCart(int cartItemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_ID + "=?", new String[]{String.valueOf(cartItemId)});
        db.close();
    }

    public void clearUserCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_CART_USER_ID + "=?", new String[]{String.valueOf(userId)});
        db.close();
    }

    // Helper methods for cursor conversion
    private Product cursorToProduct(Cursor cursor) {
        Product product = new Product();
        product.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        product.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME)));
        product.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_DESCRIPTION)));
        product.setPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE)));
        product.setCurrency(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_CURRENCY)));
        product.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_CATEGORY)));
        product.setSubCategory(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_SUB_CATEGORY)));
        product.setSellerId(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_SELLER_ID)));
        product.setSellerName(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_SELLER_NAME)));
        product.setSellerLocation(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_SELLER_LOCATION)));
        product.setImageUrl(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_IMAGE_URL)));
        product.setStockQuantity(cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_STOCK_QUANTITY)));
        product.setUnit(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_UNIT)));
        product.setRating(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_RATING)));
        product.setReviewCount(cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_REVIEW_COUNT)));
        product.setAvailable(cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_IS_AVAILABLE)) == 1);
        product.setFeatured(cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_IS_FEATURED)) == 1);
        product.setCondition(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_CONDITION)));
        product.setDeliveryOptions(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_DELIVERY_OPTIONS)));
        product.setDeliveryCost(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_DELIVERY_COST)));
        product.setTags(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_TAGS)));

        // Handle dates
        long createdAt = cursor.getLong(cursor.getColumnIndex(COLUMN_CREATED_AT));
        long updatedAt = cursor.getLong(cursor.getColumnIndex(COLUMN_UPDATED_AT));
        product.setCreatedAt(new Date(createdAt));
        product.setUpdatedAt(new Date(updatedAt));

        return product;
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_USERNAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
        user.setPasswordHash(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD_HASH)));
        user.setFullName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_FULL_NAME)));
        user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
        user.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_USER_LOCATION)));
        user.setUserType(cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE)));

        // Handle dates
        long createdAt = cursor.getLong(cursor.getColumnIndex(COLUMN_CREATED_AT));
        long updatedAt = cursor.getLong(cursor.getColumnIndex(COLUMN_UPDATED_AT));
        user.setCreatedAt(new Date(createdAt));
        user.setUpdatedAt(new Date(updatedAt));

        return user;
    }

    private Order cursorToOrder(Cursor cursor) {
        Order order = new Order();
        order.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        order.setUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_USER_ID)));
        order.setProductId(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_PRODUCT_ID)));
        order.setQuantity(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_QUANTITY)));
        order.setTotalPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_ORDER_TOTAL_PRICE)));
        order.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_STATUS)));
        order.setDeliveryAddress(cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_DELIVERY_ADDRESS)));
        order.setPaymentMethod(cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_PAYMENT_METHOD)));

        // Handle dates
        long orderDate = cursor.getLong(cursor.getColumnIndex(COLUMN_ORDER_DATE));
        long createdAt = cursor.getLong(cursor.getColumnIndex(COLUMN_CREATED_AT));
        long updatedAt = cursor.getLong(cursor.getColumnIndex(COLUMN_UPDATED_AT));
        order.setOrderDate(new Date(orderDate));
        order.setCreatedAt(new Date(createdAt));
        order.setUpdatedAt(new Date(updatedAt));

        return order;
    }

    private CartItem cursorToCartItem(Cursor cursor) {
        CartItem cartItem = new CartItem();
        cartItem.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        cartItem.setUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_CART_USER_ID)));
        cartItem.setProductId(cursor.getInt(cursor.getColumnIndex(COLUMN_CART_PRODUCT_ID)));
        cartItem.setQuantity(cursor.getInt(cursor.getColumnIndex(COLUMN_CART_QUANTITY)));

        // Handle date
        long createdAt = cursor.getLong(cursor.getColumnIndex(COLUMN_CREATED_AT));
        cartItem.setCreatedAt(new Date(createdAt));

        return cartItem;
    }

    // Method to populate database with sample data
    public void populateSampleData() {
        Log.d("DatabaseHelper", "Populating sample data...");
        
        // Add sample users
        User user1 = new User("john_doe", "john@example.com", "hashed_password", "John Doe");
        user1.setPhone("+94 71 123 4567");
        user1.setLocation("Colombo");
        user1.setUserType("buyer");
        addUser(user1);

        User user2 = new User("farmer_jane", "jane@example.com", "hashed_password", "Jane Smith");
        user2.setPhone("+94 77 987 6543");
        user2.setLocation("Kandy");
        user2.setUserType("seller");
        addUser(user2);

        // Add sample products
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
        addProduct(tomato);

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
        addProduct(rice);

        Log.d("DatabaseHelper", "Sample data populated successfully");
    }
} 