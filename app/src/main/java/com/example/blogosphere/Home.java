package com.example.blogosphere;

import android.content.Context;
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
import com.example.blogosphere.database.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class Home extends AppCompatActivity {

    private RecyclerView followingList;
    private ListView articleList;
    DBHelper myDB;
    UserModel user;

    private List<ArticleModal> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        myDB = new DBHelper(this);

        if (user == null) {
            Intent i = getIntent();
            user = (UserModel) i.getSerializableExtra("UserObject");
            myDB.getLoginUserID(user);
        }

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
                        Intent profileIntent = new Intent(getApplicationContext(), Profile.class);
                        profileIntent.putExtra("UserObject", user);
                        startActivity(profileIntent);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

//       Home Content
        followingList = findViewById(R.id.following_list);

        ArrayList<Followers> followers = new ArrayList<>();

        followers.add(new Followers("Angelina", R.drawable.a));
        followers.add(new Followers("Morgan", R.drawable.b));
        followers.add(new Followers("Elina", R.drawable.c));
        followers.add(new Followers("Taylor", R.drawable.d));


        FollowingListRecViewAdapter adapter = new FollowingListRecViewAdapter();
        adapter.setFollowers(followers);

        followingList.setAdapter(adapter);
        followingList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        articles = myDB.getAllArticles();

        CustomAdapter adapterArticle = new CustomAdapter(this,R.layout.single_article_row,articles,myDB);
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
        searchIntent.putExtra("UserObject",user);
        startActivity(searchIntent);
        overridePendingTransition(0, 0);
    }
}

// new array adapter for list view
class CustomAdapter extends ArrayAdapter<ArticleModal> {

    private Context context;
    private int resource;
    List<ArticleModal> articles;
    DBHelper myDB;

    CustomAdapter(Context context,int resource, List<ArticleModal> articles,DBHelper myDB ) {
        super(context,resource,articles);
        this.context = context;
        this.resource = resource;
        this.articles = articles;
        this.myDB = myDB;
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
        authorImgView.setImageBitmap(article.getImage());
        articleImgView.setImageBitmap(article.getImage());
        dateView.setText(article.getDate());

        bookmarkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.normal(context,"Bookmark Icon Clicked" + position).show();
//                bookmarkIcon.setImageResource(R.drawable.ic_baseline_bookmark_24);
            }
        });

        return row;
    }
}