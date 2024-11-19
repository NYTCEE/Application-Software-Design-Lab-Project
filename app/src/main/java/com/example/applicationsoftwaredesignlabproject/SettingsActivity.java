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

        // 初始化視圖
        initializeViews();
        // 設定字體大小選項
        setupFontSizeSpinner();
        // 設定按鈕點擊事件
        setupClickListeners();
        // 加載保存的設定
        loadSavedSettings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 每次返回時重新加載設定
        loadSavedSettings();
    }

    private void initializeViews() {
        notificationSwitch = findViewById(R.id.notificationSwitch);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        fontSizeSpinner = findViewById(R.id.fontSizeSpinner);
        saveButton = findViewById(R.id.saveButton);
        backgroundImage = findViewById(R.id.backgroundImage); // 背景圖片
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

        // 即時更新背景圖片
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                backgroundImage.setImageResource(R.drawable.darkbackground_image);
            } else {
                backgroundImage.setImageResource(R.drawable.background_image);
            }
        });
    }

    private void loadSavedSettings() {
        // 從 SharedPreferences 加載設定
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean notifications = preferences.getBoolean("notifications", true);
        boolean darkMode = preferences.getBoolean("darkMode", false);
        int fontSizeIndex = preferences.getInt("fontSizeIndex", 1); // 預設 Medium

        notificationSwitch.setChecked(notifications);
        darkModeSwitch.setChecked(darkMode);
        fontSizeSpinner.setSelection(fontSizeIndex);

        // 根據 Dark Mode 設定背景圖片
        if (darkMode) {
            backgroundImage.setImageResource(R.drawable.darkbackground_image);
        } else {
            backgroundImage.setImageResource(R.drawable.background_image);
        }
    }

    private void saveSettings() {
        // 儲存設定到 SharedPreferences
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("notifications", notificationSwitch.isChecked());
        editor.putBoolean("darkMode", darkModeSwitch.isChecked());
        editor.putInt("fontSizeIndex", fontSizeSpinner.getSelectedItemPosition());
        editor.apply();

        // 顯示訊息
        Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();

        // 返回 MainActivity
        boolean darkMode = darkModeSwitch.isChecked();
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.putExtra("DarkMode", darkMode);
        startActivity(intent);
        finish();
    }
}
