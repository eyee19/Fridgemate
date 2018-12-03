package com.example.eyee3.fridgemate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SavedDBHelper extends SQLiteOpenHelper { //Database helper for saved recipes
    public static final String DATABASE_NAME = "savedlist.db";
    public static final int DATABASE_VERSION = 1;
    public SavedDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SAVEDLIST_TABLE = "CREATE TABLE " +
                SavedContract.SavedEntry.TABLE_NAME + " (" +
                SavedContract.SavedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SavedContract.SavedEntry.COLUMN_NAMES + " TEXT NOT NULL, " +
                SavedContract.SavedEntry.COLUMN_LINK + " TEXT NOT NULL, " +
                SavedContract.SavedEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_SAVEDLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SavedContract.SavedEntry.TABLE_NAME);
        onCreate(db);
    }
}
