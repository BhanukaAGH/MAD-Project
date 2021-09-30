package com.example.blogosphere;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogosphere.database.ArticleModal;
import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.FollowModel;
import com.example.blogosphere.database.ListModal;
import com.example.blogosphere.database.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class Home extends AppCompatActivity {

    private RecyclerView followingList;
    private ListView articleList;
    DBHelper myDB;
    UserModel user;
    int userID;
    private List<ArticleModal> articles;
    private List<FollowModel> followers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        myDB = new DBHelper(this);

        userID = getIntent().getIntExtra("UserID",0);
        user = myDB.getUserbyID(userID);

//        Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

//        Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

//        Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.bookmark:
                        Intent bookmarkIntent = new Intent(getApplicationContext(), Bookmarks.class);
                        bookmarkIntent.putExtra("UserID", userID);
                        startActivity(bookmarkIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.writepost:
                        Intent writepostIntent = new Intent(getApplicationContext(), Write_Story.class);
                        writepostIntent.putExtra("UserID", userID);
                        startActivity(writepostIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        Intent profileIntent = new Intent(getApplicationContext(), Profile.class);
                        profileIntent.putExtra("UserID", userID);
                        startActivity(profileIntent);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

//       Home Content
        followingList = findViewById(R.id.following_list);
        followers = myDB.getCurrentUserFollowers(Integer.toString(userID));

        FollowingListRecViewAdapter adapter = new FollowingListRecViewAdapter(myDB);
        adapter.setFollowers(followers);

        followingList.setAdapter(adapter);
        followingList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        articles = myDB.getAllArticles();

        CustomAdapter adapterArticle = new CustomAdapter(this,R.layout.single_article_row,articles,myDB,userID);
        articleList = findViewById(R.id.articleList);
        articleList.setAdapter(adapterArticle);

        articleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArticleModal article = articles.get(position);
                Intent viewArticleIntent = new Intent(Home.this,ViewBlogPost.class);
                viewArticleIntent.putExtra("StoryID",Integer.toString(article.getId()));
                startActivity(viewArticleIntent);
                overridePendingTransition(0, 0);
            }
        });

    }

    //    Search Article
    public void searchArticle(View view) {
        Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
        searchIntent.putExtra("UserID", userID);
        startActivity(searchIntent);
        overridePendingTransition(0, 0);
    }
}

// new array adapter for list view
class CustomAdapter extends ArrayAdapter<ArticleModal> {

    private Context context;
    private int resource;
    List<ArticleModal> articles;
    List<ListModal> bookmarkList;
    DBHelper myDB;
    int userID;

    CustomAdapter(Context context,int resource, List<ArticleModal> articles,DBHelper myDB,int userID ) {
        super(context,resource,articles);
        this.context = context;
        this.resource = resource;
        this.articles = articles;
        this.myDB = myDB;
        this.userID = userID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(resource, parent, false);

        TextView authorView = row.findViewById(R.id.txtAuthor);
        TextView articlDesc = row.findViewById(R.id.txtDesc);
        ImageView authorImgView = row.findViewById(R.id.authorImg);
        ImageView articleImgView = row.findViewById(R.id.articleImg);
        TextView dateView = row.findViewById(R.id.txtDate);
        ImageView bookmarkIcon = row.findViewById(R.id.bookmarkVector);

        ArticleModal article = articles.get(position);

        authorView.setText(myDB.getUserNameById(article.getWriter_id()));
        articlDesc.setText(article.getTitle());
        authorImgView.setImageBitmap(myDB.getUserImageById(article.getWriter_id()));
        articleImgView.setImageBitmap(article.getImage());
        dateView.setText(article.getDate());

        authorImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,OtherProfileView.class);
                i.putExtra("UserID", userID);
                i.putExtra("authorID",article.getWriter_id());
                context.startActivity(i);
            }
        });

        bookmarkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkList = myDB.getAllLists();
                String []singleItem = new String[bookmarkList.size()];

                int i = 0;
                for(ListModal s: bookmarkList){
                    singleItem[i] = s.getList_Topic();
                    i++;
                }
                int checkedItem = 1;

                new MaterialAlertDialogBuilder(context)
                        .setTitle("Bookmark List")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bookmarkIcon.setImageResource(R.drawable.ic_outline_bookmark_border_24);
                            }
                        })
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                bookmarkIcon.setImageResource(R.drawable.ic_baseline_bookmark_24);
                            }
                        })
                        .setNeutralButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bookmarkIcon.setImageResource(R.drawable.ic_outline_bookmark_border_24);
                            }
                        })
                        .setSingleChoiceItems(singleItem, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println(singleItem[which]);
                            }
                        })
                        .show();
            }
        });

        return row;
    }
}