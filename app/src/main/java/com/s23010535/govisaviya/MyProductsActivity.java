package com.s23010535.govisaviya;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.s23010535.govisaviya.adapters.ProductAdapter;
import com.s23010535.govisaviya.data.ProductDataManager;
import com.s23010535.govisaviya.data.SessionManager;
import com.s23010535.govisaviya.models.Product;
import java.util.List;

public class MyProductsActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {
    
    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private ProductAdapter adapter;
    private ProductDataManager productDataManager;
    private SessionManager sessionManager;
    private List<Product> myProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);
        
        initializeViews();
        setupClickListeners();
        loadMyProducts();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewProducts);
        tvEmpty = findViewById(R.id.tvEmpty);
        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnAddProduct = findViewById(R.id.btnAddProduct);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        productDataManager = ProductDataManager.getInstance(this);
        sessionManager = new SessionManager(this);
    }

    private void setupClickListeners() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnAddProduct).setOnClickListener(v -> {
            Intent intent = new Intent(this, SellProductActivity.class);
            startActivity(intent);
        });
    }

    private void loadMyProducts() {
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Please login to view your products", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String sellerId = sessionManager.getUserId() + ""; // Convert to string
        myProducts = productDataManager.getProductsBySeller(sellerId);

        if (myProducts == null || myProducts.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setText("You haven't added any products yet.\nTap the + button to add your first product!");
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new ProductAdapter(this, myProducts, this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the list when returning from other activities
        loadMyProducts();
    }

    @Override
    public void onProductClick(Product product) {
        // Show options dialog for edit/delete
        showProductOptionsDialog(product);
    }

    @Override
    public void onAddToCartClick(Product product) {
        // Not applicable for my products
        Toast.makeText(this, "This is your own product", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBuyNowClick(Product product) {
        // Not applicable for my products
        Toast.makeText(this, "This is your own product", Toast.LENGTH_SHORT).show();
    }

    private void showProductOptionsDialog(Product product) {
        String[] options = {"Edit Product", "Delete Product", "View Details"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product Options");
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: // Edit
                    editProduct(product);
                    break;
                case 1: // Delete
                    showDeleteConfirmation(product);
                    break;
                case 2: // View Details
                    viewProductDetails(product);
                    break;
            }
        });
        builder.show();
    }

    private void editProduct(Product product) {
        Intent intent = new Intent(this, EditProductActivity.class);
        intent.putExtra("product_id", product.getId());
        startActivity(intent);
    }

    private void showDeleteConfirmation(Product product) {
        new AlertDialog.Builder(this)
            .setTitle("Delete Product")
            .setMessage("Are you sure you want to delete '" + product.getName() + "'?")
            .setPositiveButton("Delete", (dialog, which) -> {
                deleteProduct(product);
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void deleteProduct(Product product) {
        boolean success = productDataManager.deleteProduct(product.getId());
        if (success) {
            Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
            loadMyProducts(); // Refresh the list
        } else {
            Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewProductDetails(Product product) {
        // Show product details in a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(product.getName());
        
        String details = "Description: " + product.getDescription() + "\n\n" +
                        "Price: " + product.getFormattedPrice() + "\n" +
                        "Category: " + product.getCategory() + "\n" +
                        "Stock: " + product.getStockQuantity() + " " + product.getUnit() + "\n" +
                        "Location: " + product.getSellerLocation() + "\n" +
                        "Rating: " + product.getRating() + " (" + product.getReviewCount() + " reviews)";
        
        builder.setMessage(details);
        builder.setPositiveButton("Edit", (dialog, which) -> editProduct(product));
        builder.setNegativeButton("Close", null);
        builder.show();
    }
}
