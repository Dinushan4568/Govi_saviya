package com.s23010535.govisaviya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.s23010535.govisaviya.data.SessionManager;
import com.s23010535.govisaviya.database.DatabaseManager;
import com.s23010535.govisaviya.models.User;

public class ProfileActivity extends Activity {
    private static final int REQ_EDIT_PROFILE = 101;
    private ImageView ivAvatar;
    private TextView tvFullName, tvUsername, tvEmail, tvPhone, tvLocation, tvMemberSince;
    private Button btnEditProfile, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivAvatar = findViewById(R.id.ivAvatar);
        tvFullName = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvLocation = findViewById(R.id.tvLocation);
        tvMemberSince = findViewById(R.id.tvMemberSince);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);

        SessionManager sessionManager = new SessionManager(this);
        int userId = sessionManager.getUserId();
        User user = null;
        if (userId > 0) {
            user = DatabaseManager.getInstance(this).getUser(userId);
        }

        if (user != null) {
            tvFullName.setText(user.getFullName() != null ? user.getFullName() : user.getUsername());
            tvUsername.setText("@" + (user.getUsername() != null ? user.getUsername() : "user"));
            tvEmail.setText("Email: " + (user.getEmail() != null ? user.getEmail() : "-"));
            tvPhone.setText("Phone: " + (user.getPhone() != null ? user.getPhone() : "-"));
            tvLocation.setText("Location: " + (user.getLocation() != null ? user.getLocation() : "-"));
            tvMemberSince.setText("Member since: " + (user.getCreatedAt() != null ? user.getCreatedAt().toString() : "-"));
        } else {
            tvFullName.setText("Guest");
            tvUsername.setText("@guest");
            tvEmail.setText("Email: -");
            tvPhone.setText("Phone: -");
            tvLocation.setText("Location: -");
            tvMemberSince.setText("Member since: -");
        }

        btnEditProfile.setOnClickListener(v -> {
            Intent i = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivityForResult(i, REQ_EDIT_PROFILE);
        });

        btnLogout.setOnClickListener(v -> {
            sessionManager.logoutUser();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_EDIT_PROFILE && resultCode == Activity.RESULT_OK) {
            // Reload user info
            SessionManager sessionManager = new SessionManager(this);
            int userId = sessionManager.getUserId();
            User user = userId > 0 ? DatabaseManager.getInstance(this).getUser(userId) : null;
            if (user != null) {
                tvFullName.setText(user.getFullName() != null ? user.getFullName() : user.getUsername());
                tvUsername.setText("@" + (user.getUsername() != null ? user.getUsername() : "user"));
                tvEmail.setText("Email: " + (user.getEmail() != null ? user.getEmail() : "-"));
                tvPhone.setText("Phone: " + (user.getPhone() != null ? user.getPhone() : "-"));
                tvLocation.setText("Location: " + (user.getLocation() != null ? user.getLocation() : "-"));
                tvMemberSince.setText("Member since: " + (user.getCreatedAt() != null ? user.getCreatedAt().toString() : "-"));
            }
        }
    }
}

