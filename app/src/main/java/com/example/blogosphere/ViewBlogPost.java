package com.example.blogosphere;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blogosphere.database.ArticleModal;
import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.UserModel;

public class ViewBlogPost extends AppCompatActivity {

    private ImageView authorImage;
    private TextView authorView;
    private TextView dateView;
    private TextView articleTitleView;
    private TextView commentCountView;
    private WebView contentView;
    private ImageView commentView;
    private ImageView favoriteView;
    private ImageView shareView;
    private ImageView addListView;

    ArticleModal article;
    UserModel user;
    DBHelper myDB;
    String articleID;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_blog_post);

        myDB = new DBHelper(this);

        // Get login user object
//        if (user == null) {
//            Intent i = getIntent();
//            user = (UserModel) i.getSerializableExtra("UserObject");
//        }

        userID = getIntent().getIntExtra("UserID",0);
        user = myDB.getUserbyID(userID);

        Intent i = getIntent();
        articleID = i.getStringExtra("StoryID");
        article = myDB.getSingleArticle(articleID);

        authorImage = findViewById(R.id.authorImage);
        authorView = findViewById(R.id.txtAuthorName);
        dateView = findViewById(R.id.txtArticleDate);
        contentView = findViewById(R.id.contentWebView);
        articleTitleView = findViewById(R.id.txtArticleTitle);
        commentView = findViewById(R.id.commentImage);
        favoriteView = findViewById(R.id.favoriteImage);
        shareView = findViewById(R.id.shareImage);
        addListView = findViewById(R.id.listImage);
        commentCountView = findViewById(R.id.tv_CommnetCount);

        // set article values
        contentView.loadDataWithBaseURL(null, article.getContent(), "text/html", "utf-8", null);
        articleTitleView.setText(article.getTitle());
        dateView.setText(article.getDate());

        int CommentCount = myDB.countComments();
        commentCountView.setText(Integer.toString(CommentCount));

        commentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewCommentIntent = new Intent(ViewBlogPost.this, ViewComment.class);
                viewCommentIntent.putExtra("UserObject", user);
                viewCommentIntent.putExtra("StoryID", articleID);
                startActivity(viewCommentIntent);
            }
        });



    }
}