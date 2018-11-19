package com.example.eyee3.fridgemate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FridgeDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "fridgelist.db";
    public static final int DATABASE_VERSION = 1;
    public FridgeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FRIDGELIST_TABLE = "CREATE TABLE " +
                FridgeContract.FridgeEntry.TABLE_NAME + " (" +
                FridgeContract.FridgeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FridgeContract.FridgeEntry.COLUMN_NAMEF + " TEXT NOT NULL, " +
                FridgeContract.FridgeEntry.COLUMN_DATE_ADDED + " TEXT NOT NULL, " +
                FridgeContract.FridgeEntry.COLUMN_DATE_EXP + " TEXT NOT NULL, " +
                FridgeContract.FridgeEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_FRIDGELIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FridgeContract.FridgeEntry.TABLE_NAME);
        onCreate(db);
    }
}
