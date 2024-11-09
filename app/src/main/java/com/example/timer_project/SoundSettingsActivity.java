package com.example.timer_project;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class SoundSettingsActivity extends AppCompatActivity {

    private RadioGroup soundOptions;
    private Button saveButton, previewButton;
    private int selectedSound;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_settings);

        soundOptions = findViewById(R.id.sound_options);
        saveButton = findViewById(R.id.save_button);
        previewButton = findViewById(R.id.preview_button);

        loadSelectedSound();

        saveButton.setOnClickListener(v -> saveSelectedSound());

        previewButton.setOnClickListener(v -> previewSelectedSound());

        soundOptions.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = findViewById(checkedId);
            selectedSound = getSoundResource(selectedRadioButton.getText().toString());
        });
    }

    private int getSoundResource(String soundName) {
        switch (soundName) {
            case "Sound 1":
                return R.raw.sound1;
            case "Sound 2":
                return R.raw.sound2;
            case "Sound 3":
                return R.raw.sound3;
            default:
                return R.raw.sound1; // Default sound
        }
    }

    private void loadSelectedSound() {
        SQLiteDatabase db = new TimerDatabaseHelper(this).getReadableDatabase();
        Cursor cursor = db.query("sound_settings", new String[]{"selected_sound"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            selectedSound = cursor.getInt(cursor.getColumnIndexOrThrow("selected_sound"));
            setSelectedRadioButton(selectedSound);
        }
        cursor.close();
    }

    private void setSelectedRadioButton(int soundResource) {
        int radioButtonId;
        if (soundResource == R.raw.sound1) {
            radioButtonId = R.id.sound_option_1;
        } else if (soundResource == R.raw.sound2) {
            radioButtonId = R.id.sound_option_2;
        } else if (soundResource == R.raw.sound3) {
            radioButtonId = R.id.sound_option_3;
        } else {
            radioButtonId = R.id.sound_option_1;
        }
        soundOptions.check(radioButtonId);
    }

    private void saveSelectedSound() {
        SQLiteDatabase db = new TimerDatabaseHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("selected_sound", selectedSound);
        int rowsUpdated = db.update("sound_settings", values, null, null);
        if (rowsUpdated == 0) {
            db.insert("sound_settings", null, values);
        }
        finish();
    }

    private void previewSelectedSound() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, selectedSound);
        mediaPlayer.start();
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