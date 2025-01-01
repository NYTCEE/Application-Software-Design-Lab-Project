package com.example.applicationsoftwaredesignlabproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class game2 extends AppCompatActivity {

    private View colorOverlay;
    private TextView statusText;
    private ConstraintLayout mainLayout;
    private TextInputEditText playerIdInput;
    private TextInputLayout playerIdInputLayout;
    private MaterialButton showLeaderboardButton;
    private ImageView backgroundImage;
    private Handler handler = new Handler();
    private Random random = new Random();
    private long startTime;
    private boolean isWaitingForReaction = false;
    private boolean hasColorChanged = false;
    private String currentPlayerId;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "ReactionTestPrefs";
    private static final String PLAYER_SCORES_KEY = "PlayerScores";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        initializeViews();
        setupBackgroundAndFontSize(preferences.getBoolean("darkMode", false));
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        setupClickListeners();
    }

    private void initializeViews() {
        statusText = findViewById(R.id.statusText);
        mainLayout = findViewById(R.id.game2Layout);
        playerIdInput = findViewById(R.id.playerIdInput);
        playerIdInputLayout = findViewById(R.id.playerIdInputLayout);
        showLeaderboardButton = findViewById(R.id.showLeaderboardButton);
        backgroundImage = findViewById(R.id.backgroundImage);
        colorOverlay = findViewById(R.id.colorOverlay);
    }

    private void setupBackgroundAndFontSize(boolean darkMode) {
        // 設置背景圖片
        backgroundImage.setImageResource(darkMode ?
                R.drawable.darkbackground_image :
                R.drawable.background_image);

        // 設置字體大小
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        int fontSizeIndex = preferences.getInt("fontSizeIndex", 1); // 預設為 Medium
        applyFontSize(fontSizeIndex);
    }

    private void applyFontSize(int fontSizeIndex) {
        float fontSize;
        switch (fontSizeIndex) {
            case 0: // Small
                fontSize = 12f;
                break;
            case 2: // Large
                fontSize = 20f;
                break;
            default: // Medium
                fontSize = 16f;
        }

        statusText.setTextSize(fontSize * 1.5f); // 狀態文字稍大
        playerIdInput.setTextSize(fontSize);
        showLeaderboardButton.setTextSize(fontSize);
    }

    private void setupClickListeners() {
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isWaitingForReaction) {
                    startReactionTest();
                } else if (!hasColorChanged) {
                    showTooEarlyScreen();
                } else {
                    measureReactionTime();
                }
            }
        });

        showLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaderboard();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetTest();
    }

    private void startReactionTest() {
        currentPlayerId = playerIdInput.getText().toString().trim();
        if (currentPlayerId.isEmpty()) {
            playerIdInputLayout.setError("請輸入玩家ID");
            return;
        }

        playerIdInputLayout.setError(null);
        statusText.setText("等待顏色變化...");
        isWaitingForReaction = true;
        hasColorChanged = false;
        colorOverlay.setVisibility(View.INVISIBLE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int yellowWithAlpha = Color.argb(180, 255, 165, 0);
                colorOverlay.setBackgroundColor(yellowWithAlpha);
                colorOverlay.setVisibility(View.VISIBLE);
                statusText.setText("現在點擊！");
                startTime = System.currentTimeMillis();
                hasColorChanged = true;
            }
        }, random.nextInt(3000) + 2000);
    }

    private void measureReactionTime() {
        if (isWaitingForReaction && hasColorChanged) {
            long reactionTime = System.currentTimeMillis() - startTime;
            statusText.setText("你的反應時間是：" + reactionTime + " 毫秒");
            Toast.makeText(game2.this, "反應時間：" + reactionTime + " 毫秒", Toast.LENGTH_SHORT).show();
            isWaitingForReaction = false;
            colorOverlay.setVisibility(View.INVISIBLE);
            statusText.setText("點擊任意地方再次測試");

            savePlayerScore(currentPlayerId, reactionTime);
        }
    }

    private void savePlayerScore(String playerId, long score) {
        Set<String> playerScoresSet = sharedPreferences.getStringSet(PLAYER_SCORES_KEY, new HashSet<String>());
        Set<String> newSet = new HashSet<>(playerScoresSet); // 創建新的 Set 以確保修改會被保存
        String newScore = playerId + ": " + score + " 毫秒";
        newSet.add(newScore);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(PLAYER_SCORES_KEY, newSet);
        editor.apply();
    }

    private void showLeaderboard() {
        Intent intent = new Intent(game2.this, LeaderboardActivity.class);
        startActivity(intent);
    }

    private void showTooEarlyScreen() {
        Intent intent = new Intent(game2.this, TooEarlyActivity.class);
        startActivity(intent);
    }

    private void resetTest() {
        isWaitingForReaction = false;
        hasColorChanged = false;
        colorOverlay.setVisibility(View.INVISIBLE);
        statusText.setText("點擊任意地方開始測試");
        if (playerIdInputLayout != null) {
            playerIdInputLayout.setError(null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}