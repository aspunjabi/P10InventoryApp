package com.example.android.p10inventoryapp;

import android.provider.BaseColumns;

/**
 * Created by aspun_000 on 8/29/2016.
 */
public final class DbContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String AUTO_INCREMENT = " AUTOINCREMENT";
    private static final String COMMA_SEP = " , ";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private DbContract() {
    }

    public static abstract class Table implements BaseColumns {

        // Contacts table name
        public static final String TABLE_NAME = "inventory";
        // Contacts Table Columns names
        public static final String KEY_ID = "id";
        public static final String KEY_NAME = "name";
        public static final String KEY_QUANTITY = "quantity";
        public static final String KEY_PRICE = "price";
        public static final String KEY_IMAGE = "image";


        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                KEY_ID + INTEGER_TYPE + PRIMARY_KEY + AUTO_INCREMENT + COMMA_SEP +
                KEY_NAME + TEXT_TYPE + COMMA_SEP +
                KEY_QUANTITY + INTEGER_TYPE + COMMA_SEP +
                KEY_PRICE + REAL_TYPE + COMMA_SEP +
                KEY_IMAGE + TEXT_TYPE +
                " );";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}
