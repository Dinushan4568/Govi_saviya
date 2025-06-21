package com.s23010535.govisaviya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MarketplaceActivity extends Activity {

    private EditText searchEditText;
    private ImageView btnNotifications, btnFilter;
    private LinearLayout btnSellProduct, btnMyOrders;
    private LinearLayout categoryDirectFood, categoryPesticides, categoryRental;
    private LinearLayout categorySeeds, categoryLabor, categoryOthers;
    private TextView btnViewAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace);

        initializeViews();
        setupClickListeners();
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
    }

    private void setupClickListeners() {
        // Notifications
        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open notifications activity
                showToast("Opening notifications...");
                // Intent intent = new Intent(MarketplaceActivity.this, NotificationsActivity.class);
                // startActivity(intent);
            }
        });

        // Filter
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open filter dialog
                showToast("Opening filters...");
                // showFilterDialog();
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
                showToast("Showing all featured products...");
                // Intent intent = new Intent(MarketplaceActivity.this, AllProductsActivity.class);
                // startActivity(intent);
            }
        });
    }

    private void openCategoryView(String categoryId, String categoryName) {
        showToast("Opening " + categoryName + " category...");

        // Create intent to open category-specific activity
        // Intent intent = new Intent(MarketplaceActivity.this, CategoryActivity.class);
        // intent.putExtra("category_id", categoryId);
        // intent.putExtra("category_name", categoryName);
        // startActivity(intent);

        // For now, just show a toast message
        showToast("Category: " + categoryName);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Method to handle search functionality
    private void performSearch() {
        String searchQuery = searchEditText.getText().toString().trim();
        if (!searchQuery.isEmpty()) {
            showToast("Searching for: " + searchQuery);
            // Implement search logic here
            // You can filter products based on the search query
        } else {
            showToast("Please enter search terms");
        }
    }

    // Method to handle back button press
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // You can add custom back button behavior here if needed
    }
}