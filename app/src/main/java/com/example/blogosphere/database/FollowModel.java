package com.example.blogosphere.database;

public class FollowModel {
    int currentid;
    int userid;
    public FollowModel() {

    }

    public int getCurrentid() {
        return currentid;
    }

    public void setCurrentid(int currentid) {
        this.currentid = currentid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public FollowModel(int currentid, int userid) {
        this.currentid = currentid;
        this.userid = userid;
    }
}
