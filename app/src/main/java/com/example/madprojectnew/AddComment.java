package com.example.madprojectnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddComment extends AppCompatActivity {

    private EditText commentTxt;
    private ImageView closeBtn;
    private Button updateBtn;
    private DbHandler dbHandler;
    private Context context;
    private Long updateDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        context = this;
        dbHandler = new DbHandler(context);
        commentTxt = findViewById(R.id.etmUpdateComment);
        updateBtn = findViewById(R.id.buttonUpdate);
        closeBtn = findViewById(R.id.imageViewUpdClose);

        final String comId = getIntent().getStringExtra("id");
        Comment comment = dbHandler.getSingleComment(Integer.parseInt(comId));

        commentTxt.setText(comment.getComment());

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentTxt.getText().toString();
                updateDate = System.currentTimeMillis();

                int userId = 1;
                int blogId = 1;

                Comment comment1 = new Comment(Integer.parseInt(comId), userId, blogId, comment, updateDate);
                int state = dbHandler.updateComment(comment1);
                System.out.println(state);
                startActivity(new Intent(context, ViewComment.class));
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ViewComment.class));
            }
        });
    }
}