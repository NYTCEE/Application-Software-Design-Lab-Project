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

public class MiddlePageActivity extends AppCompatActivity {

    private Button buttonDraw;
    private Button buttonGameOOXX;
    private Button buttonAPI;
    private ImageView backgroundImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.middle_page);

        // 從 SharedPreferences 加載字體大小
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        int fontSizeIndex = preferences.getInt("fontSizeIndex", 1); // 預設為 Medium
        applyFontSize(fontSizeIndex);

        // 初始化按鈕
        buttonGameOOXX = findViewById(R.id.button1);
        buttonDraw = findViewById(R.id.button2);
        buttonAPI = findViewById(R.id.button3);
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        boolean darkMode = getIntent().getBooleanExtra("DarkMode", false);

        // 初始化背景图片
        backgroundImage = findViewById(R.id.backgroundImage);
        updateBackgroundImage();
        // 設置其他按鈕的點擊事件
        setupButtonListeners();
    }

    private void updateBackgroundImage() {
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean darkMode = preferences.getBoolean("darkMode", false);

        if (darkMode) {
            backgroundImage.setImageResource(R.drawable.darkbackground_image);
        } else {
            backgroundImage.setImageResource(R.drawable.background_image);
        }
    }

    private void setupButtonListeners() {

        buttonGameOOXX.setOnClickListener(v -> {
            Intent intent = new Intent(MiddlePageActivity.this, GameStartActivity.class);
            startActivity(intent);
        });

        buttonDraw.setOnClickListener(v -> {
            Intent intent = new Intent(MiddlePageActivity.this, DrawActivity.class);
            startActivity(intent);
        });

        buttonAPI.setOnClickListener(v -> {
            Intent intent = new Intent(MiddlePageActivity.this, APIActivity.class);
            startActivity(intent);
        });

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

        TextView copyrightText = findViewById(R.id.copyrightText);
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize + 20); // 標題略大
        button1.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        button2.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        button3.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        copyrightText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        Button backButton = findViewById(R.id.backButton);
        backButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
    }
}
