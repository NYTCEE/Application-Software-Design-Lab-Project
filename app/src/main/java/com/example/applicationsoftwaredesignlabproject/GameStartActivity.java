package com.example.applicationsoftwaredesignlabproject;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.media.MediaPlayer;

import android.util.TypedValue;
import android.content.SharedPreferences;


import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.HashMap;
import java.util.Map;

public class GameStartActivity extends AppCompatActivity {

    private TextView statusText;
    private Button resetButton;
    private Button backButton;
    private Button[] buttons = new Button[9];
    private String currentPlayer = "X";
    private boolean gameActive = true;
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayer2;
    private ImageView backgroundImage;
    private int stepCount = 0;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
        updateBackgroundImage();
        applyFontSize();

        mediaPlayer2 = MediaPlayer.create(this, R.raw.ooxx_music);
        mediaPlayer2.start();

        db = FirebaseFirestore.getInstance();   // 初始化 Firestore
        clearLeaderboard();
    }

    private void setupViews() {    //設定介面
        setContentView(R.layout.activity_game);
        backgroundImage = findViewById(R.id.backgroundImage);
        statusText = findViewById(R.id.statusText);

        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> resetGame());

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

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
            int index = i;
            buttons[i].setOnClickListener(v -> onButtonClick(index));
        }
    }

    private void onButtonClick(int index) {     //按鈕動作
        if (!gameActive) return;

        if (buttons[index].getText().toString().isEmpty()) {
            buttons[index].setText(currentPlayer);
            stepCount++;
            if (checkWin()) {

                if (currentPlayer.equals("X")) {
                    backgroundImage.setImageResource(R.drawable.x_win_image);
                    statusText.setText("Player " + currentPlayer + " Wins!");
                    GetPlayerData(currentPlayer, stepCount);

                } else {
                    backgroundImage.setImageResource(R.drawable.o_win_image);
                    statusText.setText("Player " + currentPlayer + " Wins!");
                    GetPlayerData(currentPlayer, stepCount);

                }
                gameActive = false;
            } else if (checkDraw()) {
                statusText.setText("It's a Draw!");
                backgroundImage.setImageResource(R.drawable.draw_image);

                mediaPlayer2.pause();

                mediaPlayer = MediaPlayer.create(this, R.raw.happy);
                mediaPlayer.start();
                gameActive = false;
            } else {
                currentPlayer = currentPlayer.equals("X") ? "O" : "X";
                statusText.setText("Player " + currentPlayer + "'s Turn");
            }
        }
    }

    private boolean checkWin() {    //檢查格子
        String[][] board = new String[3][3];
        for (int i = 0; i < 9; i++) {
            //列     //行
            board[i / 3][i % 3] = buttons[i].getText().toString();      //讀取
        }
        for (int i = 0; i < 3; i++) {       //檢查列
            if (!board[i][0].isEmpty() && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {       //檢查行
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
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().toString().isEmpty()) {
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
        stepCount = 0;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer2.start();
        for (int i = 0; i < 9; i++) {
            buttons[i].setText("");
            buttons[i].setEnabled(true);
        }
    }
    private void updateBackgroundImage() {      //深色模式
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean darkMode = preferences.getBoolean("darkMode", false);

        if (darkMode) {
            backgroundImage.setImageResource(R.drawable.darkbackground_image);
        } else {
            backgroundImage.setImageResource(R.drawable.background_image);
        }
    }

    private void applyFontSize() {      //字體大小
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


    @Override
    public void onBackPressed() {       //更改退出功能，確保音樂停止
        if (mediaPlayer2 != null) {
            mediaPlayer2.stop();
            mediaPlayer2.release();
            mediaPlayer2 = null;
        }
        super.onBackPressed();
    }

    ///******************************線上排行榜
    private void GetPlayerData(String playerSymbol, int steps) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Player " + currentPlayer + " Wins!");

        final EditText input = new EditText(this);
        input.setHint("Enter your name");
        builder.setView(input);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            String playerName = input.getText().toString().trim();
            if (!playerName.isEmpty()) {
                uploadScoreToFirestore(playerName, playerSymbol, steps);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

private void clearLeaderboard() {
    db.collection("leaderboard")
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    // 刪除每一個文檔
                    db.collection("leaderboard").document(document.getId()).delete()
                            .addOnSuccessListener(aVoid -> {
                                System.out.println("Leaderboard cleared!");
                            })
                            .addOnFailureListener(e -> {
                                e.printStackTrace();
                                System.out.println("Failed to clear leaderboard: " + e.getMessage());
                            });
                }
            })
            .addOnFailureListener(e -> {
                e.printStackTrace();
                System.out.println("Failed to fetch leaderboard for clearing: " + e.getMessage());
            });
}


    private void uploadScoreToFirestore(String playerName, String symbol, int steps) {
        Map<String, Object> scoreData = new HashMap<>();
        scoreData.put("playerName", playerName);
        scoreData.put("symbol", symbol);
        scoreData.put("steps", steps);
        scoreData.put("timestamp", System.currentTimeMillis());

        db.collection("leaderboard")
                .add(scoreData)
                .addOnSuccessListener(documentReference -> {
                    System.out.println("Score uploaded successfully!");
                    fetchLeaderboardFromFirestore(); // 上傳成功後更新排行榜
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    System.out.println("Failed to upload score: " + e.getMessage());
                });
    }

    private void fetchLeaderboardFromFirestore() {
        db.collection("leaderboard")
                .orderBy("steps") // 按步數排序
                .limit(10) // 只顯示前 10 名
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    StringBuilder leaderboard = new StringBuilder("Leaderboard:\n");
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String playerName = doc.getString("playerName");
                        String symbol = doc.getString("symbol");
                        long steps = doc.getLong("steps");
                        leaderboard.append(playerName)
                                .append(" (")
                                .append(symbol)
                                .append("): ")
                                .append(steps)
                                .append(" steps\n");
                    }
                    showLeaderboardDialog(leaderboard.toString());
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    System.out.println("Failed to fetch leaderboard: " + e.getMessage());
                });
    }

    private void showLeaderboardDialog(String leaderboard) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Leaderboard");
        builder.setMessage(leaderboard);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }


}
