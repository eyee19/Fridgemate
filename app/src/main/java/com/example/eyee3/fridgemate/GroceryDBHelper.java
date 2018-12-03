package com.example.eyee3.fridgemate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.eyee3.fridgemate.GroceryContract.*;

public class GroceryDBHelper extends SQLiteOpenHelper { //Database helper for shopping list
    public static final String DATABASE_NAME = "grocerylist.db";
    public static final int DATABASE_VERSION = 1;
    public GroceryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_GROCERYLIST_TABLE = "CREATE TABLE " +
                GroceryEntry.TABLE_NAME + " (" +
                GroceryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GroceryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                GroceryEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_GROCERYLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GroceryEntry.TABLE_NAME);
        onCreate(db);
    }
}
