package com.s23010535.govisaviya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.s23010535.govisaviya.data.ProductDataManager;
import com.s23010535.govisaviya.models.Product;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class EditProductActivity extends AppCompatActivity {
    
    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    
    private EditText etProductName, etDescription, etPrice, etQuantity, etLocation;
    private ImageView ivProductImage;
    private ChipGroup chipGroupCategory;
    private Button btnUpdateProduct, btnSelectImage, btnCaptureImage;
    private Uri selectedImageUri;
    private Product currentProduct;
    private ProductDataManager productDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        
        initializeViews();
        setupClickListeners();
        loadProductData();
    }

    private void initializeViews() {
        etProductName = findViewById(R.id.etProductName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        etQuantity = findViewById(R.id.etQuantity);
        etLocation = findViewById(R.id.etLocation);
        ivProductImage = findViewById(R.id.ivProductImage);
        chipGroupCategory = findViewById(R.id.chipGroupCategory);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        
        productDataManager = ProductDataManager.getInstance(this);
    }

    private void setupClickListeners() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        btnSelectImage.setOnClickListener(v -> selectImage());
        btnCaptureImage.setOnClickListener(v -> captureImage());
        btnUpdateProduct.setOnClickListener(v -> updateProduct());
    }

    private void loadProductData() {
        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentProduct = productDataManager.getProductById(productId);
        if (currentProduct == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Populate fields with current product data
        etProductName.setText(currentProduct.getName());
        etDescription.setText(currentProduct.getDescription());
        etPrice.setText(String.valueOf(currentProduct.getPrice()));
        etQuantity.setText(String.valueOf(currentProduct.getStockQuantity()));
        etLocation.setText(currentProduct.getSellerLocation());

        // Set category chip
        String category = getCategoryNameFromId(currentProduct.getCategory());
        for (int i = 0; i < chipGroupCategory.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupCategory.getChildAt(i);
            if (chip.getText().toString().equals(category)) {
                chip.setChecked(true);
                break;
            }
        }

        // Load product image
        if (currentProduct.getImageUri() != null && !currentProduct.getImageUri().isEmpty()) {
            selectedImageUri = Uri.parse(currentProduct.getImageUri());
            ivProductImage.setImageURI(selectedImageUri);
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    private void captureImage() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK && data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                ivProductImage.setImageURI(selectedImageUri);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ivProductImage.setImageBitmap(imageBitmap);
                selectedImageUri = saveBitmapToStorage(imageBitmap);
            }
        }
    }

    private Uri saveBitmapToStorage(Bitmap bitmap) {
        try {
            File imagesDir = new File(getFilesDir(), "product_images");
            if (!imagesDir.exists()) {
                imagesDir.mkdirs();
            }
            String fileName = "product_" + System.currentTimeMillis() + ".jpg";
            File imageFile = new File(imagesDir, fileName);
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(imageFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateProduct() {
        String name = etProductName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || 
            TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(quantityStr) || 
            TextUtils.isEmpty(location)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        int quantity;
        try {
            price = Double.parseDouble(priceStr);
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid price and quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        if (price <= 0 || quantity <= 0) {
            Toast.makeText(this, "Price and quantity must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected category
        int checkedChipId = chipGroupCategory.getCheckedChipId();
        if (checkedChipId == android.view.View.NO_ID) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }
        Chip selectedChip = findViewById(checkedChipId);
        String category = getCategoryIdFromName(selectedChip.getText().toString());

        // Update product
        currentProduct.setName(name);
        currentProduct.setDescription(description);
        currentProduct.setPrice(price);
        currentProduct.setStockQuantity(quantity);
        currentProduct.setSellerLocation(location);
        currentProduct.setCategory(category);
        
        if (selectedImageUri != null) {
            currentProduct.setImageUri(selectedImageUri.toString());
        }

        // Save to database
        boolean success = productDataManager.updateProduct(currentProduct);
        if (success) {
            Toast.makeText(this, "Product updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCategoryIdFromName(String categoryName) {
        switch (categoryName) {
            case "Direct Food Sale": return "direct_food";
            case "Pesticides": return "pesticides";
            case "Equipment Rental": return "rental";
            case "Seeds & Fertilizers": return "seeds";
            case "Labor Services": return "labor";
            case "Other Items": return "others";
            default: return "others";
        }
    }

    private String getCategoryNameFromId(String categoryId) {
        switch (categoryId) {
            case "direct_food": return "Direct Food Sale";
            case "pesticides": return "Pesticides";
            case "rental": return "Equipment Rental";
            case "seeds": return "Seeds & Fertilizers";
            case "labor": return "Labor Services";
            case "others": return "Other Items";
            default: return "Other Items";
        }
    }
}
