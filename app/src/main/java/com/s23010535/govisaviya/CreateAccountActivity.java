package com.s23010535.govisaviya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText etEmail, etUsername, etPassword, etFullName;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        initViews();
        setupClickListeners();
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
                String email = etEmail.getText().toString().trim();
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String fullName = etFullName.getText().toString().trim();

                if (email.isEmpty() || username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                    Toast.makeText(CreateAccountActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Navigate to OTP verification
                    Intent intent = new Intent(CreateAccountActivity.this, OtpActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}