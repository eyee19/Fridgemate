package com.example.eyee3.fridgemate;

import android.provider.BaseColumns;

public class GroceryContract { //Contract database for shopping list, declaring columns
    private GroceryContract() {}

    public static final class GroceryEntry implements BaseColumns {
        public static final String TABLE_NAME = "groceryList";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
