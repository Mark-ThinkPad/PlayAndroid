package com.example.bmicalculator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BMIDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BMI.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BMIData.BMIEntry.TABLE_NAME + " (" +
                    BMIData.BMIEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    BMIData.BMIEntry.COLUMN_NAME_NAME + " TEXT," +
                    BMIData.BMIEntry.COLUMN_NAME_SEX + " TEXT " +
                    "CHECK(sex IN ('male', 'female'))," +
                    BMIData.BMIEntry.COLUMN_NAME_HEIGHT + " FLOAT," +
                    BMIData.BMIEntry.COLUMN_NAME_WEIGHT + " FLOAT," +
                    BMIData.BMIEntry.COLUMN_NAME_BMI + " FLOAT," +
                    BMIData.BMIEntry.COLUMN_NAME_DATE +
                    " DATETIME DEFAULT(datetime('now', 'localtime')) )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BMIData.BMIEntry.TABLE_NAME;

    BMIDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
