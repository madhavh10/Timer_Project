package com.example.timer_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TimerDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "timer.db";
    private static final int DATABASE_VERSION = 1;

    public TimerDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE input_time_table (id INTEGER PRIMARY KEY AUTOINCREMENT, input_time TEXT)");
        db.execSQL("CREATE TABLE timer_history (id INTEGER PRIMARY KEY AUTOINCREMENT, duration TEXT, end_time TEXT)");
        db.execSQL("CREATE TABLE sound_settings (id INTEGER PRIMARY KEY AUTOINCREMENT, selected_sound INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS input_time_table");
        db.execSQL("DROP TABLE IF EXISTS timer_history");
        db.execSQL("DROP TABLE IF EXISTS sound_settings");
        onCreate(db);
    }
}