package com.example.applicationsoftwaredesignlabproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.TypedValue;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {

    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        backgroundImage = findViewById(R.id.backgroundImage);

        // 初始化背景
        updateBackgroundImage();

        // 設定各個連結的點擊事件
        setupLinkClickListeners();

        // 載入並應用字體大小
        applyFontSize();

        // 設定返回按鈕的點擊事件
        Button backButton = findViewById(R.id.backButton);  // 新增的返回按鈕
        backButton.setOnClickListener(v -> onBackPressed()); // 返回上一頁
    }

    // 在 onResume 方法中更新背景
    @Override
    protected void onResume() {
        super.onResume();
        updateBackgroundImage();
        applyFontSize();
    }

    // 更新背景圖片的方法
    private void updateBackgroundImage() {
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean darkMode = preferences.getBoolean("darkMode", false);

        if (darkMode) {
            backgroundImage.setImageResource(R.drawable.darkbackground_image);
        } else {
            backgroundImage.setImageResource(R.drawable.background_image);
        }
    }

    private void setupLinkClickListeners() {
        // 設定網站連結點擊事件
        TextView websiteLink = findViewById(R.id.websiteLink);
        websiteLink.setOnClickListener(v -> openUrl("https://linktr.ee/nytcee"));

        // 設定 Email 連結點擊事件
        TextView emailLink = findViewById(R.id.emailLink);
        emailLink.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:nytcee@gmail.com"));
            try {
                startActivity(emailIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 設定 YouTube 連結點擊事件
        TextView ytLink = findViewById(R.id.ytLink);
        ytLink.setOnClickListener(v -> openUrl("https://www.youtube.com/channel/UC9tVhQFwEQ0V5AclzHj8q9Q"));

        // 設定 Instagram 連結點擊事件
        TextView instagramLink = findViewById(R.id.instagramLink);
        instagramLink.setOnClickListener(v -> openUrl("https://www.instagram.com/eenytc"));
    }

    // 開啟網頁的輔助函數
    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyFontSize() {
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        int fontSizeIndex = preferences.getInt("fontSizeIndex", 1); // 預設中等大小

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

        // 更新標題文字大小
        TextView titleText = findViewById(R.id.titleText);
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize + 14);

        // 更新所有 TextView 的文字大小
        int[] viewIds = {
                R.id.copyrightText,
                R.id.websiteLink,
                R.id.emailLink,
                R.id.ytLink,
                R.id.instagramLink,
                R.id.backButton
        };

        for (int viewId : viewIds) {
            TextView textView = findViewById(viewId);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        }
    }
}
