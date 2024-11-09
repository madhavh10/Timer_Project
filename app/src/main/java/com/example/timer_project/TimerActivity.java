package com.example.timer_project;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TimerActivity extends AppCompatActivity {

    private TextView timerDisplay;
    private EditText inputHours, inputMinutes, inputSeconds;
    private Button startButton, pauseButton, resetButton;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerDisplay = findViewById(R.id.timer_display);
        inputHours = findViewById(R.id.input_hours);
        inputMinutes = findViewById(R.id.input_minutes);
        inputSeconds = findViewById(R.id.input_seconds);
        startButton = findViewById(R.id.start_button);
        pauseButton = findViewById(R.id.pause_button);
        resetButton = findViewById(R.id.reset_button);

        startButton.setOnClickListener(v -> startTimer());
        pauseButton.setOnClickListener(v -> pauseTimer());
        resetButton.setOnClickListener(v -> resetTimer());
    }

    private void startTimer() {
        int hours = Integer.parseInt(inputHours.getText().toString());
        int minutes = Integer.parseInt(inputMinutes.getText().toString());
        int seconds = Integer.parseInt(inputSeconds.getText().toString());
        timeLeftInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000;

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                Toast.makeText(TimerActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                // Play sound notification here
            }
        }.start();

        timerRunning = true;
    }

    private void pauseTimer() {
        if (timerRunning) {
            countDownTimer.cancel();
            timerRunning = false;
        }
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = 0;
        updateTimer();
        timerRunning = false;
    }

    private void updateTimer() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerDisplay.setText(timeLeftFormatted);
    }
}