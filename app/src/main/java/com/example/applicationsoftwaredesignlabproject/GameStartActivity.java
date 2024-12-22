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

    private GridLayout gridLayout;
    private TextView statusText;
    private Button resetButton;
    private Button[] buttons = new Button[9];
    private String currentPlayer = "X";
    private boolean gameActive = true;

    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        boolean darkMode = getIntent().getBooleanExtra("DarkMode", false);
        backgroundImage = findViewById(R.id.backgroundImage);
        updateBackgroundImage();

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        initializeGameViews();
        setupGameButtons();
        applyFontSize();
    }

    @Override
    protected void onResume() {//遊戲預設
        super.onResume();
        updateBackgroundImage();
        applyFontSize();
    }

    private void updateBackgroundImage() {//深色模式
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean darkMode = preferences.getBoolean("darkMode", false);

        if (darkMode) {
            backgroundImage.setImageResource(R.drawable.darkbackground_image);
        } else {
            backgroundImage.setImageResource(R.drawable.background_image);
        }
    }


    private void initializeGameViews() {
        gridLayout = findViewById(R.id.gridLayout);
        statusText = findViewById(R.id.statusText);
        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> resetGame());
    }

    private void setupGameButtons() {

        buttons[0] = findViewById(R.id.button_00);
        buttons[1] = findViewById(R.id.button_01);
        buttons[2] = findViewById(R.id.button_02);
        buttons[3] = findViewById(R.id.button_10);
        buttons[4] = findViewById(R.id.button_11);
        buttons[5] = findViewById(R.id.button_12);
        buttons[6] = findViewById(R.id.button_20);
        buttons[7] = findViewById(R.id.button_21);
        buttons[8] = findViewById(R.id.button_22);

        for (int i = 0; i < 9; i++) {
            buttons[i].setText("");
            buttons[i].setEnabled(true);
            final int index = i;
            buttons[i].setOnClickListener(v -> onButtonClick(index));
        }
    }


    private void onButtonClick(int index) {//按鈕動作
        if (!gameActive) return;

        Button clickedButton = buttons[index];
        if (clickedButton.getText().toString().isEmpty()) {
            clickedButton.setText(currentPlayer);

            if (checkWin()) {
                statusText.setText("Player " + currentPlayer + " Wins!");
                if (currentPlayer.equals("X")) {
                    backgroundImage.setImageResource(R.drawable.x_win_image);
                } else {
                    backgroundImage.setImageResource(R.drawable.o_win_image);
                }
                gameActive = false;
            } else if (checkDraw()) {
                statusText.setText("It's a Draw!");
                backgroundImage.setImageResource(R.drawable.draw_image);
                gameActive = false;
            } else {
                currentPlayer = currentPlayer.equals("X") ? "O" : "X";
                statusText.setText("Player " + currentPlayer + "'s Turn");
            }
        }
    }

    private boolean checkWin() {//勝利確認
        String[][] board = new String[3][3];
        for (int i = 0; i < 9; i++) {
            //列     //行
            board[i / 3][i % 3] = buttons[i].getText().toString();//讀取格子
        }
        for (int i = 0; i < 3; i++) {//檢查列
            if (!board[i][0].isEmpty() && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {//檢查行
            if (!board[0][i].isEmpty() && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
                return true;
            }
        }
        //檢查斜線
        if (!board[0][0].isEmpty() && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            return true;
        }
        if (!board[0][2].isEmpty() && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            return true;
        }
        return false;
    }

    private boolean checkDraw() {
        for (Button button : buttons) {
            if (button.getText().toString().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void resetGame() {//遊戲重置
        currentPlayer = "X";
        gameActive = true;
        statusText.setText("Player X's Turn");
        updateBackgroundImage();
        for (int i = 0; i < 9; i++) {
            buttons[i].setText("");
            buttons[i].setEnabled(true);
        }
    }
    private void applyFontSize() {
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        int fontSizeIndex = preferences.getInt("fontSizeIndex", 1);

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

        statusText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);


        resetButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);


        for (Button button : buttons) {
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize + 4);
        }

        Button backButton = findViewById(R.id.backButton);
        backButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
    }
}
