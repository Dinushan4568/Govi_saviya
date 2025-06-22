package com.s23010535.govisaviya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.s23010535.govisaviya.database.DatabaseManager;
import com.s23010535.govisaviya.models.User;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText etEmail, etUsername, etPassword, etFullName;
    private Button btnCreateAccount;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        initViews();
        setupClickListeners();
        databaseManager = DatabaseManager.getInstance(this);
    }

    private void initViews() {
        etEmail = findViewById(R.id.et_email);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etFullName = findViewById(R.id.et_full_name);
        btnCreateAccount = findViewById(R.id.btn_create_account);
    }

    private void setupClickListeners() {
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();

        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            Toast.makeText(CreateAccountActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if user already exists
        if (databaseManager.getUserByUsername(username) != null) {
            Toast.makeText(CreateAccountActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseManager.getUserByEmail(email) != null) {
            Toast.makeText(CreateAccountActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
            return;
        }

        // For simplicity, using password as hash. In a real app, use a strong hashing algorithm.
        User newUser = new User(username, email, password, fullName);
        long userId = databaseManager.addUser(newUser);

        if (userId > 0) {
            Toast.makeText(CreateAccountActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
            // Navigate to OTP or Login
            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(CreateAccountActivity.this, "Error creating account", Toast.LENGTH_SHORT).show();
        }
    }
}