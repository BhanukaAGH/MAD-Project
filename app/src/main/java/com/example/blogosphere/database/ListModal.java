package com.example.blogosphere.database;

public class ListModal {

    private int List_ID ,User_ID ,Story_Count;
    private String List_Topic,List_Description ;
    private String Created_date ;

    public ListModal(){

    }

    public ListModal(int list_ID, int user_ID, int story_Count, String list_Topic, String list_Description, String created_date) {
        List_ID = list_ID;
        User_ID = user_ID;
        Story_Count = story_Count;
        List_Topic = list_Topic;
        List_Description = list_Description;
        Created_date = created_date;
    }

    public ListModal(int user_ID, int story_Count, String list_Topic, String list_Description, String created_date) {
        User_ID = user_ID;
        Story_Count = story_Count;
        List_Topic = list_Topic;
        List_Description = list_Description;
        Created_date = created_date;
    }


    public ListModal(int list_ID, int user_ID, String list_Topic, String list_Description) {
        List_ID = list_ID;
        User_ID = user_ID;
        List_Topic = list_Topic;
        List_Description = list_Description;
    }

    public int getList_ID() {
        return List_ID;
    }

    public void setList_ID(int list_ID) {
        List_ID = list_ID;
    }

    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    public int getStory_Count() {
        return Story_Count;
    }

    public void setStory_Count(int story_Count) {
        Story_Count = story_Count;
    }

    public String getList_Topic() {
        return List_Topic;
    }

    public void setList_Topic(String list_Topic) {
        List_Topic = list_Topic;
    }

    public String getList_Description() {
        return List_Description;
    }

    public void setList_Description(String list_Description) {
        List_Description = list_Description;
    }

    public String getCreated_date() {
        return Created_date;
    }

    public void setCreated_date(String created_date) {
        Created_date = created_date;
    }
}