package com.example.blogosphere;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blogosphere.database.ArticleModal;
import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.FollowModel;
import com.example.blogosphere.database.UserModel;

import java.util.List;

public class OtherProfileView extends AppCompatActivity {

    private ListView view;
    private ImageView ProfileImage;
    private TextView UserName;
    private TextView UserBio;
    private TextView Heading;
    private TextView Followers;
    private Button Follow;
    private Context context;
    private DBHelper myDB;
    private UserModel userModel;
    private FollowModel followModel;
    private List<ArticleModal> articles;
    int userID;
    int authorID;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(OtherProfileView.this, Home.class);
        i.putExtra("UserID", userID);
        startActivity(i);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile_view);

        view = (ListView) findViewById(R.id.postlistviewpost);
        ProfileImage = findViewById(R.id.Update_profile_image);
        UserBio = findViewById(R.id.user_bio);
        UserName = findViewById(R.id.user_name);
        Follow = findViewById(R.id.user_follow);
        Heading = findViewById(R.id.heading);

        context = this;
        myDB = new DBHelper(context);

        userID = getIntent().getIntExtra("UserID", 0);
        authorID = getIntent().getIntExtra("authorID", 0);
        userModel = myDB.getUserbyID(authorID);
        ProfileImage.setImageBitmap(userModel.getImage());
        UserBio.setText(userModel.getAbout());
        UserName.setText(userModel.getName());
        Heading.setText(userModel.getName());

        followModel = new FollowModel();
        followModel = myDB.ReadIds(userID, authorID);
        int getuserid = followModel.getUserid();
        int currentuserid = followModel.getCurrentid();
        if (getuserid == 0 && currentuserid == 0) {
            Follow.setText("Follow");
        } else {
            Follow.setText("Unfollow");
        }

        Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Follow.getText().toString().equals("Follow")) {
                    myDB.InsertFollwingID(userID, authorID);
                    Follow.setText("Unfollow");
                } else if (Follow.getText().toString().equals("Unfollow")) {
                    myDB.UnFollow(userID, authorID);
                    Follow.setText("Follow");
                }
            }
        });

        articles = myDB.getCurrentUserAllArticles(Integer.toString(authorID));
        OtherProfileAdapter apuser = new OtherProfileAdapter(context, R.layout.raw, articles, myDB);
        view.setAdapter(apuser);
    }
}


class OtherProfileAdapter extends ArrayAdapter<ArticleModal> {

    private Context context;
    private int resource;
    List<ArticleModal> articles;
    DBHelper myDB;

    public OtherProfileAdapter(Context context, int resource, List<ArticleModal> articles, DBHelper myDB) {
        super(context, resource, articles);
        this.context = context;
        this.resource = resource;
        this.articles = articles;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View row = layoutInflater.inflate(resource, parent, false);

        ImageView imageView = row.findViewById(R.id.authorImage);
        ImageView articleImageView = row.findViewById(R.id.articleImage);
        TextView textname = row.findViewById(R.id.username);
        TextView post = row.findViewById(R.id.post);

        ArticleModal article = articles.get(position);
        imageView.setImageBitmap(myDB.getUserImageById(article.getWriter_id()));
        articleImageView.setImageBitmap(article.getImage());
        textname.setText(myDB.getUserNameById(article.getWriter_id()));
        post.setText(article.getTitle());

        return row;

    }
}