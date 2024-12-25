package com.example.applicationsoftwaredesignlabproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private VideoView videoView;
    private MediaPlayer backgroundMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        setupBackgroundMusic();
        setupVideoPlayer();
    }

    private void setupBackgroundMusic() {
        backgroundMusic = MediaPlayer.create(this, R.raw.intro_music);
        backgroundMusic.setLooping(false);
    }

    private void setupVideoPlayer() {
        videoView = findViewById(R.id.videoView);
        videoView.setKeepScreenOn(true);

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.movie_001;
        videoView.setVideoURI(Uri.parse(videoPath));

        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(false);
            mp.setVolume(1f, 1f);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            videoView.start();
            backgroundMusic.start();
        });

        videoView.setOnCompletionListener(mediaPlayer -> {
            if (backgroundMusic.isPlaying()) {
                backgroundMusic.stop();
            }
            videoView.postDelayed(this::startMainActivity, 1000);
        });

        videoView.setOnErrorListener((mediaPlayer, what, extra) -> {
            if (backgroundMusic.isPlaying()) {
                backgroundMusic.stop();
            }
            startMainActivity();
            return true;
        });
    }

    private void startMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
            backgroundMusic.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null && !videoView.isPlaying()) {
            videoView.start();
            backgroundMusic.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
        }
        if (backgroundMusic != null) {
            backgroundMusic.release();
            backgroundMusic = null;
        }
    }
}