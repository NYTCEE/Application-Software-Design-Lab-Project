package com.example.applicationsoftwaredesignlabproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LeaderboardActivity extends AppCompatActivity {
    private TextView leaderboardTextView;
    private Button backButton;
    private Button clearButton;
    private ImageView backgroundImage;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "ReactionTestPrefs";
    private static final String PLAYER_SCORES_KEY = "PlayerScores";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // 初始化視圖元件
        leaderboardTextView = findViewById(R.id.leaderboardTextView);
        backButton = findViewById(R.id.backButton);
        clearButton = findViewById(R.id.clearButton);
        backgroundImage = findViewById(R.id.backgroundImage);

        // 設置背景和字體大小
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean darkMode = preferences.getBoolean("darkMode", false);
        int fontSizeIndex = preferences.getInt("fontSizeIndex", 1); // 預設 Medium

        // 設置背景
        backgroundImage.setImageResource(darkMode ?
                R.drawable.darkbackground_image :
                R.drawable.background_image);

        // 設置字體大小
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
        leaderboardTextView.setTextSize(fontSize);
        backButton.setTextSize(fontSize);
        clearButton.setTextSize(fontSize);

        // 讀取 SharedPreferences 中保存的成績
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> playerScoresSet = sharedPreferences.getStringSet(PLAYER_SCORES_KEY, new HashSet<String>());

        // 顯示並排序排行榜
        displayLeaderboard(playerScoresSet);

        // 設置返回按鈕的點擊事件
        backButton.setOnClickListener(v -> {
            // 當按下返回鍵時，跳回主頁面
            onBackPressed();
        });

        // 設置清除成績按鈕的點擊事件
        clearButton.setOnClickListener(v -> {
            clearLeaderboard();
            displayLeaderboard(new HashSet<>()); // 清除成績後重新顯示空的排行榜
        });
    }

    // 顯示並排序排行榜
    private void displayLeaderboard(Set<String> playerScoresSet) {
        List<Score> scores = new ArrayList<>();

        // 解析每條成績並添加到 List
        if (playerScoresSet != null && !playerScoresSet.isEmpty()) {
            for (String score : playerScoresSet) {
                String[] parts = score.split(": ");
                String playerId = parts[0];
                String time = parts[1].replace(" 毫秒", "");

                // 創建 Score 物件並加入 List
                scores.add(new Score(playerId, Long.parseLong(time)));
            }
        }

        // 排序，根據反應時間（升序排序）
        Collections.sort(scores, (score1, score2) -> Long.compare(score1.getTime(), score2.getTime()));

        // 顯示排好序的排行榜，並加上名次
        StringBuilder leaderboard = new StringBuilder();
        int rank = 1; // 初始名次
        for (Score score : scores) {
            // 格式化顯示名次、玩家名稱、反應時間
            String formattedLine = String.format("%-6s%-20s%10s\n", getRankString(rank), score.getPlayerId(), score.getTime() + " 毫秒");
            leaderboard.append(formattedLine);
            rank++; // 增加名次
        }

        if (leaderboard.length() == 0) {
            leaderboard.append("暫無成績");
        }

        leaderboardTextView.setText(leaderboard.toString());
    }

    // 用來格式化名次
    private String getRankString(int rank) {
        switch (rank) {
            case 1: return "第1名";
            case 2: return "第2名";
            case 3: return "第3名";
            default: return "第" + rank + "名";
        }
    }

    // 清除所有排行榜成績
    private void clearLeaderboard() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // 清除所有 SharedPreferences 中的數據
        editor.apply();   // 提交更改
    }

    // Score 類，用於儲存每條成績
    private static class Score {
        private String playerId;
        private long time;

        public Score(String playerId, long time) {
            this.playerId = playerId;
            this.time = time;
        }

        public String getPlayerId() {
            return playerId;
        }

        public long getTime() {
            return time;
        }
    }
}