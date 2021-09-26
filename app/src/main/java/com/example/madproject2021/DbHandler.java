package com.example.madproject2021;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbRequest;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Blogosphere";
    private static final String USER_TABLE_NAME = "users";
    private static final String ARTICLE_TABLE_NAME = "articles";
    private static final String TAGS_TABLE_NAME = "tags";
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imagebyte;



    // User table column names
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_NAME_USER_NAME = "name";
    private static final String COLUMN_NAME_USER_EMAIL = "email";
    private static final String COLUMN_NAME_USER_PASSWORD = "password";
    private static final String COLUMN_NAME_USER_IMAGE = "image";
    private static final String COLUMN_NAME_USER_ABOUT = "about";



    public DbHandler(@Nullable Context context) {
        super(context,DATABASE_NAME,null ,VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_USER_CREATE_ENTRIES = "CREATE TABLE " + USER_TABLE_NAME + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME_USER_NAME + " TEXT," +
                COLUMN_NAME_USER_EMAIL + " TEXT," +
                COLUMN_NAME_USER_PASSWORD + " TEXT," +
                COLUMN_NAME_USER_IMAGE + " BLOB," +
                COLUMN_NAME_USER_ABOUT + " TEXT);";

        db.execSQL(SQL_USER_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String Drop_table_query = "DROP TABLE IF EXISTS " + USER_TABLE_NAME;
        db.execSQL(Drop_table_query);
       onCreate(db);

    }



    public UserModel getonerecord(int id){


     SQLiteDatabase sqlitedatabasee = getReadableDatabase();
     String query = " SELECT * FROM " + USER_TABLE_NAME+ " WHERE " +  COLUMN_USER_ID + "=" +id;
        Cursor cursor = sqlitedatabasee.rawQuery(query ,null);
        UserModel uermodel = new UserModel();


        if(cursor.moveToFirst()){
            uermodel.setName(cursor.getString(1));
            uermodel.setEmail(cursor.getString(2));
            uermodel.setAbout(cursor.getString(5));
            byte[] imagebytes= cursor.getBlob(4);
            Bitmap objectbitmap = BitmapFactory.decodeByteArray(imagebytes,0,imagebytes.length);
            uermodel.setImage(objectbitmap);

        }

        return  uermodel;

    }

    public int Upatesave(UserModel model){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_USER_NAME, model.getName());
        contentValues.put(COLUMN_NAME_USER_EMAIL, model.getEmail());
        contentValues.put(COLUMN_NAME_USER_ABOUT, model.getAbout());


        int stauts = db.update(USER_TABLE_NAME,contentValues, COLUMN_USER_ID +" =?",new String[]{String.valueOf(model.getId())} );
        db.close();
        return stauts;

    }


    public int imageinsert(UserModel modelobject){
        SQLiteDatabase db = getWritableDatabase();
        Bitmap imagetostore = modelobject.getImage();
        byteArrayOutputStream= new ByteArrayOutputStream();
        imagetostore.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        imagebyte = byteArrayOutputStream.toByteArray();
        System.out.println(imagebyte);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_USER_IMAGE, imagebyte);
        contentValues.put(COLUMN_NAME_USER_ABOUT, modelobject.getAbout());
        int stauts = db.update(USER_TABLE_NAME,contentValues, COLUMN_USER_ID +" =?",new String[]{String.valueOf(modelobject.getId())} );
        db.close();
        return stauts;
    }

    public int deleteaccount(int id){
        SQLiteDatabase db = getWritableDatabase();
        int result= db.delete(USER_TABLE_NAME,COLUMN_USER_ID + " =?",new String[]{String.valueOf(id)});
        db.close();
        return  result;

    }
}
