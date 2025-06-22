// CommunityActivity.java – UI only (no sample messages, ready for database integration)
package com.s23010535.govisaviya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.cardview.widget.CardView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommunityActivity extends Activity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    private EditText messageEditText;
    private Button sendButton, imageButton, cameraButton, whatsappButton, facebookButton;
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
        sendButton.setOnClickListener(v -> sendMessage());
        imageButton.setOnClickListener(v -> openImagePicker());
        cameraButton.setOnClickListener(v -> openCamera());
        whatsappButton.setOnClickListener(v -> openWhatsApp());
        facebookButton.setOnClickListener(v -> openFacebook());
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (messageText.isEmpty() && selectedImageUri == null) {
            Toast.makeText(this, "Please enter a message or select an image", Toast.LENGTH_SHORT).show();
            return;
        }
        addMessageToChat("You", messageText, getCurrentTime(), selectedImageUri != null);
        messageEditText.setText("");
        selectedImageView.setVisibility(View.GONE);
        selectedImageUri = null;
        messagesScrollView.post(() -> messagesScrollView.fullScroll(View.FOCUS_DOWN));
        Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
    }

    private void addMessageToChat(String userName, String message, String time, boolean hasImage) {
        CardView card = new CardView(this);
        card.setRadius(16);
        card.setCardElevation(6);
        card.setUseCompatPadding(true);
        card.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(16, 8, 16, 8);
        card.setLayoutParams(cardParams);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        TextView title = new TextView(this);
        title.setText(userName + " • " + time);
        title.setTextColor(getResources().getColor(android.R.color.darker_gray));
        title.setTextSize(12);

        TextView content = new TextView(this);
        content.setText(message);
        content.setTextSize(16);
        content.setTextColor(getResources().getColor(android.R.color.black));
        content.setPadding(0, 8, 0, 0);

        layout.addView(title);
        layout.addView(content);

        if (hasImage) {
            TextView imageNote = new TextView(this);
            imageNote.setText("📷 Image attached");
            imageNote.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            imageNote.setTextSize(14);
            imageNote.setPadding(0, 8, 0, 0);
            layout.addView(imageNote);
        }

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setPadding(0, 12, 0, 0);

        Button like = new Button(this);
        like.setText("❤️ Like");
        like.setTextSize(12);
        like.setBackground(null);
        like.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

        Button reply = new Button(this);
        reply.setText("💬 Reply");
        reply.setTextSize(12);
        reply.setBackground(null);
        reply.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));

        actions.addView(like);
        actions.addView(reply);
        layout.addView(actions);
        card.addView(layout);
        messagesContainer.addView(card);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select an image"), PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

    private void openWhatsApp() {
        try {
            String url = "https://wa.me/qr/AQSUHR2RYNRKB1";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open WhatsApp", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFacebook() {
        try {
            String url = "https://www.facebook.com/share/1AkHy6uHVd/";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open Facebook", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                selectedImageView.setImageURI(selectedImageUri);
                selectedImageView.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
            } else if (requestCode == CAMERA_REQUEST && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                selectedImageView.setImageBitmap(imageBitmap);
                selectedImageView.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Photo captured", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
