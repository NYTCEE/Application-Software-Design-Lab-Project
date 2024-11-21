package com.example.applicationsoftwaredesignlabproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonGameStart;
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
        buttonGameStart = findViewById(R.id.button1);
        buttonSettings = findViewById(R.id.button2);
        buttonAboutUs = findViewById(R.id.button3);
        backgroundMusicButton = findViewById(R.id.backgroundMusicButton);
        // 啟動背景音樂服務
        startService(new Intent(this, MusicService.class));

        ImageView backgroundImage = findViewById(R.id.backgroundImage);

        boolean darkMode = getIntent().getBooleanExtra("DarkMode", false);

        if (darkMode) {
            backgroundImage.setImageResource(R.drawable.darkbackground_image);
        } else {
            backgroundImage.setImageResource(R.drawable.background_image);
        }
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


        buttonGameStart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameStartActivity.class);
            startActivity(intent);
        });

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
        // 如果需要讓音樂停止（退出應用時），可以取消以下行來保持服務運行
        stopService(new Intent(this, MusicService.class));
    }

    // 確保在 onResume() 中處理音樂的狀態一致性
    @Override
    protected void onResume() {
        super.onResume();
        // 根據背景模式更新背景
        boolean darkMode = getIntent().getBooleanExtra("DarkMode", false);
        ImageView backgroundImage = findViewById(R.id.backgroundImage);

        if (darkMode) {
            backgroundImage.setImageResource(R.drawable.darkbackground_image);
        } else {
            backgroundImage.setImageResource(R.drawable.background_image);
        }

        // 恢復音樂播放狀態
        if (mediaPlayer != null && isMusicPlaying && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            backgroundMusicButton.setText("Stop Music");
        }
    }

    // 在 onPause() 停止音樂播放
    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            backgroundMusicButton.setText("Play Music");
        }
    }

}
