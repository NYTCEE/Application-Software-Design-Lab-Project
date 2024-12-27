package com.example.applicationsoftwaredesignlabproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TooEarlyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_too_early);

        Button backButton = findViewById(R.id.backButton);
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
}
