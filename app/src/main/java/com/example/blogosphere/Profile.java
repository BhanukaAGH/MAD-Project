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
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blogosphere.database.ArticleModal;
import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class Profile extends AppCompatActivity {

    ListView view;
    TextView editbtnView;
    DBHelper myDB;
    UserModel user;
    ArticleModal article;
    TextView Followig;
    ImageView UserProfileImage;
    TextView UserProfileName;
    ImageView VerticalIcon;
    private List<ArticleModal> articles;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Followig = findViewById(R.id.folowing);
        myDB = new DBHelper(this);

        userID = getIntent().getIntExtra("UserID",0);
        user = myDB.getUserbyID(userID);

        // get Number of following
        int count= myDB.CountRows(userID);
        Followig.setText(count +" Following");

        VerticalIcon = findViewById(R.id.logouticon);
        VerticalIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu =  new PopupMenu(getApplicationContext(),VerticalIcon);
                popupMenu.getMenuInflater().inflate(R.menu.logout,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.log:
                                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                                builder.setTitle("Do You Want To Log out From the Account");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent noIntent = new Intent(Profile.this, FirstView.class);
                                        startActivity(noIntent);
                                        overridePendingTransition(0,0);
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent noIntent = new Intent(Profile.this, Profile.class);
                                        noIntent.putExtra("UserID", userID);
                                        startActivity(noIntent);
                                        overridePendingTransition(0,0);
                                    }
                                });
                                builder.show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });

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
                        homeIntent.putExtra("UserID", userID);
                        startActivity(homeIntent);
                        overridePendingTransition(0, 0);
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
                        return true;
                }
                return false;
            }
        });


//        Hasith Code
        view = (ListView) findViewById(R.id.listviewpost);
        editbtnView = findViewById(R.id.edit);
        UserProfileImage = findViewById(R.id.profile_image);
        UserProfileName = findViewById(R.id.user_name);

        if(myDB.getUserImageById(userID) != null){
            UserProfileImage.setImageBitmap(myDB.getUserImageById(userID));
        }

        UserProfileName.setText(myDB.getUserNameById(userID));

        editbtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(Profile.this,Edit_section.class);
                editIntent.putExtra("UserID", userID);
                startActivity(editIntent);
                overridePendingTransition(0, 0);
            }
        });



        articles = myDB.getCurrentUserAllArticles(Integer.toString(userID));
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
                                extras.putInt("UserID", userID);
                                extras.putString("StoryID",Integer.toString(article.getId()));
                                editArticle.putExtras(extras);
                                startActivity(editArticle);
                                overridePendingTransition(0, 0);
                            }
                        })
                        .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                articleDeleteConfirm();
                            }
                        })
                        .show();
            }
        });

    }

    public void articleDeleteConfirm(){
        new MaterialAlertDialogBuilder(Profile.this)
                .setTitle("Are you sure you want to delete this article ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDB.deleteArticle(article.getId());
                        Intent dintent = new Intent(Profile.this, Profile.class);
                        dintent.putExtra("UserID", userID);
                        startActivity(dintent);
                        overridePendingTransition(0, 0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent dintent = new Intent(Profile.this, Profile.class);
                        dintent.putExtra("UserID", userID);
                        startActivity(dintent);
                        overridePendingTransition(0, 0);
                    }
                })
                .show();
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
        imageView.setImageBitmap(myDB.getUserImageById(article.getWriter_id()));
        articleImageView.setImageBitmap(article.getImage());
        textname.setText(myDB.getUserNameById(article.getWriter_id()));
        post.setText(article.getTitle());

        return row;

    }
}