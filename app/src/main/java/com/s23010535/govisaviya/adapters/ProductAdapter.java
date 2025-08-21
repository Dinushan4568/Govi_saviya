package com.s23010535.govisaviya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.s23010535.govisaviya.R;
import com.s23010535.govisaviya.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> products;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onAddToCartClick(Product product);
        void onBuyNowClick(Product product);
    }

    public ProductAdapter(Context context, List<Product> products, OnProductClickListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName, sellerName, sellerLocation, reviewCount;
        private TextView productPrice, productUnit, stockStatusBadge, featuredBadge;
        private RatingBar productRating;
        private Button btnAddToCart, btnBuyNow;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            sellerName = itemView.findViewById(R.id.sellerName);
            sellerLocation = itemView.findViewById(R.id.sellerLocation);
            productPrice = itemView.findViewById(R.id.productPrice);
            productUnit = itemView.findViewById(R.id.productUnit);
            productRating = itemView.findViewById(R.id.productRating);
            reviewCount = itemView.findViewById(R.id.reviewCount);
            stockStatusBadge = itemView.findViewById(R.id.stockStatusBadge);
            featuredBadge = itemView.findViewById(R.id.featuredBadge);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnBuyNow = itemView.findViewById(R.id.btnBuyNow);

            // Set click listeners
            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onProductClick(products.get(position));
                }
            });

            btnAddToCart.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onAddToCartClick(products.get(position));
                }
            });

            btnBuyNow.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onBuyNowClick(products.get(position));
                }
            });
        }

        public void bind(Product product) {
            // Set product name
            productName.setText(product.getName());

            // Set seller information
            sellerName.setText(product.getSellerName());
            if (product.getSellerLocation() != null && !product.getSellerLocation().isEmpty()) {
                sellerLocation.setText(product.getSellerLocation());
                sellerLocation.setVisibility(View.VISIBLE);
            } else {
                sellerLocation.setVisibility(View.GONE);
            }

            // Set price and unit
            productPrice.setText(product.getFormattedPrice());
            if (product.getUnit() != null && !product.getUnit().isEmpty()) {
                productUnit.setText("per " + product.getUnit());
                productUnit.setVisibility(View.VISIBLE);
            } else {
                productUnit.setVisibility(View.GONE);
            }

            // Set rating and reviews
            productRating.setRating((float) product.getRating());
            if (product.getReviewCount() > 0) {
                reviewCount.setText("(" + product.getReviewCount() + " reviews)");
                reviewCount.setVisibility(View.VISIBLE);
            } else {
                reviewCount.setVisibility(View.GONE);
            }

            // Set stock status
            String stockStatus = product.getStockStatus();
            stockStatusBadge.setText(stockStatus);
            
            // Update stock status badge color based on availability
            if (stockStatus.equals("Out of Stock")) {
                stockStatusBadge.setBackgroundResource(R.drawable.out_of_stock_background);
                btnAddToCart.setEnabled(false);
                btnBuyNow.setEnabled(false);
            } else if (stockStatus.equals("Low Stock")) {
                stockStatusBadge.setBackgroundResource(R.drawable.low_stock_background);
                btnAddToCart.setEnabled(true);
                btnBuyNow.setEnabled(true);
            } else {
                stockStatusBadge.setBackgroundResource(R.drawable.stock_status_background);
                btnAddToCart.setEnabled(true);
                btnBuyNow.setEnabled(true);
            }

            // Show/hide featured badge
            if (product.isFeatured()) {
                featuredBadge.setVisibility(View.VISIBLE);
            } else {
                featuredBadge.setVisibility(View.GONE);
            }

            // Set product image (show captured/selected image if available)
            if (product.getImageUri() != null && !product.getImageUri().isEmpty()) {
                productImage.setImageURI(android.net.Uri.parse(product.getImageUri()));
            } else if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                // Load image from URL (future use)
                // Glide.with(context).load(product.getImageUrl()).into(productImage);
            } else {
                // Set default image based on category
                setDefaultImage(product.getCategory());
            }
        }

        private void setDefaultImage(String category) {
            if (category != null) {
                switch (category.toLowerCase()) {
                    case "direct_food":
                    case "vegetables":
                    case "fruits":
                        productImage.setImageResource(R.drawable.ic_vegetables);
                        break;
                    case "pesticides":
                        productImage.setImageResource(R.drawable.ic_pesticides);
                        break;
                    case "rental":
                    case "equipment":
                        productImage.setImageResource(R.drawable.ic_equipmentv);
                        break;
                    case "seeds":
                    case "fertilizers":
                        productImage.setImageResource(R.drawable.ic_seeds);
                        break;
                    case "labor":
                    case "services":
                        productImage.setImageResource(R.drawable.ic_labor);
                        break;
                    default:
                        productImage.setImageResource(R.drawable.ic_others);
                        break;
                }
            } else {
                productImage.setImageResource(R.drawable.ic_vegetables);
            }
        }
    }
} 