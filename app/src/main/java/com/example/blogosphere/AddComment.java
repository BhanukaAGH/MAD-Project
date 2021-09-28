package com.example.blogosphere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blogosphere.database.CommentModal;
import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.UserModel;

public class AddComment extends AppCompatActivity {

    private EditText commentTxt;
    private ImageView closeBtn;
    private Button updateBtn;
    private DBHelper myDB;
    private Context context;
    private Long updateDate;

    UserModel user;
    String articleId;
    String comId;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        context = this;
        myDB = new DBHelper(context);

        // Get login user object
//        if (user == null) {
//            Intent i = getIntent();
//            user = (UserModel) i.getSerializableExtra("UserObject");
//        }

        userID = getIntent().getIntExtra("UserID",0);
        articleId = getIntent().getStringExtra("StoryID");
        user = myDB.getUserbyID(userID);

        commentTxt = findViewById(R.id.etmUpdateComment);
        updateBtn = findViewById(R.id.buttonUpdate);
        closeBtn = findViewById(R.id.imageViewUpdClose);
        if (comId == null) {
            comId = getIntent().getStringExtra("id");
        }

        CommentModal comment = myDB.getSingleComment(Integer.parseInt(comId));

        commentTxt.setText(comment.getComment());
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentTxt.getText().toString();
                updateDate = System.currentTimeMillis();
                int userId = 1;
                int blogId = 1;
                CommentModal comment1 = new CommentModal(Integer.parseInt(comId), userId, blogId, comment, updateDate);
                int state = myDB.updateComment(comment1);
                System.out.println(state);
                Intent updateIntent = new Intent(context, ViewComment.class);
                updateIntent.putExtra("UserID", userID);
                updateIntent.putExtra("StoryID", articleId);
                updateIntent.putExtra("id", comId);
                startActivity(updateIntent);
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent closeIntent = new Intent(context, ViewComment.class);
                closeIntent.putExtra("UserID", userID);
                closeIntent.putExtra("StoryID", articleId);
                closeIntent.putExtra("id", comId);
                startActivity(closeIntent);
            }
        });
    }
}