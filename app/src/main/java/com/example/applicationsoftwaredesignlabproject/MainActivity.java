package com.example.applicationsoftwaredesignlabproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonSettings;
    private Button buttonAboutUs;
    private MediaPlayer mediaPlayer;
    private Button backgroundMusicButton; // 音樂控制按鈕
    private boolean isMusicPlaying = false; // 音樂播放狀態標誌

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 MediaPlayer 來播放背景音樂
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music); // background_music 是音樂檔案的名稱
        mediaPlayer.setLooping(true); // 設置音樂循環播放

        // 初始化按鈕
        buttonSettings = findViewById(R.id.button2);
        buttonAboutUs = findViewById(R.id.button3);
        backgroundMusicButton = findViewById(R.id.backgroundMusicButton);

        // 設定音樂控制按鈕的點擊事件
        backgroundMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMusic();
            }
        });

        // 設置其他按鈕的點擊事件
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

    // 切換音樂播放/停止
    private void toggleMusic() {
        if (isMusicPlaying) {
            // 停止音樂
            mediaPlayer.pause();
            backgroundMusicButton.setText("Play Music");
            isMusicPlaying = false;
        } else {
            // 播放音樂
            mediaPlayer.start();
            backgroundMusicButton.setText("Stop Music");
            isMusicPlaying = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 當活動不可見時暫停音樂
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isMusicPlaying = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 恢復音樂播放
        if (!mediaPlayer.isPlaying() && isMusicPlaying) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 釋放 MediaPlayer 資源
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
