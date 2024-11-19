package com.example.applicationsoftwaredesignlabproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private Switch notificationSwitch;
    private Switch darkModeSwitch;
    private Spinner fontSizeSpinner;
    private Button saveButton;
    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // Initialize views
        initializeViews();
        // Setup font size options
        setupFontSizeSpinner();
        // Setup button click events
        setupClickListeners();
        // Load saved settings
        loadSavedSettings();
    }

    private void initializeViews() {
        notificationSwitch = findViewById(R.id.notificationSwitch);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        fontSizeSpinner = findViewById(R.id.fontSizeSpinner);
        saveButton = findViewById(R.id.saveButton);
        backgroundImage = findViewById(R.id.backgroundImage); // 新增背景圖片的引用
    }

    private void setupFontSizeSpinner() {
        String[] fontSizes = {"Small", "Medium", "Large"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                fontSizes
        );
        fontSizeSpinner.setAdapter(adapter);
    }

    private void setupClickListeners() {
        saveButton.setOnClickListener(v -> saveSettings());

        // 切換 DarkMode 時即時改變背景圖片
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                backgroundImage.setImageResource(R.drawable.darkbackground_image);
            } else {
                backgroundImage.setImageResource(R.drawable.background_image);
            }
        });
    }

    private void loadSavedSettings() {
        // Load settings from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean notifications = preferences.getBoolean("notifications", true);
        boolean darkMode = preferences.getBoolean("darkMode", false);
        int fontSizeIndex = preferences.getInt("fontSizeIndex", 1); // Default to "Medium"

        notificationSwitch.setChecked(notifications);
        darkModeSwitch.setChecked(darkMode);
        fontSizeSpinner.setSelection(fontSizeIndex);

        // 根據 DarkMode 狀態設置背景圖片
        if (darkMode) {
            backgroundImage.setImageResource(R.drawable.darkbackground_image);
        } else {
            backgroundImage.setImageResource(R.drawable.background_image);
        }
    }
    private void saveSettings() {
        // 儲存 DarkMode 狀態
        boolean darkMode = darkModeSwitch.isChecked();

        // 初始化 Intent 並傳遞額外資料
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.putExtra("DarkMode", darkMode);

        // 啟動 MainActivity 並結束當前 Activity
        startActivity(intent);
        finish();

        // 顯示訊息
        Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
    }


}
