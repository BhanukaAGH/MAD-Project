package com.example.madprojectnew;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewComment extends AppCompatActivity {

    ListView listView;
    private EditText comments;
    private Button add;
    private  ImageView close;
    private DbHandler dbHandler;
    private Context context;
    private List<Comment> allComments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comment);

        context = this;

        dbHandler = new DbHandler(context);

        listView = findViewById(R.id.listView);

        add = findViewById(R.id.buttonAdd);
        comments = findViewById(R.id.editTextComment);
        close = findViewById(R.id.imageViewClose);


        allComments = new ArrayList<>();
        allComments = dbHandler.getALLComments();

        CommentAdapter adapter = new CommentAdapter(context, R.layout.single_row, allComments);
        listView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int userId = 1;
                int blogId = 1;
                String userComments = comments.getText().toString();
                long date = System.currentTimeMillis();

                Comment comment =  new Comment(userId, blogId, userComments, date);
                dbHandler.addComment(comment);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Comment comment = allComments.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHandler.deleteComment(comment.getComId());
                        startActivity(new Intent(context, ViewComment.class));
                    }
                });
                builder.setNeutralButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, AddComment.class);
                        intent.putExtra("id", String.valueOf(comment.getComId()));
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ViewBlogPost.class));
            }
        });


    }
}


