package com.s23010535.govisaviya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ScrollView;
import androidx.cardview.widget.CardView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommunityActivity extends Activity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    private EditText messageEditText;
    private Button sendButton;
    private Button imageButton;
    private Button cameraButton;
    private Button whatsappButton;
    private Button facebookButton;
    private LinearLayout messagesContainer;
    private ScrollView messagesScrollView;
    private ImageView selectedImageView;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        initializeViews();
        setupClickListeners();
        loadSampleMessages();
    }

    private void initializeViews() {
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        imageButton = findViewById(R.id.imageButton);
        cameraButton = findViewById(R.id.cameraButton);
        whatsappButton = findViewById(R.id.whatsappButton);
        facebookButton = findViewById(R.id.facebookButton);
        messagesContainer = findViewById(R.id.messagesContainer);
        messagesScrollView = findViewById(R.id.messagesScrollView);
        selectedImageView = findViewById(R.id.selectedImageView);
    }

    private void setupClickListeners() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        whatsappButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebook();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();

        if (messageText.isEmpty() && selectedImageUri == null) {
            Toast.makeText(this, "කරුණාකර පණිවිඩයක් ටයිප් කරන්න හෝ පින්තූරයක් තෝරන්න", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create message view
        addMessageToChat("ඔබ", messageText, getCurrentTime(), selectedImageUri != null);

        // Clear input
        messageEditText.setText("");
        selectedImageView.setVisibility(View.GONE);
        selectedImageUri = null;

        // Scroll to bottom
        messagesScrollView.post(new Runnable() {
            @Override
            public void run() {
                messagesScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        Toast.makeText(this, "පණිවිඩය යවන ලදී", Toast.LENGTH_SHORT).show();
    }

    private void addMessageToChat(String userName, String message, String time, boolean hasImage) {
        // Create message card
        CardView messageCard = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(16, 8, 16, 8);
        messageCard.setLayoutParams(cardParams);
        messageCard.setRadius(12);
        messageCard.setCardElevation(4);
        messageCard.setCardBackgroundColor(getResources().getColor(android.R.color.white));

        // Create inner layout
        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        innerLayout.setPadding(16, 12, 16, 12);

        // User name and time header
        LinearLayout headerLayout = new LinearLayout(this);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView userNameText = new TextView(this);
        userNameText.setText(userName);
        userNameText.setTextSize(14);
        userNameText.setTextColor(getResources().getColor(android.R.color.black));
        userNameText.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView timeText = new TextView(this);
        timeText.setText(time);
        timeText.setTextSize(12);
        timeText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        timeParams.setMargins(16, 0, 0, 0);
        timeText.setLayoutParams(timeParams);

        headerLayout.addView(userNameText);
        headerLayout.addView(timeText);

        // Message text
        TextView messageText = new TextView(this);
        messageText.setText(message);
        messageText.setTextSize(16);
        messageText.setTextColor(getResources().getColor(android.R.color.black));
        LinearLayout.LayoutParams messageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        messageParams.setMargins(0, 8, 0, 0);
        messageText.setLayoutParams(messageParams);

        // Add image placeholder if has image
        if (hasImage) {
            TextView imageIndicator = new TextView(this);
            imageIndicator.setText("📷 පින්තූරය ඇමුණුම් කර ඇත");
            imageIndicator.setTextSize(14);
            imageIndicator.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            imageParams.setMargins(0, 8, 0, 0);
            imageIndicator.setLayoutParams(imageParams);
            innerLayout.addView(imageIndicator);
        }

        // Action buttons
        LinearLayout actionLayout = new LinearLayout(this);
        actionLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams actionParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        actionParams.setMargins(0, 12, 0, 0);
        actionLayout.setLayoutParams(actionParams);

        Button likeButton = new Button(this);
        likeButton.setText("❤️ ආදරය");
        likeButton.setTextSize(12);
        likeButton.setBackground(null);
        likeButton.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

        Button replyButton = new Button(this);
        replyButton.setText("💬 පිළිතුරු");
        replyButton.setTextSize(12);
        replyButton.setBackground(null);
        replyButton.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));

        actionLayout.addView(likeButton);
        actionLayout.addView(replyButton);

        // Add all views to inner layout
        innerLayout.addView(headerLayout);
        innerLayout.addView(messageText);
        innerLayout.addView(actionLayout);

        messageCard.addView(innerLayout);
        messagesContainer.addView(messageCard);
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "පින්තූරයක් තෝරන්න"), PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    private void openWhatsApp() {
        try {
            String message = "Govi Saviya ප්‍රජාවට සම්බන්ධ වන්න! 🌾\n\nගොවි ප්‍රජාවේ සාමාජිකයන් සමඟ අත්දැකීම් හුවමාරු කර ගන්න.";
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(whatsappIntent);
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp ස්ථාපනය කර නැත", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFacebook() {
        try {
            String facebookUrl = "https://www.facebook.com/sharer/sharer.php?u=https://govisaviya.lk";
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
            startActivity(facebookIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Facebook ස්ථාපනය කර නැත", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void loadSampleMessages() {
        // Add some sample messages
        addMessageToChat("කමල් සිල්වා - කුරුණෑගල", "අද මගේ කොකනට් වගාවේ හොඳ අස්වැන්නක් ලැබුණා. කවුරුහරි ගන්න කැමතිද?", "10:30", false);
        addMessageToChat("සුනිල් ගුණවර්ධන - අනුරාධපුරය", "පළිබෝධ නාශක භාවිතය ගැන ප්‍රශ්නයක් තියෙනවා. කවුරුහරි උදව් කරන්න පුළුවන්ද?", "11:15", false);
        addMessageToChat("මාලිනී පෙරේරා - මාතර", "ජෛවික පොහොර සකස් කරන ක්‍රමයක් දන්නවා. අවශ්‍ය අයට කියන්න පුළුවන්.", "12:00", true);
        addMessageToChat("රාජ් කුමාර - හම්බන්තොට", "වතුර කළමනාකරණය ගැන විශේෂඥ උපදෙස් අවශ්‍යයි. කොහෙන්ද ලබා ගන්නේ?", "13:45", false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                selectedImageView.setImageURI(selectedImageUri);
                selectedImageView.setVisibility(View.VISIBLE);
                Toast.makeText(this, "පින්තූරය තෝරා ගන්නා ලදී", Toast.LENGTH_SHORT).show();
            } else if (requestCode == CAMERA_REQUEST && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                selectedImageView.setImageBitmap(imageBitmap);
                selectedImageView.setVisibility(View.VISIBLE);
                Toast.makeText(this, "ඡායාරූපය ගන්නා ලදී", Toast.LENGTH_SHORT).show();
            }
        }
    }
}