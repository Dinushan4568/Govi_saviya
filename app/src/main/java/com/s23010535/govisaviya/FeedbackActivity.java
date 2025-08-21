package com.s23010535.govisaviya;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import android.content.SharedPreferences;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class FeedbackActivity extends Activity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_READ_IMAGES = 100;

    // UI Components
    private ImageButton backButton;
    private ImageButton[] starButtons;
    private TextView ratingText;
    private Spinner categorySpinner;
    private EditText feedbackEditText;
    private TextView charCountText;
    private Button attachImageButton;
    private ImageView selectedImageView;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button submitButton;
    private LinearLayout successLayout;

    // Data
    private int currentRating = 0;
    private Uri selectedImageUri;
    private String selectedCategory = "";
    private String[] categories = {
        "General Feedback",
        "Bug Report",
        "Feature Request",
        "User Experience",
        "Performance Issue",
        "Content Suggestion",
        "Other"
    };

    private static final String PREFS_NAME = "feedback_prefs";
    private static final String KEY_FEEDBACK_HISTORY = "feedback_history";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        
        initializeViews();
        setupStarRating();
        setupCategorySpinner();
        setupTextWatchers();
        setupClickListeners();
    }

    private void initializeViews() {
        backButton = findViewById(R.id.backButton);
        ratingText = findViewById(R.id.ratingText);
        categorySpinner = findViewById(R.id.categorySpinner);
        feedbackEditText = findViewById(R.id.feedbackEditText);
        charCountText = findViewById(R.id.charCountText);
        attachImageButton = findViewById(R.id.attachImageButton);
        selectedImageView = findViewById(R.id.selectedImageView);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        submitButton = findViewById(R.id.submitButton);
        successLayout = findViewById(R.id.successLayout);

        // Initialize star buttons array
        starButtons = new ImageButton[5];
        starButtons[0] = findViewById(R.id.star1);
        starButtons[1] = findViewById(R.id.star2);
        starButtons[2] = findViewById(R.id.star3);
        starButtons[3] = findViewById(R.id.star4);
        starButtons[4] = findViewById(R.id.star5);
    }

    private void setupStarRating() {
        for (int i = 0; i < starButtons.length; i++) {
            final int starIndex = i + 1;
            starButtons[i].setOnClickListener(v -> {
                setRating(starIndex);
            });
        }
    }

    private void setRating(int rating) {
        currentRating = rating;
        
        // Update star images
        for (int i = 0; i < starButtons.length; i++) {
            if (i < rating) {
                starButtons[i].setImageResource(R.drawable.ic_star_filled);
            } else {
                starButtons[i].setImageResource(R.drawable.ic_star_outline);
            }
        }

        // Update rating text
        String[] ratingDescriptions = {
            "Tap to rate",
            "Poor",
            "Fair", 
            "Good",
            "Very Good",
            "Excellent"
        };
        ratingText.setText(ratingDescriptions[rating]);
    }

    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this, 
            android.R.layout.simple_spinner_item, 
            categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = categories[0];
            }
        });
    }

    private void setupTextWatchers() {
        feedbackEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                int charCount = s.length();
                charCountText.setText(charCount + "/500");
                
                // Change color when approaching limit
                if (charCount > 450) {
                    charCountText.setTextColor(getResources().getColor(android.R.color.holo_red_dark, getTheme()));
                } else if (charCount > 400) {
                    charCountText.setTextColor(getResources().getColor(android.R.color.holo_orange_dark, getTheme()));
                } else {
                    charCountText.setTextColor(getResources().getColor(android.R.color.darker_gray, getTheme()));
                }
            }
        });
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        
        attachImageButton.setOnClickListener(v -> selectImage());
        
        submitButton.setOnClickListener(v -> submitFeedback());
    }

    private boolean hasReadImagesPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            return checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestReadImagesPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_READ_IMAGES);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_READ_IMAGES);
        }
    }

    private void selectImage() {
        if (!hasReadImagesPermission()) {
            requestReadImagesPermission();
            return;
        }
        
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    private void submitFeedback() {
        // Validate input
        if (currentRating == 0) {
            showErrorDialog("Please provide a rating");
            return;
        }

        String feedbackText = feedbackEditText.getText().toString().trim();
        if (feedbackText.isEmpty()) {
            showErrorDialog("Please enter your feedback");
            return;
        }

        if (feedbackText.length() < 10) {
            showErrorDialog("Please provide more detailed feedback (at least 10 characters)");
            return;
        }

        // Create feedback object
        FeedbackItem feedback = new FeedbackItem(
            currentRating,
            selectedCategory,
            feedbackText,
            emailEditText.getText().toString().trim(),
            phoneEditText.getText().toString().trim(),
            selectedImageUri != null ? selectedImageUri.toString() : null,
            getCurrentTime()
        );

        // Save feedback
        saveFeedback(feedback);

        // Show success message
        showSuccessMessage();
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show();
    }

    private void showSuccessMessage() {
        // Hide main content and show success layout
        View mainContent = findViewById(R.id.mainContent);
        if (mainContent != null) {
            mainContent.setVisibility(View.GONE);
        }
        successLayout.setVisibility(View.VISIBLE);

        // Auto-hide after 3 seconds and go back
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            finish();
        }, 3000);
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    private void saveFeedback(FeedbackItem feedback) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String existingJson = prefs.getString(KEY_FEEDBACK_HISTORY, "[]");
        
        try {
            JSONArray feedbackArray = new JSONArray(existingJson);
            feedbackArray.put(feedback.toJson());
            
            prefs.edit()
                .putString(KEY_FEEDBACK_HISTORY, feedbackArray.toString())
                .apply();
                
        } catch (JSONException e) {
            // Handle error
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            selectedImageView.setImageURI(selectedImageUri);
            selectedImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_IMAGES) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Feedback data class
    public static class FeedbackItem {
        public int rating;
        public String category;
        public String feedback;
        public String email;
        public String phone;
        public String imageUri;
        public String timestamp;

        public FeedbackItem(int rating, String category, String feedback, String email, String phone, String imageUri, String timestamp) {
            this.rating = rating;
            this.category = category;
            this.feedback = feedback;
            this.email = email;
            this.phone = phone;
            this.imageUri = imageUri;
            this.timestamp = timestamp;
        }

        public JSONObject toJson() throws JSONException {
            JSONObject obj = new JSONObject();
            obj.put("rating", rating);
            obj.put("category", category);
            obj.put("feedback", feedback);
            obj.put("email", email);
            obj.put("phone", phone);
            obj.put("imageUri", imageUri == null ? JSONObject.NULL : imageUri);
            obj.put("timestamp", timestamp);
            return obj;
        }

        public static FeedbackItem fromJson(JSONObject obj) throws JSONException {
            return new FeedbackItem(
                obj.getInt("rating"),
                obj.getString("category"),
                obj.getString("feedback"),
                obj.getString("email"),
                obj.getString("phone"),
                obj.isNull("imageUri") ? null : obj.getString("imageUri"),
                obj.getString("timestamp")
            );
        }
    }
}

