package com.s23010535.govisaviya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private ImageView ivSettings;
    private LinearLayout llColorBlue, llColorPurple, llColorYellow, llColorBrown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        ivSettings = findViewById(R.id.iv_settings);
        llColorBlue = findViewById(R.id.ll_color_blue);
        llColorPurple = findViewById(R.id.ll_color_purple);
        llColorYellow = findViewById(R.id.ll_color_yellow);
        llColorBrown = findViewById(R.id.ll_color_brown);
    }

    private void setupClickListeners() {


        llColorBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Blue theme selected", Toast.LENGTH_SHORT).show();
                // Add theme change logic here
            }
        });

        llColorPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Purple theme selected", Toast.LENGTH_SHORT).show();
                // Add theme change logic here
            }
        });

        llColorYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Yellow theme selected", Toast.LENGTH_SHORT).show();
                // Add theme change logic here
            }
        });

        llColorBrown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Brown theme selected", Toast.LENGTH_SHORT).show();
                // Add theme change logic here
            }
        });
    }


}