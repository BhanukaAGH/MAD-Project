package com.example.madproject2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Otherprofile_section extends AppCompatActivity {

    private   ListView view;
    private  ImageView ProfileImage;
    private  TextView UserName;
    private  TextView UserBio;
    private  TextView Followers;
    private Button Follow;
    private Context context;
    private DbHandler db;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherprofile_section);
        ProfileImage = findViewById(R.id.Update_profile_image);
        UserBio = findViewById(R.id.user_bio);
        UserName = findViewById(R.id.user_name);
        context = this;
        db =  new DbHandler(context);
        userModel = new UserModel();
        userModel = db.getonerecord(1);
        UserBio.setText(userModel.getAbout());
        UserName.setText(userModel.getName());
        ProfileImage.setImageBitmap(userModel.getImage());


        view= (ListView)findViewById(R.id.postlistviewpost);

        ArrayList<user> arrayList = new ArrayList<>();

        arrayList.add(new user(R.drawable.profilepic,"Aaron Caleb","There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. "));
        arrayList.add(new user(R.drawable.profilepic,"Aaron Caleb","Contrary to popular belief, Lorem Ipsum is not simply random text."));
        arrayList.add(new user(R.drawable.profilepic,"Aaron Caleb","There are many variations of passages of Lorem Ipsum available"));


        userAdapter apuser=  new userAdapter(this,R.layout.post_structure,arrayList);

        view.setAdapter(apuser);

    }
}