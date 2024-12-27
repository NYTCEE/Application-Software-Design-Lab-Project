package com.example.applicationsoftwaredesignlabproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIActivity extends AppCompatActivity {
    private ImageView backgroundImage;
    private TextView loadingText;
    private Button backButton;
    private static final String API_URL = "https://api-for-android-app-project.onrender.com/data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api);

        initializeViews();
        setupBackButton();
        applySettings();
        fetchAPIData();
    }

    private void initializeViews() {
        backgroundImage = findViewById(R.id.backgroundImage);
        loadingText = findViewById(R.id.loadingText);
        backButton = findViewById(R.id.backButton);
        loadingText.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.INVISIBLE);
    }

    private void setupBackButton() {
        backButton.setOnClickListener(v -> {
            finish();  // 直接結束當前Activity
        });
    }

    private void applySettings() {
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        applyFontSize(preferences.getInt("fontSizeIndex", 1));
        updateBackgroundImage(preferences.getBoolean("darkMode", false));
    }

    private void applyFontSize(int fontSizeIndex) {
        float fontSize = 16f;
        switch (fontSizeIndex) {
            case 0: fontSize = 12f; break;
            case 1: fontSize = 16f; break;
            case 2: fontSize = 20f; break;
        }
        loadingText.setTextSize(fontSize);
    }

    private void updateBackgroundImage(boolean darkMode) {
        backgroundImage.setImageResource(darkMode ?
                R.drawable.darkbackground_image :
                R.drawable.background_image);
    }

    private void fetchAPIData() {
        Request request = new Request.Builder().url(API_URL).build();
        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if(e.getMessage() != null) {
                    Log.e("API Error", e.getMessage());
                    showError();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful() && response.body() != null) {
                    Data data = new Gson().fromJson(response.body().string(), Data.class);
                    processAndDisplayData(data);
                } else {
                    showError();
                }
            }
        });
    }

    private void processAndDisplayData(Data data) {
        String[] items = new String[data.result.results.length];
        for(int i = 0; i < items.length; i++) {
            items[i] = "\n姓名：" + data.result.results[i].name +
                    "\n年齡：" + data.result.results[i].age +
                    "\n職業：" + data.result.results[i].occupation +
                    "\n更新時間：" + data.result.results[i].UpdateTime;
        }

        runOnUiThread(() -> {
            loadingText.setVisibility(View.GONE);
            new AlertDialog.Builder(APIActivity.this)
                    .setTitle("API資料")
                    .setItems(items, null)
                    .show();
        });
    }

    private void showError() {
        runOnUiThread(() -> {
            loadingText.setVisibility(View.GONE);
            new AlertDialog.Builder(APIActivity.this)
                    .setTitle("Error")
                    .setMessage("獲取資料失敗，請稍後再試")
                    .setPositiveButton("確定", null)
                    .show();
        });
    }
}