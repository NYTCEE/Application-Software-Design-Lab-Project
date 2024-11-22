package com.example.applicationsoftwaredesignlabproject;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DrawActivity extends AppCompatActivity {

    private ImageView backgroundImage; // 移到類別屬性中
    private MyCanvasView canvasView;   // 繪畫區域的自定義 View

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw);

        // 初始化背景圖片
        backgroundImage = findViewById(R.id.backgroundImage);
        updateBackgroundImage(); // 更新背景圖片

        // 初始化自定義繪畫區域
        canvasView = findViewById(R.id.myCanvasView);

        // 綁定按鈕並設置點擊事件
        Button btClear = findViewById(R.id.button_Clear);
        Button btGreen = findViewById(R.id.button_Green);
        Button btBlue = findViewById(R.id.button_Blue);
        Button btRed = findViewById(R.id.button_Red);

        btClear.setOnClickListener(v -> canvasView.clear());             // 清除畫布
        btGreen.setOnClickListener(v -> canvasView.setColor(Color.GREEN)); // 設置畫筆顏色為綠色
        btBlue.setOnClickListener(v -> canvasView.setColor(Color.BLUE));   // 設置畫筆顏色為藍色
        btRed.setOnClickListener(v -> canvasView.setColor(Color.RED));     // 設置畫筆顏色為紅色
    }

    /**
     * 更新背景圖片，根據暗黑模式的狀態切換背景。
     */
    private void updateBackgroundImage() {
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean darkMode = preferences.getBoolean("darkMode", false);

        if (darkMode) {
            backgroundImage.setImageResource(R.drawable.darkbackground_image); // 暗黑模式背景
        } else {
            backgroundImage.setImageResource(R.drawable.background_image);     // 普通模式背景
        }
    }
}
