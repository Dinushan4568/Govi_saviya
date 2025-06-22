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

import java.util.List;

public class MarketplaceActivity extends Activity implements ProductAdapter.OnProductClickListener {

    private EditText searchEditText;
    private ImageView btnNotifications, btnFilter;
    private LinearLayout btnSellProduct, btnMyOrders;
    private LinearLayout categoryDirectFood, categoryPesticides, categoryRental;
    private LinearLayout categorySeeds, categoryLabor, categoryOthers;
    private TextView btnViewAll;
    private RecyclerView featuredProductsRecyclerView;
    
    private ProductAdapter productAdapter;
    private ProductDataManager dataManager;
    private List<Product> currentProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace);

        initializeViews();
        setupDataManager();
        setupRecyclerView();
        setupClickListeners();
        setupSearchFunctionality();
        loadFeaturedProducts();
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

        // View all button
        btnViewAll = findViewById(R.id.btnViewAll);
        
        // RecyclerView
        featuredProductsRecyclerView = findViewById(R.id.featuredProductsRecyclerView);
    }

    private void setupDataManager() {
        dataManager = ProductDataManager.getInstance();
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter(this, dataManager.getFeaturedProducts(), this);
        featuredProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        featuredProductsRecyclerView.setAdapter(productAdapter);
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
                showToast("Opening sell product form...");
                // Intent intent = new Intent(MarketplaceActivity.this, SellProductActivity.class);
                // startActivity(intent);
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
                openCategoryView("direct_food", "Direct Food Sale");
            }
        });

        categoryPesticides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryView("pesticides", "Pesticides");
            }
        });

        categoryRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryView("rental", "Equipment Rental");
            }
        });

        categorySeeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryView("seeds", "Seeds & Fertilizers");
            }
        });

        categoryLabor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryView("labor", "Labor Services");
            }
        });

        categoryOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryView("others", "Other Items");
            }
        });

        // View All button
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAllProductsView();
            }
        });
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

    private void loadFeaturedProducts() {
        currentProducts = dataManager.getFeaturedProducts();
        productAdapter.updateProducts(currentProducts);
    }

    private void performSearch(String query) {
        if (query.trim().isEmpty()) {
            // Show featured products when search is empty
            currentProducts = dataManager.getFeaturedProducts();
        } else {
            // Perform search
            currentProducts = dataManager.searchProducts(query);
        }
        productAdapter.updateProducts(currentProducts);
    }

    private void openCategoryView(String categoryId, String categoryName) {
        showToast("Opening " + categoryName + " category...");
        
        // Get products for this category
        List<Product> categoryProducts = dataManager.getProductsByCategory(categoryId);
        
        // TODO: Open category-specific activity with products
        // Intent intent = new Intent(MarketplaceActivity.this, CategoryActivity.class);
        // intent.putExtra("category_id", categoryId);
        // intent.putExtra("category_name", categoryName);
        // startActivity(intent);
        
        showToast("Found " + categoryProducts.size() + " products in " + categoryName);
    }

    private void openAllProductsView() {
        showToast("Opening all products...");
        // TODO: Open all products activity
        // Intent intent = new Intent(MarketplaceActivity.this, AllProductsActivity.class);
        // startActivity(intent);
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
        showToast("Added " + product.getName() + " to cart");
        // TODO: Implement add to cart functionality
        // CartManager.getInstance().addToCart(product);
    }

    @Override
    public void onBuyNowClick(Product product) {
        showToast("Buying " + product.getName() + " now");
        // TODO: Implement buy now functionality
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