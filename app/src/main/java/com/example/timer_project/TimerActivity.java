package com.example.timer_project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    private EditText inputHours, inputMinutes, inputSeconds;
    private TextView timerDisplay;
    private Button startButton, historyButton, soundSettingsButton, resetButton, pauseButton;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean timerRunning;
    private MediaPlayer mediaPlayer;
    private int selectedSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        inputHours = findViewById(R.id.input_hours);
        inputMinutes = findViewById(R.id.input_minutes);
        inputSeconds = findViewById(R.id.input_seconds);
        timerDisplay = findViewById(R.id.timer_display);
        startButton = findViewById(R.id.start_button);
        historyButton = findViewById(R.id.timer_history_button);
        soundSettingsButton = findViewById(R.id.sound_settings_button);
        resetButton = findViewById(R.id.reset_button);
        pauseButton = findViewById(R.id.pause_button);

        startButton.setOnClickListener(v -> startTimer());
        historyButton.setOnClickListener(v -> openTimerHistory());
        soundSettingsButton.setOnClickListener(v -> openSoundSettings());
        resetButton.setOnClickListener(v -> resetTimer());
        pauseButton.setOnClickListener(v -> pauseTimer());

        loadSelectedSound();
    }

    private void startTimer() {
        String hoursStr = inputHours.getText().toString();
        String minutesStr = inputMinutes.getText().toString();
        String secondsStr = inputSeconds.getText().toString();

        if (hoursStr.isEmpty() || minutesStr.isEmpty() || secondsStr.isEmpty()) {
            Toast.makeText(this, "Please enter valid time", Toast.LENGTH_SHORT).show();
            return;
        }

        int hours = Integer.parseInt(hoursStr);
        int minutes = Integer.parseInt(minutesStr);
        int seconds = Integer.parseInt(secondsStr);
        timeLeftInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000;

        saveInputTime(hours, minutes, seconds); // Save the input time to the database

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                timerDisplay.setText("00:00:00");
                playNotificationSound();
                saveTimerHistory();
                Toast.makeText(TimerActivity.this, "Time's Up", Toast.LENGTH_SHORT).show();
            }
        }.start();

        timerRunning = true;
    }

    private void updateTimer() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        timerDisplay.setText(timeLeftFormatted);
    }

    private void saveInputTime(int hours, int minutes, int seconds) {
        String inputTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        SQLiteDatabase db = new TimerDatabaseHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("input_time", inputTime);
        db.insert("input_time_table", null, values);
    }

    private void saveTimerHistory() {
        String duration = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                (int) (timeLeftInMillis / 1000) / 3600,
                (int) ((timeLeftInMillis / 1000) % 3600) / 60,
                (int) (timeLeftInMillis / 1000) % 60);
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        SQLiteDatabase db = new TimerDatabaseHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("duration", duration);
        values.put("end_time", endTime);
        db.insert("timer_history", null, values);
    }

    private void openTimerHistory() {
        Intent intent = new Intent(TimerActivity.this, TimerHistoryActivity.class);
        startActivity(intent);
    }

    private void openSoundSettings() {
        Intent intent = new Intent(TimerActivity.this, SoundSettingsActivity.class);
        startActivity(intent);
    }

    private void playNotificationSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, selectedSound);
        }
        mediaPlayer.start();
    }

    private void loadSelectedSound() {
        SQLiteDatabase db = new TimerDatabaseHelper(this).getReadableDatabase();
        Cursor cursor = db.query("sound_settings", new String[]{"selected_sound"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            selectedSound = cursor.getInt(cursor.getColumnIndexOrThrow("selected_sound"));
        } else {
            selectedSound = R.raw.sound1; // Default sound if no selection is found
            // Insert default sound into the database if no selection is found
            ContentValues values = new ContentValues();
            values.put("selected_sound", selectedSound);
            db.insert("sound_settings", null, values);
        }
        cursor.close();
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = 0;
        updateTimer();
        timerRunning = false;
        inputHours.setText("");
        inputMinutes.setText("");
        inputSeconds.setText("");
    }

    private void pauseTimer() {
        if (timerRunning) {
            countDownTimer.cancel();
            timerRunning = false;
        } else {
            startTimer();
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }
}