package com.s23010535.govisaviya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommunityActivity extends Activity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    private LinearLayout rootLayout;
    private EditText messageEditText;
    private Button sendButton, imageButton, cameraButton;
    private RecyclerView messagesRecyclerView;
    private ImageView selectedImageView;
    private Uri selectedImageUri;
    private List<CommunityMessage> messages;
    private CommunityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createLayout();
        initializeData();
        setupClickListeners();
        addSampleMessages();
    }

    private void createLayout() {
        rootLayout = new LinearLayout(this);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(Color.parseColor("#F8F9FA"));

        createHeader();
        createStatsSection();
        createMessagesSection();
        createInputSection();

        setContentView(rootLayout);
    }

    private void createHeader() {
        LinearLayout headerContainer = new LinearLayout(this);
        headerContainer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        headerContainer.setOrientation(LinearLayout.VERTICAL);
        headerContainer.setBackground(createGradientBackground("#4CAF50", "#45A049"));
        headerContainer.setElevation(8);

        LinearLayout mainHeader = new LinearLayout(this);
        mainHeader.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mainHeader.setOrientation(LinearLayout.HORIZONTAL);
        mainHeader.setPadding(20, 16, 20, 16);
        mainHeader.setGravity(Gravity.CENTER_VERTICAL);

        ImageButton backButton = new ImageButton(this);
        backButton.setLayoutParams(new LinearLayout.LayoutParams(48, 48));
        backButton.setImageResource(android.R.drawable.ic_menu_revert);
        backButton.setBackground(createRoundedBackground(Color.TRANSPARENT, 24));
        backButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        backButton.setOnClickListener(v -> finish());

        TextView titleText = new TextView(this);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        titleParams.setMargins(16, 0, 0, 0);
        titleText.setLayoutParams(titleParams);
        titleText.setText("🌾 Govi Saviya Community");
        titleText.setTextSize(20);
        titleText.setTypeface(null, Typeface.BOLD);
        titleText.setTextColor(Color.WHITE);

        LinearLayout onlineIndicator = new LinearLayout(this);
        onlineIndicator.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        onlineIndicator.setOrientation(LinearLayout.HORIZONTAL);
        onlineIndicator.setBackground(createRoundedBackground(Color.parseColor("#66BB6A"), 16));
        onlineIndicator.setPadding(12, 6, 12, 6);
        onlineIndicator.setGravity(Gravity.CENTER_VERTICAL);

        View onlineDot = new View(this);
        onlineDot.setLayoutParams(new LinearLayout.LayoutParams(8, 8));
        onlineDot.setBackground(createRoundedBackground(Color.WHITE, 4));

        TextView onlineCountText = new TextView(this);
        onlineCountText.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        onlineCountText.setText(" 47 online");
        onlineCountText.setTextSize(12);
        onlineCountText.setTextColor(Color.WHITE);
        onlineCountText.setTypeface(null, Typeface.BOLD);

        onlineIndicator.addView(onlineDot);
        onlineIndicator.addView(onlineCountText);

        mainHeader.addView(backButton);
        mainHeader.addView(titleText);
        mainHeader.addView(onlineIndicator);

        headerContainer.addView(mainHeader);
        rootLayout.addView(headerContainer);
    }

    private void createStatsSection() {
        LinearLayout statsContainer = new LinearLayout(this);
        statsContainer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        statsContainer.setOrientation(LinearLayout.HORIZONTAL);
        statsContainer.setBackgroundColor(Color.WHITE);
        statsContainer.setElevation(4);
        statsContainer.setPadding(20, 16, 20, 16);

        LinearLayout usersStat = createStatItem("👥", "Total Users", "1,247", "#4CAF50");
        LinearLayout discussionsStat = createStatItem("💬", "Active Discussions", "89", "#2196F3");
        LinearLayout postsStat = createStatItem("📝", "Today's Posts", "156", "#FF9800");

        statsContainer.addView(usersStat);
        statsContainer.addView(discussionsStat);
        statsContainer.addView(postsStat);
        rootLayout.addView(statsContainer);
    }

    private LinearLayout createStatItem(String icon, String label, String value, String color) {
        LinearLayout container = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        container.setLayoutParams(params);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setGravity(Gravity.CENTER);

        TextView iconText = new TextView(this);
        iconText.setText(icon);
        iconText.setTextSize(24);
        iconText.setGravity(Gravity.CENTER);

        TextView valueText = new TextView(this);
        valueText.setText(value);
        valueText.setTextSize(18);
        valueText.setTypeface(null, Typeface.BOLD);
        valueText.setTextColor(Color.parseColor(color));
        valueText.setGravity(Gravity.CENTER);

        TextView labelText = new TextView(this);
        labelText.setText(label);
        labelText.setTextSize(12);
        labelText.setTextColor(Color.parseColor("#666666"));
        labelText.setGravity(Gravity.CENTER);

        container.addView(iconText);
        container.addView(valueText);
        container.addView(labelText);
        return container;
    }

    private void createMessagesSection() {
        LinearLayout messagesContainer = new LinearLayout(this);
        messagesContainer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0, 1));
        messagesContainer.setOrientation(LinearLayout.VERTICAL);
        messagesContainer.setBackgroundColor(Color.parseColor("#F8F9FA"));

        LinearLayout sectionHeader = new LinearLayout(this);
        sectionHeader.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        sectionHeader.setOrientation(LinearLayout.HORIZONTAL);
        sectionHeader.setBackgroundColor(Color.WHITE);
        sectionHeader.setPadding(20, 12, 20, 12);
        sectionHeader.setElevation(2);

        TextView sectionTitle = new TextView(this);
        sectionTitle.setText("💬 Community Discussions");
        sectionTitle.setTextSize(16);
        sectionTitle.setTypeface(null, Typeface.BOLD);
        sectionTitle.setTextColor(Color.parseColor("#333333"));

        sectionHeader.addView(sectionTitle);

        messagesRecyclerView = new RecyclerView(this);
        messagesRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setPadding(8, 8, 8, 8);

        messagesContainer.addView(sectionHeader);
        messagesContainer.addView(messagesRecyclerView);
        rootLayout.addView(messagesContainer);
    }

    private void createInputSection() {
        LinearLayout inputContainer = new LinearLayout(this);
        inputContainer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        inputContainer.setOrientation(LinearLayout.VERTICAL);
        inputContainer.setBackgroundColor(Color.WHITE);
        inputContainer.setElevation(8);
        inputContainer.setPadding(16, 16, 16, 16);

        selectedImageView = new ImageView(this);
        selectedImageView.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
        selectedImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        selectedImageView.setBackground(createRoundedBackground(Color.parseColor("#E0E0E0"), 8));
        selectedImageView.setVisibility(View.GONE);
        selectedImageView.setPadding(8, 8, 8, 8);

        LinearLayout inputRow = new LinearLayout(this);
        inputRow.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        inputRow.setOrientation(LinearLayout.HORIZONTAL);
        inputRow.setGravity(Gravity.CENTER_VERTICAL);

        messageEditText = new EditText(this);
        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        editParams.setMargins(0, 0, 12, 0);
        messageEditText.setLayoutParams(editParams);
        messageEditText.setHint("Share your farming experience...");
        messageEditText.setPadding(16, 12, 16, 12);
        messageEditText.setBackground(createRoundedBackground(Color.parseColor("#F5F5F5"), 24));
        messageEditText.setTextSize(16);
        messageEditText.setMaxLines(3);

        sendButton = new Button(this);
        sendButton.setLayoutParams(new LinearLayout.LayoutParams(48, 48));
        sendButton.setText("📤");
        sendButton.setTextSize(18);
        sendButton.setBackground(createRoundedBackground(Color.parseColor("#4CAF50"), 24));
        sendButton.setTextColor(Color.WHITE);

        inputRow.addView(messageEditText);
        inputRow.addView(sendButton);

        inputContainer.addView(selectedImageView);
        inputContainer.addView(inputRow);
        rootLayout.addView(inputContainer);
    }

    private void initializeData() {
        messages = new ArrayList<>();
        adapter = new CommunityAdapter(messages);
        messagesRecyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void addSampleMessages() {
        addMessage("Farmer John", "Just harvested my paddy field today! The yield looks promising.", "14:30", false, 12, 3);
        addMessage("Maria Silva", "Having trouble with pest control in my vegetable garden.", "14:25", false, 8, 5);
        addMessage("Expert Advisor", "🌾 Tip: Always check soil moisture before irrigation.", "14:15", false, 25, 7);
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

    private GradientDrawable createGradientBackground(String startColor, String endColor) {
        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor(startColor), Color.parseColor(endColor)}
        );
        return gradient;
    }

    private GradientDrawable createRoundedBackground(int color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    private GradientDrawable createRoundedBackground(String color, float radius) {
        return createRoundedBackground(Color.parseColor(color), radius);
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
            CardView cardView = new CardView(CommunityActivity.this);
            cardView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            cardView.setRadius(16);
            cardView.setCardElevation(4);
            cardView.setUseCompatPadding(true);
            cardView.setCardBackgroundColor(Color.WHITE);

            LinearLayout layout = new LinearLayout(CommunityActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(20, 20, 20, 20);

            LinearLayout header = new LinearLayout(CommunityActivity.this);
            header.setOrientation(LinearLayout.HORIZONTAL);
            header.setGravity(Gravity.CENTER_VERTICAL);

            TextView userName = new TextView(CommunityActivity.this);
            userName.setTextSize(16);
            userName.setTypeface(null, Typeface.BOLD);
            userName.setTextColor(Color.parseColor("#333333"));

            TextView timeText = new TextView(CommunityActivity.this);
            timeText.setTextSize(12);
            timeText.setTextColor(Color.parseColor("#666666"));

            LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            userName.setLayoutParams(nameParams);

            header.addView(userName);
            header.addView(timeText);

            TextView messageText = new TextView(CommunityActivity.this);
            messageText.setTextSize(14);
            messageText.setTextColor(Color.parseColor("#333333"));
            messageText.setPadding(0, 8, 0, 0);

            layout.addView(header);
            layout.addView(messageText);

            cardView.addView(layout);

            return new MessageViewHolder(cardView, userName, timeText, messageText);
        }

        @Override
        public void onBindViewHolder(MessageViewHolder holder, int position) {
            CommunityMessage message = messages.get(position);
            
            holder.userName.setText(message.userName);
            holder.timeText.setText(" • " + message.time);
            holder.messageText.setText(message.message);
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        class MessageViewHolder extends RecyclerView.ViewHolder {
            TextView userName, timeText, messageText;

            MessageViewHolder(View itemView, TextView userName, TextView timeText, TextView messageText) {
                super(itemView);
                this.userName = userName;
                this.timeText = timeText;
                this.messageText = messageText;
            }
        }
    }
} 