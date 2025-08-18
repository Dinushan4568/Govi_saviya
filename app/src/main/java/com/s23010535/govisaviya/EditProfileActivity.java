package com.s23010535.govisaviya;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.s23010535.govisaviya.data.SessionManager;
import com.s23010535.govisaviya.database.DatabaseManager;
import com.s23010535.govisaviya.models.User;

public class EditProfileActivity extends Activity {
    private EditText etFullName, etPhone, etLocation;
    private Button btnSave, btnCancel;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etLocation = findViewById(R.id.etLocation);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        SessionManager sessionManager = new SessionManager(this);
        int userId = sessionManager.getUserId();
        if (userId > 0) {
            user = DatabaseManager.getInstance(this).getUser(userId);
        }
        if (user == null) {
            Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etFullName.setText(user.getFullName());
        etPhone.setText(user.getPhone());
        etLocation.setText(user.getLocation());

        btnSave.setOnClickListener(v -> {
            user.setFullName(etFullName.getText().toString().trim());
            user.setPhone(etPhone.getText().toString().trim());
            user.setLocation(etLocation.getText().toString().trim());
            boolean ok = DatabaseManager.getInstance(this).updateUser(user);
            if (ok) {
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> finish());
    }
} 