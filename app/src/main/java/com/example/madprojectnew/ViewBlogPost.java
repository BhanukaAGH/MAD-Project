package com.example.madprojectnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewBlogPost extends AppCompatActivity {

    private ImageView commentBtn;
    private TextView commentCount;
    Context context;
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_blog_post);

        context = this;

        dbHandler = new DbHandler(context);

        commentBtn = findViewById(R.id.imageViewComment);
        commentCount = findViewById(R.id.tv_CommnetCount);

        // get comment count from the table
        int countComment = dbHandler.countComments();
        commentCount.setText(String.valueOf(countComment));


        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ViewComment .class));
            }
        });
    }
}