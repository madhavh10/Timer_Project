package com.example.timer_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class FlashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(FlashActivity.this, TimerActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}