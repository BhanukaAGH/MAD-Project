package com.example.blogosphere;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blogosphere.database.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Write_Story extends AppCompatActivity {

    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_story);

//        Get login user object
        if (user == null) {
            Intent i = getIntent();
            user = (UserModel) i.getSerializableExtra("UserObject");
        }

//       Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

//        Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.writepost);

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

    }

    public void writeArticle(View view) {
        Intent writeArticle = new Intent(getApplicationContext(), Create_Article.class);
        writeArticle.putExtra("UserObject", user);
        startActivity(writeArticle);
        overridePendingTransition(0, 0);
    }
}