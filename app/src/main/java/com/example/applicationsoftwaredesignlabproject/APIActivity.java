package com.example.applicationsoftwaredesignlabproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api);

        findViewById(R.id.btn_search).setOnClickListener(v -> {
            String URL = "https://api-for-android-app-project.onrender.com/data";

            Request request = new Request.Builder().url(URL).build();
            OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    if(e.getMessage() != null){
                        Log.e("查詢失敗", e.getMessage());
                    }
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(response.code() == 200){
                        if(response.body() == null) return;
                        Data data = new Gson().fromJson(response.body().string(), Data.class);
                        final String[] items = new String[data.result.results.length];
                        for(int i = 0; i < items.length; i++){
                            items[i] = "\n姓名：" + data.result.results[i].name +
                                    "\n年齡：" + data.result.results[i].age +
                                    "\n職業：" + data.result.results[i].occupation +
                                    "\n更新時間：" + data.result.results[i].UpdateTime;
                        }

                        runOnUiThread(() -> {
                            new AlertDialog.Builder(APIActivity.this)
                                    .setTitle("API資料")
                                    .setItems(items, null)
                                    .show();
                        });
                    }
                }
            });
        });
    }
}
