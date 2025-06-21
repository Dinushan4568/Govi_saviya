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

        //navigate to VideoTutorialsActivity
        ImageView ivVideoTutorials = findViewById(R.id.ivVideoTutorials);
        ivVideoTutorials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, VideoTutorialsActivity.class);
                startActivity(intent);
            }
        });

        //navigate to CommunityActivity
        ImageView ivCommunity = findViewById(R.id.ivCommunity);
        ivCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CommunityActivity.class);
                startActivity(intent);
            }
        });

        //navigate to DiseaseAlertActivity
        ImageView ivDiseaseAlert = findViewById(R.id.ivDiseaseAlert);
        ivDiseaseAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DiseaseAlertActivity.class);
                startActivity(intent);
            }
        });

        //navigate to ExpertAdviceActivity
        ImageView ivExpertAdvice = findViewById(R.id.ivExpertAdvice);
        ivExpertAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ExpertAdviceActivity.class);
                startActivity(intent);
            }
        });

        //navigate to FarmCalendarActivity
        ImageView ivFarmCalendar = findViewById(R.id.ivFarmCalendar);
        ivFarmCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FarmCalendarActivity.class);
                startActivity(intent);
            }
        });

        //navigate to FinancialHelpActivity
        ImageView ivFinancialHelp = findViewById(R.id.ivFinancialHelp);
        ivFinancialHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FinancialHelpActivity.class);
                startActivity(intent);
            }
        });

        //navigate to FindWorkersActivity
        ImageView ivFindWorkers = findViewById(R.id.ivFindWorkers);
        ivFindWorkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FindWorkersActivity.class);
                startActivity(intent);
            }
        });

        //navigate to PesticidesActivity
        ImageView ivPesticides = findViewById(R.id.ivPesticides);
        ivPesticides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PesticidesActivity.class);
                startActivity(intent);
            }
        });
    }
    }
