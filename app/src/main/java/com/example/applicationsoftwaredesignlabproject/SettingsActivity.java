package com.example.applicationsoftwaredesignlabproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private Switch notificationSwitch;
    private Switch darkModeSwitch;
    private Spinner fontSizeSpinner;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // Initialize views
        initializeViews();
        // Setup font size options
        setupFontSizeSpinner();
        // Setup button click events
        setupClickListeners();
        // Load saved settings
        loadSavedSettings();
    }

    private void initializeViews() {
        notificationSwitch = findViewById(R.id.notificationSwitch);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        fontSizeSpinner = findViewById(R.id.fontSizeSpinner);
        saveButton = findViewById(R.id.saveButton);
    }

    private void setupFontSizeSpinner() {
        String[] fontSizes = {"Small", "Medium", "Large"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                fontSizes
        );
        fontSizeSpinner.setAdapter(adapter);
    }

    private void setupClickListeners() {
        saveButton.setOnClickListener(v -> saveSettings());
    }

    private void loadSavedSettings() {
        // Load settings from SharedPreferences
        // Demonstrating with default values
        notificationSwitch.setChecked(true);
        darkModeSwitch.setChecked(false);
        fontSizeSpinner.setSelection(1); // Default to "Medium"
    }

    private void saveSettings() {
        // Save settings to SharedPreferences
        boolean notifications = notificationSwitch.isChecked();
        boolean darkMode = darkModeSwitch.isChecked();
        String fontSize = fontSizeSpinner.getSelectedItem().toString();

        // Add saving logic here
        // Example: Show a Toast
        Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();

        // Return to MainActivity
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // End the current activity
    }
}
