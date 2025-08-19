package com.s23010535.govisaviya;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    private TextView tvLocation, tvTemperature, tvCondition;
    private final String API_KEY = "10ea36b5fddecd62996885365404d3dc";
    private final String CITY = "Colombo"; // You can make this dynamic later

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Weather TextViews
        tvLocation = findViewById(R.id.tvLocation);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvCondition = findViewById(R.id.tvCondition);

        fetchWeather(); // Fetch and display weather data

        // Navigation buttons (your existing code)
        findViewById(R.id.ivMarketplace).setOnClickListener(v -> startActivity(new Intent(this, MarketplaceActivity.class)));
        findViewById(R.id.ivVideoTutorials).setOnClickListener(v -> startActivity(new Intent(this, VideoTutorialsActivity.class)));
        findViewById(R.id.ivCommunity).setOnClickListener(v -> startActivity(new Intent(this, CommunityActivity.class)));
        findViewById(R.id.ivDiseaseAlert).setOnClickListener(v -> {
            try {
                Log.d("HomeActivity", "Attempting to navigate to DiseaseAlertActivity");
                Intent intent = new Intent(this, DiseaseAlertActivity.class);
                startActivity(intent);
                Log.d("HomeActivity", "Successfully started DiseaseAlertActivity");
            } catch (Exception e) {
                Log.e("HomeActivity", "Error navigating to DiseaseAlertActivity: " + e.getMessage(), e);
                Toast.makeText(this, "Error opening Disease Alert", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.ivExpertAdvice).setOnClickListener(v -> startActivity(new Intent(this, ExpertAdviceActivity.class)));
        findViewById(R.id.ivFarmCalendar).setOnClickListener(v -> startActivity(new Intent(this, FarmCalendarActivity.class)));
        findViewById(R.id.ivFinancialHelp).setOnClickListener(v -> startActivity(new Intent(this, FinancialHelpActivity.class)));
        findViewById(R.id.ivFindWorkers).setOnClickListener(v -> startActivity(new Intent(this, FindWorkersActivity.class)));
        findViewById(R.id.ivPesticides).setOnClickListener(v -> {
            try {
                Log.d("HomeActivity", "Attempting to navigate to PesticidesActivity");
                Intent intent = new Intent(this, PesticidesActivity.class);
                startActivity(intent);
                Log.d("HomeActivity", "Successfully started PesticidesActivity");
            } catch (Exception e) {
                Log.e("HomeActivity", "Error navigating to PesticidesActivity: " + e.getMessage(), e);
                Toast.makeText(this, "Error opening Pesticides page", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.ivFeedback).setOnClickListener(v -> startActivity(new Intent(this, FeedbackActivity.class)));
        findViewById(R.id.ivshopping).setOnClickListener(v -> startActivity(new Intent(this, MarketplaceActivity.class)));
        findViewById(R.id.ivChat).setOnClickListener(v -> startActivity(new Intent(this, CommunityActivity.class)));
        findViewById(R.id.ivLearn).setOnClickListener(v -> startActivity(new Intent(this, VideoTutorialsActivity.class)));
        findViewById(R.id.ivProfile).setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }

    private void fetchWeather() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);
        Call<WeatherResponse> call = weatherApi.getWeather(CITY, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();
                    tvLocation.setText(weather.name);
                    tvTemperature.setText(weather.main.temp + "°C");
                    tvCondition.setText(weather.weather.get(0).description);
                } else {
                    tvCondition.setText("Weather data unavailable");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                tvCondition.setText("Error loading weather");
            }
        });
    }
}
