package com.example.applicationsoftwaredesignlabproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        // 設定各個連結的點擊事件
        setupLinkClickListeners();
    }

    private void setupLinkClickListeners() {
        // 設定網站連結點擊事件
        TextView websiteLink = findViewById(R.id.websiteLink);
        websiteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://linktr.ee/nytcee");
            }
        });

        // 設定 Email 連結點擊事件
        TextView emailLink = findViewById(R.id.emailLink);
        emailLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:nytcee@gmail.com"));
                try {
                    startActivity(emailIntent);
                } catch (Exception e) {
                    // 處理沒有郵件應用程式的情況
                    e.printStackTrace();
                }
            }
        });

        // 設定 YouTube 連結點擊事件
        TextView ytLink = findViewById(R.id.ytLink);
        ytLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://www.youtube.com/channel/UC9tVhQFwEQ0V5AclzHj8q9Q");
            }
        });

        // 設定 Instagram 連結點擊事件
        TextView instagramLink = findViewById(R.id.instagramLink);
        instagramLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://www.instagram.com/nytcee");
            }
        });
    }

    // 開啟網頁的輔助函數
    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        try {
            startActivity(intent);
        } catch (Exception e) {
            // 處理沒有瀏覽器應用程式的情況
            e.printStackTrace();
        }
    }
}
