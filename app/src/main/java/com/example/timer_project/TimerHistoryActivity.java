package com.example.timer_project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TimerHistoryActivity extends AppCompatActivity {

    private ListView timerHistoryList;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_history);

        timerHistoryList = findViewById(R.id.timer_history_list);
        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(v -> finish());

        loadTimerHistory();
    }

    private void loadTimerHistory() {
        ArrayList<String> historyList = new ArrayList<>();
        SQLiteDatabase db = new TimerDatabaseHelper(this).getReadableDatabase();
        Cursor cursor = db.query("input_time_table", new String[]{"input_time"}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String inputTime = cursor.getString(cursor.getColumnIndexOrThrow("input_time"));
            historyList.add("Input Time: " + inputTime);
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
        timerHistoryList.setAdapter(adapter);
    }
}