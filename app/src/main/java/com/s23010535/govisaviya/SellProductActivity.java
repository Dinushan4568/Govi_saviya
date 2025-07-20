package com.s23010535.govisaviya;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.s23010535.govisaviya.data.ProductDataManager;
import com.s23010535.govisaviya.models.Product;

import java.io.IOException;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

public class SellProductActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 1001;
    private static final int REQUEST_IMAGE_CAPTURE = 1002;
    private static final int REQUEST_CAMERA_PERMISSION = 2001;

    private ImageView ivProductImage;
    private Button btnPickImage, btnSubmit;
    private EditText etProductName, etDescription, etPrice, etQuantity, etLocation;
    private ChipGroup chipGroupCategory;
    private Uri selectedImageUri;
    private String selectedCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_product);

        ivProductImage = findViewById(R.id.ivProductImage);
        btnPickImage = findViewById(R.id.btnPickImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        etProductName = findViewById(R.id.etProductName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        etQuantity = findViewById(R.id.etQuantity);
        etLocation = findViewById(R.id.etLocation);
        chipGroupCategory = findViewById(R.id.chipGroupCategory);

        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitProduct();
            }
        });
    }

    private void pickImage() {
        String[] options = {"Choose from Gallery", "Take Photo"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Add Product Image");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            } else if (which == 1) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK && data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                ivProductImage.setImageURI(selectedImageUri);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ivProductImage.setImageBitmap(imageBitmap);
                // Save bitmap to cache and get its URI
                selectedImageUri = saveBitmapToCache(imageBitmap);
            }
        }
    }

    private Uri saveBitmapToCache(Bitmap bitmap) {
        try {
            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs();
            String fileName = "product_" + UUID.randomUUID().toString() + ".jpg";
            File file = new File(cachePath, fileName);
            OutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            stream.flush();
            stream.close();
            return Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void submitProduct() {
        String name = etProductName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        double price = 0;
        int quantity = 0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (Exception e) {
            // ignore, will show error below
        }
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (Exception e) {
            // ignore, will show error below
        }
        int checkedChipId = chipGroupCategory.getCheckedChipId();
        if (checkedChipId == View.NO_ID) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }
        Chip selectedChip = findViewById(checkedChipId);
        String category = selectedChip.getText().toString();
        String categoryId = getCategoryIdFromName(category);

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || price <= 0 || quantity <= 0 || TextUtils.isEmpty(location)) {
            Toast.makeText(this, "Please fill all fields and enter valid price, quantity, and location", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImageUri == null) {
            Toast.makeText(this, "Please add a product image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create and save product
        Product product = new Product(name, description, price, categoryId, "seller001", "You");
        product.setImageUri(selectedImageUri.toString());
        product.setStockQuantity(quantity);
        product.setSellerLocation(location);
        boolean success = ProductDataManager.getInstance(this).addProduct(product);
        if (success) {
            Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add product. Try again.", Toast.LENGTH_SHORT).show();
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
} 