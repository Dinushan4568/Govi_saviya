package com.s23010535.govisaviya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.s23010535.govisaviya.adapters.ProductAdapter;
import com.s23010535.govisaviya.data.ProductDataManager;
import com.s23010535.govisaviya.models.Product;
import com.s23010535.govisaviya.data.SessionManager;
import com.s23010535.govisaviya.models.CartItem;
import com.s23010535.govisaviya.database.DatabaseManager;

import java.util.List;

public class MarketplaceActivity extends Activity implements ProductAdapter.OnProductClickListener {

    private EditText searchEditText;
    private ImageView btnNotifications, btnFilter;
    private LinearLayout btnSellProduct, btnMyOrders;
    private LinearLayout categoryDirectFood, categoryPesticides, categoryRental;
    private LinearLayout categorySeeds, categoryLabor, categoryOthers;
    private LinearLayout categoryAll;
    private ProductDataManager dataManager;
    private SessionManager sessionManager;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace);

        initializeViews();
        setupManagers();
        setupClickListeners();
        setupSearchFunctionality();
    }

    private void initializeViews() {
        // Search and filter
        searchEditText = findViewById(R.id.searchEditText);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnFilter = findViewById(R.id.btnFilter);

        // Quick actions
        btnSellProduct = findViewById(R.id.btnSellProduct);
        btnMyOrders = findViewById(R.id.btnMyOrders);

        // Categories
        categoryDirectFood = findViewById(R.id.categoryDirectFood);
        categoryPesticides = findViewById(R.id.categoryPesticides);
        categoryRental = findViewById(R.id.categoryRental);
        categorySeeds = findViewById(R.id.categorySeeds);
        categoryLabor = findViewById(R.id.categoryLabor);
        categoryOthers = findViewById(R.id.categoryOthers);
        categoryAll = findViewById(R.id.categoryAll);
    }

    private void setupManagers() {
        dataManager = ProductDataManager.getInstance(this);
        sessionManager = new SessionManager(this);
        databaseManager = DatabaseManager.getInstance(this);
    }

    private void setupClickListeners() {
        // Notifications
        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Opening notifications...");
                // Intent intent = new Intent(MarketplaceActivity.this, NotificationActivity.class);
                // startActivity(intent);
            }
        });

        // Filter
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

        // Sell Product
        btnSellProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarketplaceActivity.this, SellProductActivity.class);
                startActivity(intent);
            }
        });

        // My Orders
        btnMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Opening my orders...");
                // Intent intent = new Intent(MarketplaceActivity.this, MyOrdersActivity.class);
                // startActivity(intent);
            }
        });

        // Category click listeners
        categoryDirectFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductList("direct_food", "Direct Food Sale");
            }
        });

        categoryPesticides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductList("pesticides", "Pesticides");
            }
        });

        categoryRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductList("rental", "Equipment Rental");
            }
        });

        categorySeeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductList("seeds", "Seeds & Fertilizers");
            }
        });

        categoryLabor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductList("labor", "Labor Services");
            }
        });

        categoryOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductList("others", "Other Items");
            }
        });

        categoryAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductList("all", "All Products");
            }
        });

        // View All button
        // Removed: btnViewAll.setOnClickListener(new View.OnClickListener() { ... });
    }

    private void setupSearchFunctionality() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void performSearch(String query) {
        if (query.trim().isEmpty()) {
            // Show featured products when search is empty
            // currentProducts = dataManager.getFeaturedProducts(); // Removed
        } else {
            // Perform search
            // currentProducts = dataManager.searchProducts(query); // Removed
        }
        // productAdapter.updateProducts(currentProducts); // Removed
    }

    private void openProductList(String categoryId, String categoryName) {
        Intent intent = new Intent(MarketplaceActivity.this, ProductListActivity.class);
        intent.putExtra(ProductListActivity.EXTRA_CATEGORY_ID, categoryId);
        intent.putExtra(ProductListActivity.EXTRA_CATEGORY_NAME, categoryName);
        startActivity(intent);
    }

    private void showFilterDialog() {
        // TODO: Implement filter dialog
        showToast("Filter options coming soon...");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // ProductAdapter.OnProductClickListener implementations
    @Override
    public void onProductClick(Product product) {
        showToast("Opening product: " + product.getName());
        // TODO: Open product detail activity
        // Intent intent = new Intent(MarketplaceActivity.this, ProductDetailActivity.class);
        // intent.putExtra("product_id", product.getId());
        // startActivity(intent);
    }

    @Override
    public void onAddToCartClick(Product product) {
        if (!sessionManager.isLoggedIn()) {
            showToast("Please login to add items to the cart.");
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        int userId = sessionManager.getUserId();
        CartItem cartItem = new CartItem(userId, product.getId(), 1); // Default quantity is 1
        
        // Check if item is already in cart, if so, update quantity (or show message)
        // For simplicity, we just add it. A real app should handle this better.
        long cartItemId = databaseManager.addToCart(cartItem);

        if (cartItemId > 0) {
            showToast("Added " + product.getName() + " to cart");
        } else {
            showToast("Failed to add item to cart.");
        }
    }

    @Override
    public void onBuyNowClick(Product product) {
        if (!sessionManager.isLoggedIn()) {
            showToast("Please login to buy items.");
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        
        showToast("Buying " + product.getName() + " now");
        // TODO: Implement buy now functionality
        // This would typically add the item to cart and go directly to checkout
        // Intent intent = new Intent(MarketplaceActivity.this, CheckoutActivity.class);
        // intent.putExtra("product_id", product.getId());
        // startActivity(intent);
    }

    // Method to handle back button press
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // You can add custom back button behavior here if needed
    }
} 