package com.s23010535.govisaviya;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.*;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.s23010535.govisaviya.data.SessionManager;

public class CommunityActivity extends Activity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private static final int PERMISSION_REQUEST_READ_IMAGES = 101;

    private EditText messageEditText;
    private ImageButton sendButton, backButton, notificationButton;
    private Button whatsappButton, facebookButton, imageButton, cameraButton, quickPostButton;
    private RecyclerView messagesRecyclerView;
    private ImageView selectedImageView;
    private Uri selectedImageUri;
    private List<CommunityMessage> messages;
    private CommunityAdapter adapter;
    private SessionManager sessionManager;

    private static final String PREFS_NAME = "community_prefs";
    private static final String KEY_MESSAGES = "messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        
        initializeViews();
        initializeData();
        setupClickListeners();
        loadMessagesFromStorage();
    }

    private void initializeViews() {
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        backButton = findViewById(R.id.backButton);
        notificationButton = findViewById(R.id.notificationButton);
        whatsappButton = findViewById(R.id.whatsappButton);
        facebookButton = findViewById(R.id.facebookButton);
        imageButton = findViewById(R.id.imageButton);
        cameraButton = findViewById(R.id.cameraButton);
        quickPostButton = findViewById(R.id.quickPostButton);
        selectedImageView = findViewById(R.id.selectedImageView);
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        
        // Initialize RecyclerView
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setBackgroundColor(getResources().getColor(android.R.color.transparent, getTheme()));
    }

    private void initializeData() {
        messages = new ArrayList<>();
        adapter = new CommunityAdapter(messages);
        messagesRecyclerView.setAdapter(adapter);
        sessionManager = new SessionManager(this);
    }

    private void setupClickListeners() {
        sendButton.setOnClickListener(v -> sendMessage());
        backButton.setOnClickListener(v -> finish());
        notificationButton.setOnClickListener(v -> showNotifications());
        whatsappButton.setOnClickListener(v -> openWhatsApp());
        facebookButton.setOnClickListener(v -> openFacebook());
        imageButton.setOnClickListener(v -> selectImage());
        cameraButton.setOnClickListener(v -> capturePhoto());
        quickPostButton.setOnClickListener(v -> showQuickPostDialog());
    }

    private void showNotifications() {
        Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show();
    }

    private void openWhatsApp() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://wa.me/your-community-group"));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFacebook() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://facebook.com/your-community-page"));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Facebook not available", Toast.LENGTH_SHORT).show();
        }
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

    private void capturePhoto() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private void showQuickPostDialog() {
        String[] options = {"💡 Share Problem", "🌱 Share Experience", "💰 Sell/Buy"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quick Post");
        builder.setItems(options, (dialog, which) -> {
            String selectedOption = options[which];
            messageEditText.setText(selectedOption + ": ");
            messageEditText.requestFocus();
        });
        builder.show();
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (messageText.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean hasImg = selectedImageUri != null;
        String userName = (sessionManager != null && sessionManager.isLoggedIn() && sessionManager.getUsername() != null && !sessionManager.getUsername().isEmpty())
                ? sessionManager.getUsername()
                : "You";
        addMessage(userName, messageText, getCurrentTime(), hasImg, 0, 0, hasImg ? selectedImageUri.toString() : null);
        messageEditText.setText("");
        // Clear selected image preview state after sending
        selectedImageUri = null;
        selectedImageView.setVisibility(View.GONE);
        saveMessagesToStorage();
        Toast.makeText(this, "Message sent to community!", Toast.LENGTH_SHORT).show();
    }
    private void addMessage(String userName, String message, String time, boolean hasImage, int likes, int replies, String imageUri) {
        CommunityMessage msg = new CommunityMessage(userName, message, time, hasImage, likes, replies, imageUri);
        messages.add(0, msg);
        adapter.notifyItemInserted(0);
        messagesRecyclerView.scrollToPosition(0);
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                selectedImageUri = data.getData();
                selectedImageView.setImageURI(selectedImageUri);
                selectedImageView.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                selectedImageUri = null;
                selectedImageView.setVisibility(View.GONE);
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (photo != null) {
                    Uri photoUri = saveBitmapToInternalStorage(photo);
                    if (photoUri != null) {
                        selectedImageUri = photoUri;
                        selectedImageView.setImageURI(selectedImageUri);
                        selectedImageView.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(this, "Error saving photo", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error processing photo", Toast.LENGTH_SHORT).show();
                selectedImageUri = null;
                selectedImageView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                capturePhoto();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_READ_IMAGES) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Uri saveBitmapToInternalStorage(Bitmap bitmap) {
        try {
            File imagesDir = new File(getFilesDir(), "community_images");
            if (!imagesDir.exists()) {
                if (!imagesDir.mkdirs()) {
                    return null;
                }
            }
            String fileName = "IMG_" + System.currentTimeMillis() + ".png";
            File imageFile = new File(imagesDir, fileName);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                return Uri.fromFile(imageFile);
            } catch (IOException e) {
                return null;
            } finally {
                if (fos != null) {
                    try { fos.close(); } catch (IOException ignored) {}
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static class CommunityMessage {
        public String userName;
        public String message;
        public String time;
        public boolean hasImage;
        public int likes;
        public int replies;
        public String imageUri;

        public CommunityMessage(String userName, String message, String time, boolean hasImage, int likes, int replies) {
            this(userName, message, time, hasImage, likes, replies, null);
        }

        public CommunityMessage(String userName, String message, String time, boolean hasImage, int likes, int replies, String imageUri) {
            this.userName = userName;
            this.message = message;
            this.time = time;
            this.hasImage = hasImage;
            this.likes = likes;
            this.replies = replies;
            this.imageUri = imageUri;
        }

        public JSONObject toJson() throws JSONException {
            JSONObject obj = new JSONObject();
            obj.put("userName", userName);
            obj.put("message", message);
            obj.put("time", time);
            obj.put("hasImage", hasImage);
            obj.put("likes", likes);
            obj.put("replies", replies);
            obj.put("imageUri", imageUri == null ? JSONObject.NULL : imageUri);
            return obj;
        }

        public static CommunityMessage fromJson(JSONObject obj) throws JSONException {
            String userName = obj.optString("userName", "User");
            String message = obj.optString("message", "");
            String time = obj.optString("time", "");
            boolean hasImage = obj.optBoolean("hasImage", false);
            int likes = obj.optInt("likes", 0);
            int replies = obj.optInt("replies", 0);
            String imageUri = obj.isNull("imageUri") ? null : obj.optString("imageUri", null);
            return new CommunityMessage(userName, message, time, hasImage, likes, replies, imageUri);
        }
    }

    private void loadMessagesFromStorage() {
        try {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String json = prefs.getString(KEY_MESSAGES, null);
            messages.clear();
            if (json != null) {
                try {
                    JSONArray arr = new JSONArray(json);
                    for (int i = 0; i < arr.length(); i++) {
                        try {
                            JSONObject obj = arr.getJSONObject(i);
                            messages.add(CommunityMessage.fromJson(obj));
                        } catch (JSONException e) {
                            // Skip malformed individual messages
                        }
                    }
                } catch (JSONException e) {
                    // Clear corrupted data
                    prefs.edit().remove(KEY_MESSAGES).apply();
                }
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            // If there's any error, start with empty messages
            messages.clear();
            adapter.notifyDataSetChanged();
        }
    }

    private void saveMessagesToStorage() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        JSONArray arr = new JSONArray();
        for (CommunityMessage m : messages) {
            try {
                arr.put(m.toJson());
            } catch (JSONException ignored) {}
        }
        prefs.edit().putString(KEY_MESSAGES, arr.toString()).apply();
    }

    private class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.MessageViewHolder> {
        private List<CommunityMessage> messages;

        public CommunityAdapter(List<CommunityMessage> messages) {
            this.messages = messages;
        }

        @Override
        public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_community_message, parent, false);
            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MessageViewHolder holder, int position) {
            CommunityMessage message = messages.get(position);

            holder.userName.setText(message.userName);
            holder.timeText.setText(" • " + message.time);
            holder.messageText.setText(message.message);

            if (message.imageUri != null && !message.imageUri.isEmpty()) {
                try {
                    Uri imageUri = Uri.parse(message.imageUri);
                    // Check if the URI is still accessible
                    if (imageUri != null) {
                        holder.messageImage.setVisibility(View.VISIBLE);
                        holder.messageImage.setImageURI(imageUri);
                    } else {
                        holder.messageImage.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    // If there's any error loading the image, hide it
                    holder.messageImage.setVisibility(View.GONE);
                }
            } else {
                holder.messageImage.setVisibility(View.GONE);
            }

            // Set different colors for different user types
            if (message.userName.equals("Expert Advisor")) {
                holder.userName.setTextColor(getResources().getColor(android.R.color.holo_green_dark, getTheme()));
            } else if (message.userName.equals("Community Admin")) {
                holder.userName.setTextColor(getResources().getColor(android.R.color.holo_blue_dark, getTheme()));
            } else if (message.userName.equals("You")) {
                holder.userName.setTextColor(getResources().getColor(android.R.color.holo_orange_dark, getTheme()));
            } else {
                holder.userName.setTextColor(getResources().getColor(android.R.color.black, getTheme()));
            }
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        class MessageViewHolder extends RecyclerView.ViewHolder {
            TextView userName, timeText, messageText;
            ImageView messageImage;
            public MessageViewHolder(View itemView) {
                super(itemView);
                userName = itemView.findViewById(R.id.userName);
                timeText = itemView.findViewById(R.id.timeText);
                messageText = itemView.findViewById(R.id.messageText);
                messageImage = itemView.findViewById(R.id.messageImage);
            }
        }
    }
} 