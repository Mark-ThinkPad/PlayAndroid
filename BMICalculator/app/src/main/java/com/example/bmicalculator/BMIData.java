package com.example.bmicalculator;

import android.provider.BaseColumns;

final class BMIData {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private BMIData() {}

    /* Inner class that defines the table contents */
    public static class BMIEntry implements BaseColumns {
        static final String TABLE_NAME = "BMIEntry";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_SEX = "sex";
        static final String COLUMN_NAME_HEIGHT = "height";
        static final String COLUMN_NAME_WEIGHT = "weight";
        static final String COLUMN_NAME_BMI = "bmi";
        static final String COLUMN_NAME_DATE = "date";

        public static final String DATE_FORMAT = "YYYY-MM-DD HH:MM:SS";
    }
}
