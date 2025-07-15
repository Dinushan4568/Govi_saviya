package com.s23010535.govisaviya;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.s23010535.govisaviya.adapters.ProductAdapter;
import com.s23010535.govisaviya.data.ProductDataManager;
import com.s23010535.govisaviya.models.Product;
import java.util.List;

public class ProductListActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {
    public static final String EXTRA_CATEGORY_ID = "category_id";
    public static final String EXTRA_CATEGORY_NAME = "category_name";
    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private ProductAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        recyclerView = findViewById(R.id.recyclerViewProducts);
        tvEmpty = findViewById(R.id.tvEmpty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String categoryId = getIntent().getStringExtra(EXTRA_CATEGORY_ID);
        String categoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        setTitle(categoryName != null ? categoryName : "Products");
        List<Product> products;
        if (categoryId == null || categoryId.equals("all")) {
            products = ProductDataManager.getInstance(this).getAllProducts();
        } else {
            products = ProductDataManager.getInstance(this).getProductsByCategory(categoryId);
        }
        if (products == null || products.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new ProductAdapter(this, products, this);
            recyclerView.setAdapter(adapter);
        }
    }
    @Override
    public void onProductClick(Product product) {
        // TODO: Open product details if needed
    }

    @Override
    public void onBuyNowClick(Product product) {
        // TODO: Implement buy now logic if needed
    }

    @Override
    public void onAddToCartClick(Product product) {
        // TODO: Implement add to cart logic if needed
    }
} 