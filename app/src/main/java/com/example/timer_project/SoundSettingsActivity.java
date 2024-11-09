package com.example.timer_project;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SoundSettingsActivity extends AppCompatActivity {

    private RadioGroup soundOptions;
    private Button previewButton, saveButton;
    private MediaPlayer mediaPlayer;
    private int selectedSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_settings);

        soundOptions = findViewById(R.id.sound_options);
        previewButton = findViewById(R.id.preview_button);
        saveButton = findViewById(R.id.save_button);

        previewButton.setOnClickListener(v -> previewSound());
        saveButton.setOnClickListener(v -> saveSound());

        loadSavedSound();
    }

    private void previewSound() {
        int selectedId = soundOptions.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select a sound to preview", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

//        switch (selectedId) {
//            case R.id.sound_option_1:
//                mediaPlayer = MediaPlayer.create(this, R.raw.sound1);
//                break;
//            case R.id.sound_option_2:
//                mediaPlayer = MediaPlayer.create(this, R.raw.sound2);
//                break;
//            case R.id.sound_option_3:
//                mediaPlayer = MediaPlayer.create(this, R.raw.sound3);
//                break;
//        }

        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    private void saveSound() {
        int selectedId = soundOptions.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select a sound to save", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("TimerApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

//        switch (selectedId) {
//            case R.id.sound_option_1:
//                selectedSound = R.raw.sound1;
//                break;
//            case R.id.sound_option_2:
//                selectedSound = R.raw.sound2;
//                break;
//            case R.id.sound_option_3:
//                selectedSound = R.raw.sound3;
//                break;
//        }

        editor.putInt("selectedSound", selectedSound);
        editor.apply();

        Toast.makeText(this, "Sound saved", Toast.LENGTH_SHORT).show();
    }

    private void loadSavedSound() {
        SharedPreferences sharedPreferences = getSharedPreferences("TimerApp", MODE_PRIVATE);
//        selectedSound = sharedPreferences.getInt("selectedSound", R.raw.sound1);

//        RadioButton selectedRadioButton;
//        if (selectedSound == R.raw.sound1) {
//            selectedRadioButton = findViewById(R.id.sound_option_1);
//        } else if (selectedSound == R.raw.sound2) {
//            selectedRadioButton = findViewById(R.id.sound_option_2);
//        } else {
//            selectedRadioButton = findViewById(R.id.sound_option_3);
//        }

//        selectedRadioButton.setChecked(true);
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}