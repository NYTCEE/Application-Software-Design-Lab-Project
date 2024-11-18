package com.example.applicationsoftwaredesignlabproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonSettings;
    private Button buttonAboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        buttonSettings = findViewById(R.id.button2);
        buttonAboutUs = findViewById(R.id.button3);

        // 設置按鈕的點擊事件
        setupButtonListeners();
    }

    // 設定按鈕點擊事件的方法
    private void setupButtonListeners() {

        // "Settings" 按鈕的點擊事件
        buttonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // "About Us" 按鈕的點擊事件
        buttonAboutUs.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);
        });
    }
}
