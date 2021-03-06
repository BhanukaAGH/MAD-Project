package com.example.blogosphere.database;

import android.graphics.Bitmap;

import java.io.Serializable;

public class UserModel implements Serializable {

    private int id;
    private String name, email,password, about;
    private Bitmap image;

    public UserModel(){}

    public UserModel(int id, Bitmap image){
        this.id = id;
        this.image = image;
    }

    public UserModel(String name, String email){
        this.name = name;
        this.email = email;
    }

    public UserModel(int id, String name, String email, String about) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.about = about;
    }

    public UserModel(Bitmap image, String name, String about) {
        this.image = image;
        this.name = name;
        this.about = about;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
