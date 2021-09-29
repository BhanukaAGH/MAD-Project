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
    private static final int VERSION = 8;
    private static final String DATABASE_NAME = "Blogosphere";
    private static final String USER_TABLE_NAME = "users";
    private static final String ARTICLE_TABLE_NAME = "articles";
    private static final String TAGS_TABLE_NAME = "tags";
    private static final String LIST_TABLE_NAME = "List";
    private static final String COMMENT_TABLE_NAME = "comments";

    // User table column names
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_NAME_USER_NAME = "name";
    private static final String COLUMN_NAME_USER_EMAIL = "email";
    private static final String COLUMN_NAME_USER_PASSWORD = "password";
    private static final String COLUMN_NAME_USER_IMAGE = "image";
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

    // List table column name
    private static final String COLUMN_NAME_LIST_ID = "List_ID";
    private static final String COLUMN_NAME_LIST_USER_ID = "user_ID";
    private static final String COLUMN_NAME_LIST_Topic = "List_Topic";
    private static final String COLUMN_NAME_LIST_DESCRIPTION = "Description";
    private static final String COLUMN_NAME_LIST_CREATED_DATE = "Date";
    private static final String COLUMN_NAME_LIST_STORY_COUNT = "Story_Count";

    // Comment table column name
    private static final String COLUMN_NAME_COMMENT_ID = "com_id";
    private static final String COLUMN_NAME_COMMENT_USER_ID = "user_id";
    private static final String COLUMN_NAME_COMMENT_BLOG_ID = "blog_id";
    private static final String COLUMN_NAME_COMMENT_COMMENT = "comment";
    private static final String COLUMN_NAME_COMMENT_DATE = "date";


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
                COLUMN_NAME_USER_IMAGE + " BLOB," +
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
                "PRIMARY KEY (" + COLUMN_TAGS_ARTICLE_ID + "," + COLUMN_NAME_TAGS_TAG_NAME + ") );";

        String LIST_TABLE_CREATE_QUERY = "CREATE TABLE " + LIST_TABLE_NAME + " " + "("
                + COLUMN_NAME_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + COLUMN_NAME_LIST_USER_ID + " INTEGER ,"
                + COLUMN_NAME_LIST_Topic + " TEXT ,"
                + COLUMN_NAME_LIST_DESCRIPTION + " TEXT ,"
                + COLUMN_NAME_LIST_CREATED_DATE + " TEXT ,"
                + COLUMN_NAME_LIST_STORY_COUNT + " INTEGER );";

        String COMMENT_TABLE_CREATE_QUERY = "CREATE TABLE " + COMMENT_TABLE_NAME + " " +
                "("
                + COLUMN_NAME_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME_COMMENT_USER_ID + " INTEGER,"
                + COLUMN_NAME_COMMENT_BLOG_ID + " INTEGER,"
                + COLUMN_NAME_COMMENT_COMMENT + " TEXT,"
                + COLUMN_NAME_COMMENT_DATE + " TEXT" +
                ");";

        db.execSQL(SQL_USER_CREATE_ENTRIES);
        db.execSQL(SQL_ARTICLE_CREATE_ENTRIES);
        db.execSQL(SQL_TAGS_CREATE_ENTRIES);
        db.execSQL(LIST_TABLE_CREATE_QUERY);
        db.execSQL(COMMENT_TABLE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ARTICLE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TAGS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LIST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COMMENT_TABLE_NAME);
        onCreate(db);
    }

    /*++++++++++++++++++++++++ User Table Methods ++++++++++++++++++++++++*/
    public boolean userRegister(UserModel user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_USER_EMAIL, user.getEmail());
        values.put(COLUMN_NAME_USER_PASSWORD, user.getPassword());
        long result = db.insert(USER_TABLE_NAME, null, values);

        if (result == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public boolean checkUserExists(UserModel user) {
        SQLiteDatabase db = getWritableDatabase();
        String CHECK_USER_EXISTS = "SELECT * FROM " + USER_TABLE_NAME +
                " WHERE " + COLUMN_NAME_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(CHECK_USER_EXISTS, new String[]{user.getEmail()});

        if (cursor.getCount() > 0) {
            db.close();
            return true;
        } else {
            db.close();
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
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }

    }

    public int getLoginUserID(String email) {
        SQLiteDatabase db = getWritableDatabase();
        String GET_USER_ID = "SELECT * FROM " + USER_TABLE_NAME +
                " WHERE " + COLUMN_NAME_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(GET_USER_ID, new String[]{email});

        if (cursor.moveToFirst()) {
            do {
                return cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        db.close();
        return 0;
    }

    public String getUserNameById(int userID) {
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
        db.close();
        return authorName;
    }

    /*++++++++++++++++++++++++ Article Table Methods ++++++++++++++++++++++++*/
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
        db.close();
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
        db.close();
        return articles;
    }

    // Get article ID
    public void getArticleID(ArticleModal article) {
        SQLiteDatabase db = getWritableDatabase();
        String GET_ARTICLE_ID = "SELECT * FROM " + ARTICLE_TABLE_NAME +
                " WHERE " + COLUMN_NAME_ARTICLE_TITLE + " = ? and " +
                COLUMN_NAME_ARTICLE_WRITER_ID + " = ?";
        Cursor cursor = db.rawQuery(GET_ARTICLE_ID, new String[]{article.getTitle(), Integer.toString(article.getWriter_id())});

        if (cursor.moveToFirst()) {
            do {
                article.setId(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        db.close();
    }

    // Get artivle by current user
    public List<ArticleModal> getCurrentUserAllArticles(String userID) {

        List<ArticleModal> articles = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + ARTICLE_TABLE_NAME + " WHERE " + COLUMN_NAME_ARTICLE_WRITER_ID +
                " =?";

        Cursor cursor = db.rawQuery(query, new String[]{userID});
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
        db.close();
        return articles;
    }

    // Get article by article ID
    public ArticleModal getSingleArticle(String articleID) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(ARTICLE_TABLE_NAME, new String[]{COLUMN_ARTICLE_ID,
                        COLUMN_NAME_ARTICLE_TITLE, COLUMN_NAME_ARTICLE_IMAGE, COLUMN_NAME_ARTICLE_CONTENT,
                        COLUMN_NAME_ARTICLE_WRITER_ID, COLUMN_NAME_ARTICLE_WRITE_DATE}, COLUMN_ARTICLE_ID + " = ?", new String[]{articleID}, null,
                null, null);

        ArticleModal article;
        if (cursor != null) {
            cursor.moveToFirst();
            article = new ArticleModal();
            article.setId(cursor.getInt(0));
            article.setTitle(cursor.getString(1));
            byte[] imageBytes = cursor.getBlob(2);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            article.setImage(imageBitmap);
            article.setContent(cursor.getString(3));
            article.setWriter_id(cursor.getInt(4));
            article.setDate(cursor.getString(5));

            db.close();
            return article;
        }
        db.close();
        return null;
    }

    // Edit & Update article
    public int updateArticle(ArticleModal article) {
        SQLiteDatabase db = getWritableDatabase();

        Bitmap articleImageBitmap = article.getImage();
        byteArrayOutputStream = new ByteArrayOutputStream();
        articleImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        imageInBytes = byteArrayOutputStream.toByteArray();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_ARTICLE_TITLE, article.getTitle());
        contentValues.put(COLUMN_NAME_ARTICLE_IMAGE, imageInBytes);
        contentValues.put(COLUMN_NAME_ARTICLE_CONTENT, article.getContent());
        contentValues.put(COLUMN_NAME_ARTICLE_WRITER_ID, article.getWriter_id());
        contentValues.put(COLUMN_NAME_ARTICLE_WRITE_DATE, article.getDate());

        int status = db.update(ARTICLE_TABLE_NAME, contentValues, COLUMN_ARTICLE_ID + " =?",
                new String[]{String.valueOf(article.getId())});

        db.close();
        return status;
    }

    // Delete One Article
    public void deleteArticle(int articleID) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(ARTICLE_TABLE_NAME, COLUMN_ARTICLE_ID + " =?", new String[]{Integer.toString(articleID)});
        db.close();
    }

    /*++++++++++++++++++++++++ Article Tags Table Methods ++++++++++++++++++++++++*/
    public boolean insertArticleTags(int articleID, String tagName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TAGS_ARTICLE_ID, articleID);
        values.put(COLUMN_NAME_TAGS_TAG_NAME, tagName);
        long result = db.insert(TAGS_TABLE_NAME, null, values);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Gihan Queries
    /*++++++++++++++++++++++++ Bookmark Table Methods ++++++++++++++++++++++++*/
    public void addToTheList(ListModal listModel) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME_LIST_USER_ID, listModel.getUser_ID());
        contentValues.put(COLUMN_NAME_LIST_Topic, listModel.getList_Topic());
        contentValues.put(COLUMN_NAME_LIST_DESCRIPTION, listModel.getList_Description());
        contentValues.put(COLUMN_NAME_LIST_CREATED_DATE, listModel.getCreated_date());
        contentValues.put(COLUMN_NAME_LIST_STORY_COUNT, listModel.getStory_Count());
        db.insert(LIST_TABLE_NAME, null, contentValues);

        db.close();
    }

    //get alll the created lists.
    public List<ListModal> getAllLists() {
        List<ListModal> listmodel = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        String query = " SELECT * FROM " + LIST_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                ListModal listmodel_obj = new ListModal();
                listmodel_obj.setList_ID(cursor.getInt(0));
                listmodel_obj.setUser_ID(cursor.getInt(1));
                listmodel_obj.setList_Topic(cursor.getString(2));
                listmodel_obj.setList_Description(cursor.getString(3));
                listmodel_obj.setCreated_date(cursor.getString(4));
                listmodel_obj.setStory_Count(cursor.getInt(5));
                listmodel.add(listmodel_obj);
            } while (cursor.moveToNext());
        }
        return listmodel;
    }


    //for deleting an item
    public void deleteList(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(LIST_TABLE_NAME, " List_ID =? ", new String[]{String.valueOf(id)});
        db.close();
    }

    //get single List
    public ListModal getSingleList(int list_id) {

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(LIST_TABLE_NAME, new String[]{COLUMN_NAME_LIST_ID, COLUMN_NAME_LIST_USER_ID, COLUMN_NAME_LIST_Topic, COLUMN_NAME_LIST_DESCRIPTION},
                COLUMN_NAME_LIST_ID + " = ?", new String[]{String.valueOf(list_id)}, null, null, null);

        ListModal listModeledit;

        if (cursor != null) {
            cursor.moveToFirst();
            listModeledit = new ListModal(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            return listModeledit;

        }
        return null;
    }

    //update List topic
    public int updateSingleList(ListModal listModel) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME_LIST_USER_ID, listModel.getUser_ID());
        contentValues.put(COLUMN_NAME_LIST_Topic, listModel.getList_Topic());
        contentValues.put(COLUMN_NAME_LIST_DESCRIPTION, listModel.getList_Description());

        int status = db.update(LIST_TABLE_NAME, contentValues, COLUMN_NAME_LIST_ID + " =?",
                new String[]{String.valueOf(listModel.getList_ID())});
        db.close();
        return status;
    }


    // Buddheesha Queries
    /*++++++++++++++++++++++++ Comment Table Methods ++++++++++++++++++++++++*/
    //add a comment
    public void addComment(CommentModal comment) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_COMMENT_USER_ID, comment.getUserId());
        contentValues.put(COLUMN_NAME_COMMENT_BLOG_ID, comment.getBlogId());
        contentValues.put(COLUMN_NAME_COMMENT_COMMENT, comment.getComment());
        contentValues.put(COLUMN_NAME_COMMENT_DATE, comment.getDate());
        sqLiteDatabase.insert(COMMENT_TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    // count comments table records
    public int countComments() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + COMMENT_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        db.close();
        return count;
    }

    //get All comments into a list
    public List<CommentModal> getALLComments() {
        List<CommentModal> comments = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + COMMENT_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                CommentModal comment = new CommentModal();
                comment.setComId(cursor.getInt(0));
                comment.setUserId(cursor.getInt(1));
                comment.setBlogId(cursor.getInt(2));
                comment.setComment(cursor.getString(3));
                comment.setDate(cursor.getLong(4));
                comments.add(comment);
            } while (cursor.moveToNext());
        }
        db.close();
        return comments;
    }

    //Delete Comment
    public void deleteComment(int ComId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(COMMENT_TABLE_NAME, COLUMN_NAME_COMMENT_ID + "=?", new String[]{String.valueOf(ComId)});
        db.close();
    }

    //Get a single comment
    public CommentModal getSingleComment(int ComId) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(COMMENT_TABLE_NAME, new String[]{COLUMN_NAME_COMMENT_ID, COLUMN_NAME_COMMENT_USER_ID, COLUMN_NAME_COMMENT_BLOG_ID, COLUMN_NAME_COMMENT_COMMENT, COLUMN_NAME_COMMENT_DATE}, COLUMN_NAME_COMMENT_ID + "= ?", new String[]{String.valueOf(ComId)}, null, null, null);
        CommentModal comment;
        if (cursor != null) {
            cursor.moveToFirst();
            comment = new CommentModal(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getLong(4)
            );
            db.close();
            return comment;
        }
        db.close();
        return null;
    }

    //Update a comment
    public int updateComment(CommentModal comment) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_COMMENT_USER_ID, comment.getUserId());
        contentValues.put(COLUMN_NAME_COMMENT_BLOG_ID, comment.getBlogId());
        contentValues.put(COLUMN_NAME_COMMENT_COMMENT, comment.getComment());
        contentValues.put(COLUMN_NAME_COMMENT_DATE, comment.getDate());
        int status = db.update(COMMENT_TABLE_NAME, contentValues, COLUMN_NAME_COMMENT_ID + " =?", new String[]{String.valueOf(comment.getComId())});
        db.close();
        return status;
    }


    // Hasith Queries
    /*++++++++++++++++++++++++ User Table Methods ++++++++++++++++++++++++*/
    public UserModel getUserbyID(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = " SELECT * FROM " + USER_TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(id)});
        UserModel uermodel = new UserModel();

        if (cursor.moveToFirst()) {
            uermodel.setName(cursor.getString(1));
            uermodel.setEmail(cursor.getString(2));
            uermodel.setAbout(cursor.getString(5));
            byte[] imagebytes = cursor.getBlob(4);
            if (imagebytes != null) {
            Bitmap objectbitmap = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);
                uermodel.setImage(objectbitmap);
            }
        }
        db.close();
        return uermodel;
    }

    public int Upatesave(UserModel model) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_USER_NAME, model.getName());
        contentValues.put(COLUMN_NAME_USER_EMAIL, model.getEmail());
        contentValues.put(COLUMN_NAME_USER_ABOUT, model.getAbout());

        int stauts = db.update(USER_TABLE_NAME, contentValues, COLUMN_USER_ID + " =?", new String[]{String.valueOf(model.getId())});
        db.close();
        return stauts;
    }

    public int imageinsert(UserModel modelobject) {
        SQLiteDatabase db = getWritableDatabase();
        Bitmap imagetostore = modelobject.getImage();
        byteArrayOutputStream = new ByteArrayOutputStream();
        imagetostore.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        imageInBytes = byteArrayOutputStream.toByteArray();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_USER_NAME, modelobject.getName());
        contentValues.put(COLUMN_NAME_USER_IMAGE, imageInBytes);
        contentValues.put(COLUMN_NAME_USER_ABOUT, modelobject.getAbout());
        int stauts = db.update(USER_TABLE_NAME, contentValues, COLUMN_USER_ID + " =?", new String[]{String.valueOf(modelobject.getId())});
        db.close();
        return stauts;
    }

    public int deleteaccount(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(USER_TABLE_NAME, COLUMN_USER_ID + " =?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

}
