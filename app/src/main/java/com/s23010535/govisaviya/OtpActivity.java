package com.s23010535.govisaviya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OtpActivity extends AppCompatActivity {

    private EditText etOtp;
    private Button btnConfirmOtp, btnBackToLogin;
    private TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etOtp = findViewById(R.id.et_otp);
        btnConfirmOtp = findViewById(R.id.btn_confirm_otp);
        btnBackToLogin = findViewById(R.id.btn_back_to_login);
        tvMessage = findViewById(R.id.tv_message);
    }

    private void setupClickListeners() {
        btnConfirmOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = etOtp.getText().toString().trim();

                if (otp.isEmpty()) {
                    Toast.makeText(OtpActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else if (otp.length() != 6) {
                    Toast.makeText(OtpActivity.this, "OTP must be 6 digits", Toast.LENGTH_SHORT).show();
                } else {
                    // Navigate to confirmation screen
                    Intent intent = new Intent(OtpActivity.this, SignupConfirmActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}