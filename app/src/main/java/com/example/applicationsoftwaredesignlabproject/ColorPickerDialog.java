package com.example.applicationsoftwaredesignlabproject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.GridLayout;

public class ColorPickerDialog extends Dialog {
    private OnColorSelectedListener listener;

    public interface OnColorSelectedListener {
        void onColorSelected(int color);
    }

    public ColorPickerDialog(Context context, OnColorSelectedListener listener) {
        super(context);
        this.listener = listener;

        setContentView(R.layout.dialog_color_picker);
        setTitle("Select Color");

        GridLayout gridLayout = findViewById(R.id.colorGrid);
        int[] colors = {
                Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                Color.CYAN, Color.MAGENTA, Color.BLACK, Color.WHITE,
                Color.GRAY, Color.DKGRAY, Color.LTGRAY,
                Color.rgb(255, 165, 0), // Orange
                Color.rgb(128, 0, 128), // Purple
                Color.rgb(165, 42, 42), // Brown
                Color.rgb(255, 192, 203), // Pink
                Color.rgb(0, 128, 0) // Dark Green
        };

        for (int color : colors) {
            View colorView = new View(context);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 100;
            params.height = 100;
            params.setMargins(10, 10, 10, 10);
            colorView.setLayoutParams(params);
            colorView.setBackgroundColor(color);
            colorView.setOnClickListener(v -> {
                listener.onColorSelected(color);
                dismiss();
            });
            gridLayout.addView(colorView);
        }
    }
}