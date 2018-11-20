package com.example.eyee3.fridgemate;

import android.provider.BaseColumns;

public class FridgeContract {
    private FridgeContract() {}

    public static final class FridgeEntry implements BaseColumns {
        public static final String TABLE_NAME = "fridgeList";
        public static final String COLUMN_NAMEF = "nameF";
        public static final String COLUMN_DATE_ADDED = "dateAdded";
        public static final String COLUMN_DATE_EXP = "dateExp";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }
}
