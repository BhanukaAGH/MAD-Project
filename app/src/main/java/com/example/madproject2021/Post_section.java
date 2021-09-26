package com.example.madproject2021;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class Post_section extends AppCompatActivity {

    ListView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_section);


        view= (ListView)findViewById(R.id.listviewpost);


        ArrayList<user> arrayList = new ArrayList<>();

        arrayList.add(new user(R.drawable.profileimage,"Mark Dale","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,"));
        arrayList.add(new user(R.drawable.profileimage,"Mark Dale","Contrary to popular belief, Lorem Ipsum is not simply random text."));
        arrayList.add(new user(R.drawable.profileimage,"Mark Dale","There are many variations of passages of Lorem Ipsum available"));


        userAdapter apuser=  new userAdapter(this,R.layout.raw,arrayList);

        view.setAdapter(apuser);
    }
}