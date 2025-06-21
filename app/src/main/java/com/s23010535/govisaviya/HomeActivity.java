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

        ImageView ivMarketplace = findViewById(R.id.ivMarketplace);
        ivMarketplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MarketplaceActivity.class);
                startActivity(intent);
            }
        });
    }
    }
