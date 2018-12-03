package com.example.eyee3.fridgemate;

import android.provider.BaseColumns;

public class SavedContract { //Contract database for saved recipes, declaring columns
    private SavedContract() {}

    public static final class SavedEntry implements BaseColumns {
        public static final String TABLE_NAME = "savedList";
        public static final String COLUMN_NAMES = "nameS";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }
}
