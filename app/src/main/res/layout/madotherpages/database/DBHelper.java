package com.example.madotherpages.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Blogosphere";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + UsersMaster.Users.TABLE_NAME + " (" +
                UsersMaster.Users._ID + " INTEGER PRIMARY KEY," +
                UsersMaster.Users.COLUMN_NAME_EMAIL + " TEXT," +
                UsersMaster.Users.COLUMN_NAME_PASSWORD + " TEXT);";

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UsersMaster.Users.TABLE_NAME);
        onCreate(db);
    }

    public boolean userRegister(String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UsersMaster.Users.COLUMN_NAME_EMAIL, email);
        values.put(UsersMaster.Users.COLUMN_NAME_PASSWORD, password);
        long result = db.insert(UsersMaster.Users.TABLE_NAME, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkUserExists(String email) {
        SQLiteDatabase db = getWritableDatabase();
        String CHECK_USER_EXISTS = "SELECT * FROM " + UsersMaster.Users.TABLE_NAME +
                " WHERE " + UsersMaster.Users.COLUMN_NAME_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(CHECK_USER_EXISTS, new String[]{email});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean login(String email, String password){
        SQLiteDatabase db = getWritableDatabase();
        String CHECK_USER_CREDENTIALS = "SELECT * FROM " + UsersMaster.Users.TABLE_NAME +
                " WHERE " + UsersMaster.Users.COLUMN_NAME_EMAIL + " = ? and " +
                UsersMaster.Users.COLUMN_NAME_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(CHECK_USER_CREDENTIALS,new String[]{email,password});
        if(cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }
}
