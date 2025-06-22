package com.s23010535.govisaviya.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.s23010535.govisaviya.models.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Database helper class for local SQLite database
 * This will be replaced with MySQL database integration later
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GoviSaviyaDB";
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
            + "username TEXT UNIQUE NOT NULL,"
            + "email TEXT UNIQUE NOT NULL,"
            + "password_hash TEXT NOT NULL,"
            + "full_name TEXT,"
            + "phone TEXT,"
            + "location TEXT,"
            + "user_type TEXT DEFAULT 'buyer',"
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COLUMN_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    private static final String CREATE_TABLE_ORDERS = "CREATE TABLE " + TABLE_ORDERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "user_id INTEGER NOT NULL,"
            + "product_id INTEGER NOT NULL,"
            + "quantity INTEGER NOT NULL,"
            + "total_price REAL NOT NULL,"
            + "status TEXT DEFAULT 'pending',"
            + "order_date DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + "delivery_address TEXT,"
            + "payment_method TEXT,"
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COLUMN_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    private static final String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "user_id INTEGER NOT NULL,"
            + "product_id INTEGER NOT NULL,"
            + "quantity INTEGER NOT NULL,"
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
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);

        // Create tables again
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

    // Method to populate database with sample data
    public void populateSampleData() {
        // This method will be called to populate the database with sample data
        // It will use the ProductDataManager to get sample products and insert them
        Log.d("DatabaseHelper", "Populating sample data...");
        
        // TODO: Implement sample data population
        // This will be useful for testing and demonstration purposes
    }
} 