package com.example.applicationsoftwaredesignlabproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameStartActivity extends AppCompatActivity {

    private GridLayout gridLayout;      // 游戏棋盘
    private TextView statusText;        // 游戏状态显示
    private Button resetButton;         // 重置按钮
    private Button[] buttons = new Button[9]; // 每个格子的按钮
    private String currentPlayer = "X"; // 当前玩家（X 或 O）
    private boolean gameActive = true;  // 游戏是否进行中

    private ImageView backgroundImage;  // 背景图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game); // 绑定游戏布局

        // 获取从其他页面传递的暗黑模式参数
        boolean darkMode = getIntent().getBooleanExtra("DarkMode", false);

        // 初始化背景图片
        backgroundImage = findViewById(R.id.backgroundImage);
        updateBackgroundImage();
        // 設定返回按鈕的點擊事件
        Button backButton = findViewById(R.id.backButton);  // 新增的返回按鈕
        backButton.setOnClickListener(v -> onBackPressed()); // 返回上一頁
        // 初始化游戏控件
        initializeGameViews();
        setupGameButtons();
        applyFontSize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBackgroundImage();
        // 新增：重新應用字體大小
        applyFontSize();
    }

    private void updateBackgroundImage() {
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean darkMode = preferences.getBoolean("darkMode", false);

        if (darkMode) {
            backgroundImage.setImageResource(R.drawable.darkbackground_image);
        } else {
            backgroundImage.setImageResource(R.drawable.background_image);
        }
    }

    // 初始化游戏控件
    private void initializeGameViews() {
        gridLayout = findViewById(R.id.gridLayout);
        statusText = findViewById(R.id.statusText);
        resetButton = findViewById(R.id.resetButton);

        // 重置按钮点击事件
        resetButton.setOnClickListener(v -> resetGame());
    }

    // 设置游戏格子的按钮事件
    private void setupGameButtons() {
        // 获取每个格子按钮的 ID
        buttons[0] = findViewById(R.id.button_00);
        buttons[1] = findViewById(R.id.button_01);
        buttons[2] = findViewById(R.id.button_02);
        buttons[3] = findViewById(R.id.button_10);
        buttons[4] = findViewById(R.id.button_11);
        buttons[5] = findViewById(R.id.button_12);
        buttons[6] = findViewById(R.id.button_20);
        buttons[7] = findViewById(R.id.button_21);
        buttons[8] = findViewById(R.id.button_22);

        // 为每个按钮设置点击事件
        for (int i = 0; i < 9; i++) {
            buttons[i].setText("");      // 清空按钮内容
            buttons[i].setEnabled(true); // 启用按钮
            final int index = i;         // 确保索引正确传递
            buttons[i].setOnClickListener(v -> onButtonClick(index));
        }
    }

    // 处理格子点击事件
    private void onButtonClick(int index) {
        if (!gameActive) return; // 如果游戏已结束，忽略点击

        Button clickedButton = buttons[index];
        if (clickedButton.getText().toString().isEmpty()) {
            clickedButton.setText(currentPlayer); // 设置当前玩家的标记

            if (checkWin()) { // 判断是否胜利
                statusText.setText("Player " + currentPlayer + " Wins!");
                gameActive = false; // 停止游戏
            } else if (checkDraw()) { // 判断是否平局
                statusText.setText("It's a Draw!");
                gameActive = false; // 停止游戏
            } else {
                currentPlayer = currentPlayer.equals("X") ? "O" : "X"; // 切换玩家
                statusText.setText("Player " + currentPlayer + "'s Turn");
            }
        }
    }

    // 检查是否有玩家获胜
    private boolean checkWin() {
        String[][] board = new String[3][3];
        for (int i = 0; i < 9; i++) {
            board[i / 3][i % 3] = buttons[i].getText().toString();
        }

        // 检查行
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].isEmpty() && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                return true;
            }
        }

        // 检查列
        for (int i = 0; i < 3; i++) {
            if (!board[0][i].isEmpty() && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
                return true;
            }
        }

        // 检查对角线
        if (!board[0][0].isEmpty() && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            return true;
        }
        if (!board[0][2].isEmpty() && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            return true;
        }

        return false;
    }

    // 检查是否平局
    private boolean checkDraw() {
        for (Button button : buttons) {
            if (button.getText().toString().isEmpty()) {
                return false; // 还有空格子，继续游戏
            }
        }
        return true; // 没有空格子且无人获胜，平局
    }

    // 重置游戏
    private void resetGame() {
        currentPlayer = "X";  // 重置为玩家 X
        gameActive = true;    // 重启游戏
        statusText.setText("Player X's Turn");

        // 清空按钮并启用
        for (Button button : buttons) {
            button.setText("");
            button.setEnabled(true);
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

        // 更新狀態文字大小
        statusText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        // 更新重置按鈕文字大小
        resetButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        // 更新每個遊戲格子按鈕的文字大小
        for (Button button : buttons) {
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize + 4); // 遊戲格子文字可以稍大一點
        }

        // 更新返回按鈕文字大小
        Button backButton = findViewById(R.id.backButton);
        backButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
    }
}
