package com.example.madotherpages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    private RecyclerView followingList;
    private ListView articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        followingList = findViewById(R.id.following_list);

        ArrayList<Followers> followers = new ArrayList<>();

        followers.add(new Followers("Angelina",R.drawable.a));
        followers.add(new Followers("Morgan",R.drawable.b));
        followers.add(new Followers("Elina",R.drawable.c));
        followers.add(new Followers("Taylor",R.drawable.d));


        FollowingListRecViewAdapter adapter = new FollowingListRecViewAdapter();
        adapter.setFollowers(followers);

        followingList.setAdapter(adapter);
        followingList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

//        Article List
        int[] authorImg = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d};
        String[] author = {"Angelina","Morgan","Elina","Taylor",};
        String [] desc = {"New! 10x more awesome ways to write JSON configuration using Jsonnet.",
                "How to Create Dynamic Backgrounds With the CSS Paint API ",
                "How I landed a full stack developer job without a tech degree or work experience",
                "A Teenager’s View on Social Media"};
        int[] articleImg = {R.drawable.aa,R.drawable.ab,R.drawable.ac,R.drawable.ad};
        String[] date = {"27 Jun","14 Apr","10 Aug","12 Jan"};

        CustomAdapter adapterArticle = new CustomAdapter(this,author,desc,authorImg,articleImg,date);
        articleList = findViewById(R.id.articleList);
        articleList.setAdapter(adapterArticle);

    }
//    Navigation Button
    public void navigate_home(View view) {
        startActivity(new Intent(HomePage.this, HomePage.class));
    }

    public void navigate_bookmark(View view) {
        startActivity(new Intent(HomePage.this, Register_Activity.class));
    }


    public void navigate_createstory(View view) {
        startActivity(new Intent(HomePage.this, MadCreateArticle.class));
    }


    public void navigate_userprofile(View view) {
        startActivity(new Intent(HomePage.this, Login_Activity.class));
    }
}

class CustomAdapter extends ArrayAdapter<String>{
    Context context;
    String[] author;
    String[] desc;
    int[] authorImg;
    int[] articleImg;
    String[] date;

    CustomAdapter(Context context,String[] author,String[] desc,int[] authorImg,int[] articleImg,String[] date){
        super(context,R.layout.single_article_row,R.id.txtAuthor,author);
        this.context = context;
        this.author = author;
        this.desc = desc;
        this.authorImg = authorImg;
        this.articleImg = articleImg;
        this.date = date;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.single_article_row,parent,false);
        TextView authorView = row.findViewById(R.id.txtAuthor);
        TextView articlDesc = row.findViewById(R.id.txtDesc);
        ImageView authorImgView = row.findViewById(R.id.authorImg);
        ImageView articleImgView = row.findViewById(R.id.articleImg);
        TextView dateView = row.findViewById(R.id.txtDate);

        authorView.setText(author[position]);
        articlDesc.setText(desc[position]);
        authorImgView.setImageResource(authorImg[position]);
        articleImgView.setImageResource(articleImg[position]);
        dateView.setText(date[position]);

        return row;
    }
}
