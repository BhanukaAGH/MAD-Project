package com.example.blogosphere.database;

public class CommentModal {
    private int comId, userId, blogId;
    private String comment;
    private long date;
    public CommentModal(){
    }
    public CommentModal(int comId, int userId, int blogId, String comment, long date) {
        this.comId = comId;
        this.userId = userId;
        this.blogId = blogId;
        this.comment = comment;
        this.date = date;
    }
    public CommentModal(int userId, int blogId, String comment, long date) {
        this.userId = userId;
        this.blogId = blogId;
        this.comment = comment;
        this.date = date;
    }
    public int getComId() {
        return comId;
    }
    public void setComId(int comId) {
        this.comId = comId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getBlogId() {
        return blogId;
    }
    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public long getDate() {
        return date;
    }
    public void setDate(long date) {
        this.date = date;
    }
}
