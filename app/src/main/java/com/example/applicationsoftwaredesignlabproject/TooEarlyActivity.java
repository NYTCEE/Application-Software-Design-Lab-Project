package com.example.applicationsoftwaredesignlabproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TooEarlyActivity extends AppCompatActivity {

    private ImageView backgroundImage;
    private TextView tooEarlyMessage;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_too_early);

        // 初始化視圖元件
        backgroundImage = findViewById(R.id.backgroundImage);
        tooEarlyMessage = findViewById(R.id.tooEarlyMessage);
        backButton = findViewById(R.id.backButton);

        // 設置背景和字體大小
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        setupBackgroundAndFontSize(preferences.getBoolean("darkMode", false));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 當玩家點擊返回時，跳轉回 MainActivity，並停止當前測試
                Intent intent = new Intent(TooEarlyActivity.this, game2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // 確保返回時清除活動堆棧
                startActivity(intent);
            }
        });
    }

    private void setupBackgroundAndFontSize(boolean darkMode) {
        // 設置背景圖片
        backgroundImage.setImageResource(darkMode ?
                R.drawable.darkbackground_image :
                R.drawable.background_image);

        // 設置字體大小
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        int fontSizeIndex = preferences.getInt("fontSizeIndex", 1); // 預設為 Medium
        applyFontSize(fontSizeIndex);
    }

    private void applyFontSize(int fontSizeIndex) {
        float fontSize;
        switch (fontSizeIndex) {
            case 0: // Small
                fontSize = 12f;
                break;
            case 2: // Large
                fontSize = 20f;
                break;
            default: // Medium
                fontSize = 16f;
        }

        tooEarlyMessage.setTextSize(fontSize * 2); // 訊息文字要大一點
        backButton.setTextSize(fontSize);
    }
}