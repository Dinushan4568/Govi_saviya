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

public class CommunityActivity extends Activity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    private EditText messageEditText;
    private ImageButton sendButton, backButton, notificationButton;
    private Button whatsappButton, facebookButton, imageButton, cameraButton, quickPostButton;
    private RecyclerView messagesRecyclerView;
    private ImageView selectedImageView;
    private Uri selectedImageUri;
    private List<CommunityMessage> messages;
    private CommunityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        
        initializeViews();
        initializeData();
        setupClickListeners();
        addSampleMessages();
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
        messagesRecyclerView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void initializeData() {
        messages = new ArrayList<>();
        adapter = new CommunityAdapter(messages);
        messagesRecyclerView.setAdapter(adapter);
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

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    private void capturePhoto() {
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

    private void addSampleMessages() {
        addMessage("Farmer John", "Just harvested my paddy field today! The yield looks promising.", "14:30", false, 12, 3);
        addMessage("Maria Silva", "Having trouble with pest control in my vegetable garden.", "14:25", false, 8, 5);
        addMessage("Expert Advisor", "🌾 Tip: Always check soil moisture before irrigation.", "14:15", false, 25, 7);
        addMessage("Local Farmer", "Best time to plant tomatoes in this season?", "14:10", false, 15, 8);
        addMessage("Community Admin", "Welcome to our farming community! Share your experiences.", "14:05", false, 30, 12);
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (messageText.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        addMessage("You", messageText, getCurrentTime(), false, 0, 0);
        messageEditText.setText("");
        Toast.makeText(this, "Message sent to community!", Toast.LENGTH_SHORT).show();
    }

    private void addMessage(String userName, String message, String time, boolean hasImage, int likes, int replies) {
        CommunityMessage msg = new CommunityMessage(userName, message, time, hasImage, likes, replies);
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
            selectedImageUri = data.getData();
            selectedImageView.setImageURI(selectedImageUri);
            selectedImageView.setVisibility(View.VISIBLE);
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            selectedImageView.setImageBitmap(photo);
            selectedImageView.setVisibility(View.VISIBLE);
        }
    }

    public static class CommunityMessage {
        public String userName;
        public String message;
        public String time;
        public boolean hasImage;
        public int likes;
        public int replies;

        public CommunityMessage(String userName, String message, String time, boolean hasImage, int likes, int replies) {
            this.userName = userName;
            this.message = message;
            this.time = time;
            this.hasImage = hasImage;
            this.likes = likes;
            this.replies = replies;
        }
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
            
            // Set different colors for different user types
            if (message.userName.equals("Expert Advisor")) {
                holder.userName.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else if (message.userName.equals("Community Admin")) {
                holder.userName.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            } else if (message.userName.equals("You")) {
                holder.userName.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            }
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        class MessageViewHolder extends RecyclerView.ViewHolder {
            TextView userName, timeText, messageText;

            MessageViewHolder(View itemView) {
                super(itemView);
                userName = itemView.findViewById(R.id.userName);
                timeText = itemView.findViewById(R.id.timeText);
                messageText = itemView.findViewById(R.id.messageText);
            }
        }
    }
} 