package com.example.madprojectnew;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "blogs";
    private static final String TABLE_NAME = "comments";

    // Column names
    private static final String COM_ID = "com_id";
    private static final String USER_ID = "user_id";
    private static final String BLOG_ID = "blog_id";
    private static final String COMMENT = "comment";
    private static final String DATE = "date";

    public DbHandler(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String TABLE_CREATE_QUERY = "CREATE TABLE "+TABLE_NAME+" " +
                "("
                +COM_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +USER_ID + " INTEGER,"
                +BLOG_ID + " INTEGER,"
                +COMMENT + " TEXT,"
                +DATE+" TEXT" +
                ");";
        /*
            CREATE TABLE comments (id INTEGER PRIMARY KEY AUTOINCREMENT, userID TEXT, postID TEXT, comment TEXT,
            started TEXT,finished TEXT); */
        db.execSQL(TABLE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS "+ TABLE_NAME;
        // Drop older table if existed
        db.execSQL(DROP_TABLE_QUERY);
        // Create tables again
        onCreate(db);
    }

    //add a comment
    public void addComment(Comment comment){
        SQLiteDatabase  sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_ID, comment.getUserId());
        contentValues.put(BLOG_ID, comment.getBlogId());
        contentValues.put(COMMENT,comment.getComment());
        contentValues.put(DATE, comment.getDate());

        //save to table
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        //close database
        sqLiteDatabase.close();
    }

    // count comments table records
    public int countComments(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME;

        Cursor cursor = db.rawQuery(query,null);
        return cursor.getCount();
    }

    //get All comments into a list
    public List<Comment> getALLComments(){

        List<Comment> comments = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                //Create new comment object
                Comment comment = new Comment();

                comment.setComId(cursor.getInt(0));
                comment.setUserId(cursor.getInt(1));
                comment.setBlogId(cursor.getInt(2));
                comment.setComment(cursor.getString(3));
                comment.setDate(cursor.getLong(4));

                comments.add(comment);
            }while (cursor.moveToNext());
        }
        return comments;

    }
    //Delete Comment
    public void  deleteComment(int ComId){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COM_ID + "=?", new String[]{String.valueOf(ComId)});
    }

    //Get a single comment
    public Comment getSingleComment(int ComId){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COM_ID, USER_ID, BLOG_ID, COMMENT, DATE}, COM_ID + "= ?", new String[]{String.valueOf(ComId)}, null, null, null);

        Comment comment;
        if (cursor != null){
            cursor.moveToFirst();
            comment =  new Comment(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getLong(4)

            );
            return comment;
        }
        return null;
    }

    //Update a comment
    public int updateComment(Comment comment){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_ID, comment.getUserId());
        contentValues.put(BLOG_ID, comment.getBlogId());
        contentValues.put(COMMENT,comment.getComment());
        contentValues.put(DATE, comment.getDate());

        int status = db.update(TABLE_NAME, contentValues, COM_ID +" =?", new String[]{String.valueOf(comment.getComId())});

        db.close();
        return status;
    }

}
