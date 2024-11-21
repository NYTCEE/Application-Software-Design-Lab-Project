package com.example.applicationsoftwaredesignlabproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonGameStart;
    private Button buttonSettings;
    private Button buttonAboutUs;
    private Button backgroundMusicButton; // 音樂控制按鈕
    private boolean isMusicPlaying = false; // 音樂播放狀態標誌

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 從 SharedPreferences 加載字體大小
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        int fontSizeIndex = preferences.getInt("fontSizeIndex", 1); // 預設為 Medium
        applyFontSize(fontSizeIndex);

        // 初始化按鈕
        buttonGameStart = findViewById(R.id.button1);
        buttonSettings = findViewById(R.id.button2);
        buttonAboutUs = findViewById(R.id.button3);
        backgroundMusicButton = findViewById(R.id.backgroundMusicButton);

        ImageView backgroundImage = findViewById(R.id.backgroundImage);
        boolean darkMode = getIntent().getBooleanExtra("DarkMode", false);

        if (darkMode) {
            backgroundImage.setImageResource(R.drawable.darkbackground_image);
        } else {
            backgroundImage.setImageResource(R.drawable.background_image);
        }

        // 設定音樂控制按鈕的點擊事件
        backgroundMusicButton.setOnClickListener(v -> toggleMusic());

        // 設置其他按鈕的點擊事件
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        buttonGameStart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameStartActivity.class);
            startActivity(intent);
        });

        buttonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        buttonAboutUs.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);
        });
    }

    // 切換音樂播放/停止
    private void toggleMusic() {
        if (isMusicPlaying) {
            stopService(new Intent(this, MusicService.class)); // 停止音樂服務
            backgroundMusicButton.setText("Play Music");
            isMusicPlaying = false;
        } else {
            startService(new Intent(this, MusicService.class)); // 啟動音樂服務
            backgroundMusicButton.setText("Stop Music");
            isMusicPlaying = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MusicService.class)); // 停止音樂服務
    }

    private void applyFontSize(int fontSizeIndex) {
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

        TextView titleText = findViewById(R.id.titleText);
        TextView button1 = findViewById(R.id.button1);
        TextView button2 = findViewById(R.id.button2);
        TextView button3 = findViewById(R.id.button3);
        TextView backgroundMusicButton = findViewById(R.id.backgroundMusicButton);
        TextView copyrightText = findViewById(R.id.copyrightText);

        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize + 20); // 標題略大
        button1.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        button2.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        button3.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        backgroundMusicButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        copyrightText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
    }
}
