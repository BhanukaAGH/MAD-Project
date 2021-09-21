package com.example.madotherpages.database;

import android.provider.BaseColumns;

public final class UsersMaster {

    private UsersMaster() {
    }

    //    Inner class that defines the table contents
    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "users";

        //    Column names
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";

    }
}