package com.example.madproject2021;

import android.graphics.Bitmap;

import java.io.Serializable;

public class UserModel implements Serializable {

    private int id;
    private String name, email, password, about;
    private Bitmap image;

    public UserModel(int id, Bitmap image) {
        this.id = id;
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public UserModel() {
    }

    public UserModel(String name, String email) {
        this.name = name;
        this.email = email;

    }

    public UserModel(int id, String name, String email, String about) {
        this.id = id;
        this.name = name;
        this.email = email;
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


}