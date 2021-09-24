package com.example.blogosphere;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blogosphere.database.ArticleModal;
import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class Profile extends AppCompatActivity {

    ListView view;
    DBHelper myDB;
    UserModel user;
    ArticleModal article;

    private List<ArticleModal> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        myDB = new DBHelper(this);

//        Get login user object
        if (user == null) {
            Intent i = getIntent();
            user = (UserModel) i.getSerializableExtra("UserObject");
        }

//        Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

//        Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.profile);

//        Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent homeIntent = new Intent(getApplicationContext(), Home.class);
                        homeIntent.putExtra("UserObject", user);
                        startActivity(homeIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.bookmark:
                        Intent bookmarkIntent = new Intent(getApplicationContext(), Bookmarks.class);
                        bookmarkIntent.putExtra("UserObject", user);
                        startActivity(bookmarkIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.writepost:
                        Intent writepostIntent = new Intent(getApplicationContext(), Write_Story.class);
                        writepostIntent.putExtra("UserObject", user);
                        startActivity(writepostIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });


//        Hasith Code
        view = (ListView) findViewById(R.id.listviewpost);

        articles = myDB.getCurrentUserAllArticles(Integer.toString(user.getId()));
        UserAdapter apuser = new UserAdapter(this, R.layout.raw, articles, myDB);
        view.setAdapter(apuser);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                article = articles.get(position);

                new MaterialAlertDialogBuilder(Profile.this)
                        .setTitle("Your Story")
                        .setMessage(article.getTitle())
                        .setNeutralButton("VIEW", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent viewArticleIntent = new Intent(Profile.this,ViewBlogPost.class);
                                viewArticleIntent.putExtra("StoryID",Integer.toString(article.getId()));
                                startActivity(viewArticleIntent);
                                overridePendingTransition(0, 0);
                            }
                        })
                        .setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent editArticle = new Intent(Profile.this,Edit_Article.class);
                                Bundle extras = new Bundle();
                                extras.putSerializable("UserObject", user);
                                extras.putString("StoryID",Integer.toString(article.getId()));
                                editArticle.putExtras(extras);
                                startActivity(editArticle);
                                overridePendingTransition(0, 0);
                            }
                        })
                        .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myDB.deleteArticle(article.getId());
                                Intent dintent = new Intent(Profile.this, Profile.class);
                                dintent.putExtra("UserObject", user);
                                startActivity(dintent);
                                overridePendingTransition(0, 0);
                            }
                        })
                        .show();
            }
        });

    }

}

class UserAdapter extends ArrayAdapter<ArticleModal> {

    private Context context;
    private int resource;
    List<ArticleModal> articles;
    DBHelper myDB;

    public UserAdapter(Context context, int resource, List<ArticleModal> articles, DBHelper myDB) {
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
        imageView.setImageBitmap(article.getImage());
        articleImageView.setImageBitmap(article.getImage());
//        textname.setText(myDB.getUserNameById(article.getWriter_id()));
        textname.setText("Mark Dale");
        post.setText(article.getTitle());

        return row;

    }
}