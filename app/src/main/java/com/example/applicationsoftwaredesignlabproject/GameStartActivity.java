package com.example.applicationsoftwaredesignlabproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameStartActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private TextView statusText;
    private Button resetButton;
    private Button[] buttons = new Button[9];
    private String currentPlayer = "X";  // 当前玩家（X或O）
    private boolean gameActive = true;  // 游戏是否继续

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);  // 加载布局

        // 初始化界面元素
        gridLayout = findViewById(R.id.gridLayout);
        statusText = findViewById(R.id.statusText);
        resetButton = findViewById(R.id.resetButton);

        // 初始化按钮
        buttons[0] = findViewById(R.id.button_00);
        buttons[1] = findViewById(R.id.button_01);
        buttons[2] = findViewById(R.id.button_02);
        buttons[3] = findViewById(R.id.button_10);
        buttons[4] = findViewById(R.id.button_11);
        buttons[5] = findViewById(R.id.button_12);
        buttons[6] = findViewById(R.id.button_20);
        buttons[7] = findViewById(R.id.button_21);
        buttons[8] = findViewById(R.id.button_22);

        // 设置按钮点击监听器
        for (int i = 0; i < 9; i++) {
            buttons[i].setText("");  // 清空按钮文本
            buttons[i].setEnabled(true);  // 启用按钮
            final int index = i;  // 使用 final 变量来传递给点击监听器
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClick(index);
                }
            });
        }

        // 重置按钮的点击事件
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    // 处理按钮点击
    private void onButtonClick(int index) {
        if (!gameActive) {
            // 游戏已经结束，不能继续操作
            return;
        }

        Button clickedButton = buttons[index];

        if (clickedButton.getText().toString().equals("")) {
            clickedButton.setText(currentPlayer);  // 设置当前玩家的符号（X 或 O）
            if (checkWin()) {
                statusText.setText("Player " + currentPlayer + " Wins!");
                gameActive = false;  // 游戏结束
            } else if (checkDraw()) {  // 检查是否平局
                statusText.setText("It's a Draw!");
                gameActive = false;  // 游戏结束
            } else {
                currentPlayer = (currentPlayer.equals("X")) ? "O" : "X";  // 切换玩家
                statusText.setText("Player " + currentPlayer + "'s Turn");
            }
        }
    }

    // 检查是否有玩家获胜
    private boolean checkWin() {
        // 通过检查行、列和对角线来判断是否获胜
        String[][] board = new String[3][3];
        for (int i = 0; i < 9; i++) {
            int row = i / 3;
            int col = i % 3;
            board[row][col] = buttons[i].getText().toString();
        }

        // 检查行
        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2]) && !board[i][0].equals("")) {
                return true;
            }
        }

        // 检查列
        for (int i = 0; i < 3; i++) {
            if (board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i]) && !board[0][i].equals("")) {
                return true;
            }
        }

        // 检查对角线
        if (board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2]) && !board[0][0].equals("")) {
            return true;
        }
        if (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0]) && !board[0][2].equals("")) {
            return true;
        }

        return false;
    }

    // 检查是否平局
    private boolean checkDraw() {
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().toString().equals("")) {
                return false;  // 如果还有空格，表示游戏未满
            }
        }
        return true;  // 如果没有空格且没有人获胜，则是平局
    }

    // 重置游戏
    private void resetGame() {
        currentPlayer = "X";  // 重置为玩家X
        gameActive = true;  // 游戏继续
        statusText.setText("Player X's Turn");

        // 清空按钮并启用它们
        for (int i = 0; i < 9; i++) {
            buttons[i].setText("");
            buttons[i].setEnabled(true);
        }
    }
}
