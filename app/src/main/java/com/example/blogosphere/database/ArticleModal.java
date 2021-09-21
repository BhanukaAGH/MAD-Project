package com.example.blogosphere.database;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class ArticleModal implements Serializable {

    private int id, writer_id;
    private String title, content, date;
    private Bitmap image;
    private ArrayList<String> tags;

    public ArticleModal() {
    }

    public ArticleModal(int id, String title, Bitmap image, String content, ArrayList<String> tags, int writer_id, String date) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.content = content;
        this.tags = tags;
        this.writer_id = writer_id;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public int getWriter_id() {
        return writer_id;
    }

    public void setWriter_id(int writer_id) {
        this.writer_id = writer_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
