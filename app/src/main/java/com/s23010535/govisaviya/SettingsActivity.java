package com.23010535.govisaviya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private LinearLayout llScan, llAccount, llPrivacy, llNotifications, llSecurity,
            llAbout, llHelp, llContactPrefs, llDesktop, llLanguage,
            llAccessibility, llStorage;
    private TextView tvUserName, tvLogout;
    private Switch switchNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        llScan = findViewById(R.id.ll_scan);
        llAccount = findViewById(R.id.ll_account);
        llPrivacy = findViewById(R.id.ll_privacy);
        llNotifications = findViewById(R.id.ll_notifications);
        llSecurity = findViewById(R.id.ll_security);
        llAbout = findViewById(R.id.ll_about);
        llHelp = findViewById(R.id.ll_help);
        llContactPrefs = findViewById(R.id.ll_contact_prefs);
        llDesktop = findViewById(R.id.ll_desktop);
        llLanguage = findViewById(R.id.ll_language);
        llAccessibility = findViewById(R.id.ll_accessibility);
        llStorage = findViewById(R.id.ll_storage);

        tvUserName = findViewById(R.id.tv_user_name);
        tvLogout = findViewById(R.id.tv_logout);
        switchNotifications = findViewById(R.id.switch_notifications);
    }

    private void setupClickListeners() {
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        llScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Scan feature", Toast.LENGTH_SHORT).show();
            }
        });

        llAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Account settings", Toast.LENGTH_SHORT).show();
            }
        });

        llPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Privacy settings", Toast.LENGTH_SHORT).show();
            }
        });

        llNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Notification settings", Toast.LENGTH_SHORT).show();
            }
        });

        llSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Security settings", Toast.LENGTH_SHORT).show();
            }
        });

        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "About app", Toast.LENGTH_SHORT).show();
            }
        });

        llHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Help & Support", Toast.LENGTH_SHORT).show();
            }
        });

        llContactPrefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Contact Preferences", Toast.LENGTH_SHORT).show();
            }
        });

        llDesktop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Desktop settings", Toast.LENGTH_SHORT).show();
            }
        });

        llLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Language settings", Toast.LENGTH_SHORT).show();
            }
        });

        llAccessibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Accessibility settings", Toast.LENGTH_SHORT).show();
            }
        });

        llStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Storage settings", Toast.LENGTH_SHORT).show();
            }
        });

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(SettingsActivity.this, "Notifications enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SettingsActivity.this, "Notifications disabled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLogoutDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}