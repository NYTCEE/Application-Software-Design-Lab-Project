package com.example.applicationsoftwaredesignlabproject;

import android.content.Intent;  // 添加這行
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class game2 extends AppCompatActivity {

    private TextView statusText;
    private RelativeLayout mainLayout;
    private EditText playerIdInput;
    private Button showLeaderboardButton;
    private Handler handler = new Handler();
    private Random random = new Random();
    private long startTime;
    private boolean isWaitingForReaction = false;
    private boolean hasColorChanged = false;
    private String currentPlayerId;

    // 使用 SharedPreferences 存儲玩家成績
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "ReactionTestPrefs";
    private static final String PLAYER_SCORES_KEY = "PlayerScores";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);

        statusText = findViewById(R.id.statusText);
        mainLayout = findViewById(R.id.game2Layout);
        playerIdInput = findViewById(R.id.playerIdInput);
        showLeaderboardButton = findViewById(R.id.showLeaderboardButton);

        // 初始化 SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isWaitingForReaction) {
                    startReactionTest();
                } else if (!hasColorChanged) {
                    showTooEarlyScreen();  // 太早點擊，跳轉到提示畫面
                } else {
                    measureReactionTime();
                }
            }
        });

        showLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaderboard();  // 跳轉到排行榜畫面
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetTest();  // 每次返回測試畫面時，重置遊戲
    }

    private void startReactionTest() {
        currentPlayerId = playerIdInput.getText().toString().trim();
        if (currentPlayerId.isEmpty()) {
            Toast.makeText(this, "請輸入玩家ID", Toast.LENGTH_SHORT).show();
            return;
        }

        statusText.setText("等待顏色變化...");
        isWaitingForReaction = true;
        hasColorChanged = false;
        mainLayout.setBackgroundColor(getResources().getColor(android.R.color.white));

        // 隨機延遲 2 到 5 秒後改變顏色
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light)); // 變色
                statusText.setText("現在點擊！");
                startTime = System.currentTimeMillis(); // 記錄開始時間
                hasColorChanged = true; // 標記顏色已經變化
            }
        }, random.nextInt(3000) + 2000); // 延遲 2 到 5 秒
    }

    private void measureReactionTime() {
        if (isWaitingForReaction && hasColorChanged) {
            long reactionTime = System.currentTimeMillis() - startTime; // 計算反應時間
            statusText.setText("你的反應時間是：" + reactionTime + " 毫秒");
            Toast.makeText(game2.this, "反應時間：" + reactionTime + " 毫秒", Toast.LENGTH_SHORT).show();
            isWaitingForReaction = false;
            mainLayout.setBackgroundColor(getResources().getColor(android.R.color.white)); // 重置背景顏色
            statusText.setText("點擊任意地方再次測試");

            // 保存成績到 SharedPreferences
            savePlayerScore(currentPlayerId, reactionTime);
        }
    }

    private void savePlayerScore(String playerId, long score) {
        // 取得 SharedPreferences 中的成績，並將新的成績加入
        Set<String> playerScoresSet = sharedPreferences.getStringSet(PLAYER_SCORES_KEY, new HashSet<String>());
        String newScore = playerId + ": " + score + " 毫秒";
        playerScoresSet.add(newScore);

        // 保存更新後的成績集
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(PLAYER_SCORES_KEY, playerScoresSet);
        editor.apply();
    }

    private void showLeaderboard() {
        Intent intent = new Intent(game2.this, LeaderboardActivity.class);
        startActivity(intent);  // 跳轉到排行榜畫面
    }

    private void showTooEarlyScreen() {
        Intent intent = new Intent(game2.this, TooEarlyActivity.class);
        startActivity(intent);  // 跳轉到 "太早啦" 畫面
    }

    // 重置測試狀態
    private void resetTest() {
        isWaitingForReaction = false;
        hasColorChanged = false;
        mainLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
        statusText.setText("點擊任意地方開始測試");
    }
}
