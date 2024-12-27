package com.example.applicationsoftwaredesignlabproject;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class DrawActivity extends AppCompatActivity {
    private ImageView backgroundImage;
    private MyCanvasView canvasView;
    private boolean isEraserMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw);

        backgroundImage = findViewById(R.id.backgroundImage);
        updateBackgroundImage();

        canvasView = findViewById(R.id.myCanvasView);

        Button btClear = findViewById(R.id.button_Clear);
        Button btEraser = findViewById(R.id.button_Eraser);
        Button btColorPicker = findViewById(R.id.button_ColorPicker);
        SeekBar eraserSizeBar = findViewById(R.id.seekBar_eraser);

        btClear.setOnClickListener(v -> canvasView.clear());

        btEraser.setOnClickListener(v -> {
            isEraserMode = !isEraserMode;
            if (isEraserMode) {
                canvasView.enableEraser(eraserSizeBar.getProgress());
                btEraser.setText("Draw");
            } else {
                canvasView.disableEraser();
                btEraser.setText("Eraser");
            }
        });

        eraserSizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isEraserMode) {
                    canvasView.setEraserSize(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btColorPicker.setOnClickListener(v -> {
            ColorPickerDialog dialog = new ColorPickerDialog(this, color -> {
                isEraserMode = false;
                btEraser.setText("Eraser");
                canvasView.setColor(color);
            });
            dialog.show();
        });
    }

    private void updateBackgroundImage() {
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean darkMode = preferences.getBoolean("darkMode", false);
        backgroundImage.setImageResource(darkMode ?
                R.drawable.darkbackground_image : R.drawable.background_image);
    }
}