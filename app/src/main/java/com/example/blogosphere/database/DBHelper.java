package com.example.blogosphere.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "Blogosphere";
    private static final String USER_TABLE_NAME = "users";
    private static final String ARTICLE_TABLE_NAME = "articles";
    private static final String TAGS_TABLE_NAME = "tags";

    // User table column names
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_NAME_USER_NAME = "name";
    private static final String COLUMN_NAME_USER_EMAIL = "email";
    private static final String COLUMN_NAME_USER_PASSWORD = "password";
    private static final String COLUMN_NAME_USER_ABOUT = "about";

    // Article table column names
    private static final String COLUMN_ARTICLE_ID = "id";
    private static final String COLUMN_NAME_ARTICLE_TITLE = "title";
    private static final String COLUMN_NAME_ARTICLE_IMAGE = "image";
    private static final String COLUMN_NAME_ARTICLE_CONTENT = "content";
    private static final String COLUMN_NAME_ARTICLE_WRITER_ID = "user_id";
    private static final String COLUMN_NAME_ARTICLE_WRITE_DATE = "date";

    // Tag table column name
    private static final String COLUMN_TAGS_ARTICLE_ID = "id";
    private static final String COLUMN_NAME_TAGS_TAG_NAME = "tag_name";


    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_USER_CREATE_ENTRIES = "CREATE TABLE " + USER_TABLE_NAME + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME_USER_NAME + " TEXT," +
                COLUMN_NAME_USER_EMAIL + " TEXT," +
                COLUMN_NAME_USER_PASSWORD + " TEXT," +
                COLUMN_NAME_USER_ABOUT + " TEXT);";

        String SQL_ARTICLE_CREATE_ENTRIES = "CREATE TABLE " + ARTICLE_TABLE_NAME + " (" +
                COLUMN_ARTICLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME_ARTICLE_TITLE + " TEXT," +
                COLUMN_NAME_ARTICLE_IMAGE + " BLOB," +
                COLUMN_NAME_ARTICLE_CONTENT + " TEXT," +
                COLUMN_NAME_ARTICLE_WRITER_ID + " INTEGER," +
                COLUMN_NAME_ARTICLE_WRITE_DATE + " TEXT);";

        String SQL_TAGS_CREATE_ENTRIES = "CREATE TABLE " + TAGS_TABLE_NAME + " (" +
                COLUMN_TAGS_ARTICLE_ID + " INTEGER," +
                COLUMN_NAME_TAGS_TAG_NAME + " TEXT," +
                "PRIMARY KEY ("+ COLUMN_TAGS_ARTICLE_ID + "," + COLUMN_NAME_TAGS_TAG_NAME + ") );";

        db.execSQL(SQL_USER_CREATE_ENTRIES);
        db.execSQL(SQL_ARTICLE_CREATE_ENTRIES);
        db.execSQL(SQL_TAGS_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ARTICLE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TAGS_TABLE_NAME);
        onCreate(db);
    }

    /*+++++ User Table Methods +++++*/
    public boolean userRegister(UserModel user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_USER_EMAIL, user.getEmail());
        values.put(COLUMN_NAME_USER_PASSWORD, user.getPassword());
        long result = db.insert(USER_TABLE_NAME, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkUserExists(UserModel user) {
        SQLiteDatabase db = getWritableDatabase();
        String CHECK_USER_EXISTS = "SELECT * FROM " + USER_TABLE_NAME +
                " WHERE " + COLUMN_NAME_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(CHECK_USER_EXISTS, new String[]{user.getEmail()});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean login(UserModel user) {
        SQLiteDatabase db = getWritableDatabase();
        String CHECK_USER_CREDENTIALS = "SELECT * FROM " + USER_TABLE_NAME +
                " WHERE " + COLUMN_NAME_USER_EMAIL + " = ? and " +
                COLUMN_NAME_USER_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(CHECK_USER_CREDENTIALS, new String[]{user.getEmail(), user.getPassword()});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void getLoginUserID(UserModel user) {
        SQLiteDatabase db = getWritableDatabase();
        String GET_USER_ID = "SELECT * FROM " + USER_TABLE_NAME +
                " WHERE " + COLUMN_NAME_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(GET_USER_ID, new String[]{user.getEmail()});

        if (cursor.moveToFirst()) {
            do {
                user.setId(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
    }

    public String getUserNameById(int userID){
        String authorName = "";
        SQLiteDatabase db = getWritableDatabase();
        String GET_USER_NAME = "SELECT " + COLUMN_NAME_USER_NAME + " FROM " + USER_TABLE_NAME +
                " WHERE " + COLUMN_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(GET_USER_NAME, new String[]{Integer.toString(userID)});

        if (cursor.moveToFirst()) {
            do {
                authorName = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return authorName;
    }

    /*+++++ Article Table Methods +++++*/
    public boolean publishArticle(ArticleModal article) {
        SQLiteDatabase db = getWritableDatabase();

        Bitmap articleImageBitmap = article.getImage();
        byteArrayOutputStream = new ByteArrayOutputStream();
        articleImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        imageInBytes = byteArrayOutputStream.toByteArray();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ARTICLE_TITLE, article.getTitle());
        values.put(COLUMN_NAME_ARTICLE_IMAGE, imageInBytes);
        values.put(COLUMN_NAME_ARTICLE_CONTENT, article.getContent());
        values.put(COLUMN_NAME_ARTICLE_WRITER_ID, article.getWriter_id());
        values.put(COLUMN_NAME_ARTICLE_WRITE_DATE, article.getDate());
        long result = db.insert(ARTICLE_TABLE_NAME, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Get all articles into a list
    public List<ArticleModal> getAllArticles() {

        List<ArticleModal> articles = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + ARTICLE_TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // Create new ArticleModel object
                ArticleModal article = new ArticleModal();
                // set methods
                article.setId(cursor.getInt(0));
                article.setTitle(cursor.getString(1));
                byte[] imageBytes = cursor.getBlob(2);
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                article.setImage(imageBitmap);
                article.setContent(cursor.getString(3));
                article.setWriter_id(cursor.getInt(4));
                article.setDate(cursor.getString(5));

                //articles [obj,objs,asas,asa]
                articles.add(article);
            } while (cursor.moveToNext());
        }
        return articles;
    }

    // Get article ID
    public void getArticleID(ArticleModal article){
        SQLiteDatabase db = getWritableDatabase();
        String GET_ARTICLE_ID = "SELECT * FROM " + ARTICLE_TABLE_NAME +
                " WHERE " + COLUMN_NAME_ARTICLE_TITLE + " = ? and " +
                COLUMN_NAME_ARTICLE_WRITER_ID + " = ?";
        Cursor cursor = db.rawQuery(GET_ARTICLE_ID, new String[]{article.getTitle(),Integer.toString(article.getWriter_id())});

        if (cursor.moveToFirst()) {
            do {
                article.setId(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
    }


    /*+++++ Article Tags Table Methods +++++*/
    public boolean insertArticleTags(int articleID, String tagName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TAGS_ARTICLE_ID, articleID);
        values.put(COLUMN_NAME_TAGS_TAG_NAME, tagName);
        long result = db.insert(TAGS_TABLE_NAME, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }





}