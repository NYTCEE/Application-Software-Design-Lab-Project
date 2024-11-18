package com.example.applicationsoftwaredesignlabproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        // 設定各個連結的點擊事件
        setupLinkClickListeners();

        // 設定返回按鈕的點擊事件
        Button backButton = findViewById(R.id.backButton);  // 新增的返回按鈕
        backButton.setOnClickListener(v -> onBackPressed()); // 返回上一頁
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
}
