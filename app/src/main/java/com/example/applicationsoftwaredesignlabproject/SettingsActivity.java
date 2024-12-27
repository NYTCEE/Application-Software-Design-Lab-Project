package com.example.applicationsoftwaredesignlabproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

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
        // 應用字體大小到設定頁面的元件
        applyFontSizeToSettingsPage(fontSizeIndex);

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
        // 儲存通知開關狀態
        boolean notificationsEnabled = notificationSwitch.isChecked();
        editor.putBoolean("notifications", notificationSwitch.isChecked());
        editor.putBoolean("darkMode", darkModeSwitch.isChecked());
        // 獲取選擇的字體大小索引
        int fontSizeIndex = fontSizeSpinner.getSelectedItemPosition();
        editor.putInt("fontSizeIndex", fontSizeIndex);
        editor.apply();

        // 只有在通知開啟時才顯示提醒
        if (notificationsEnabled) {
            Toast.makeText(this, "Settings Saved!", Toast.LENGTH_SHORT).show();
        }

        // 返回 MainActivity
        boolean darkMode = darkModeSwitch.isChecked();
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.putExtra("DarkMode", darkMode);
        intent.putExtra("FontSize", fontSizeIndex);
        startActivity(intent);
        finish();
    }

    private void applyFontSizeToSettingsPage(int fontSizeIndex) {
        float fontSize;
        switch (fontSizeIndex) {
            case 0: // Small
                fontSize = 12f;
                break;
            case 1: // Medium
                fontSize = 16f;
                break;
            case 2: // Large
                fontSize = 20f;
                break;
            default:
                fontSize = 16f;
        }

        // 標題文字大小
        TextView titleText = findViewById(R.id.titleText);
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize + 14);

        // 各種 TextView、Switch 和 Button 的文字大小
        int[] viewIds = {
                R.id.notificationSwitch,
                R.id.darkModeSwitch,
                R.id.saveButton,
                R.id.fontSizeSpinner  // 加入 Spinner 的 ID
        };

        for (int viewId : viewIds) {
            View view = findViewById(viewId);
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            } else if (view instanceof Switch) {
                // 特別處理 Switch 的文字大小
                ((Switch) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            } else if (view instanceof Button) {
                ((Button) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            }
        }
    }
}
