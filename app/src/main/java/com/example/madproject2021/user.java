package com.example.madproject2021;

public class user {

    int image;
    String name;
    String Des;

    public user(int image, String name, String des) {
        this.image = image;
        this.name = name;
        Des = des;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }

}
