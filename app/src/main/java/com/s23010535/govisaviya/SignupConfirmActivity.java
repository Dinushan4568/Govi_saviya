package com.s23010535.govisaviya;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SignupConfirmActivity extends AppCompatActivity {

    private TextView tvTapToStart;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_confirm);

        initViews();
        setupClickListeners();

        // Auto navigate to home after 3 seconds
        handler = new Handler(android.os.Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToHome();
            }
        }, 3000);
    }

    private void initViews() {
        tvTapToStart = findViewById(R.id.tv_tap_to_start);
    }

    private void setupClickListeners() {
        tvTapToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHome();
            }
        });

        // Make entire screen clickable
        findViewById(R.id.root_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHome();
            }
        });
    }

    private void navigateToHome() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        Intent intent = new Intent(SignupConfirmActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}