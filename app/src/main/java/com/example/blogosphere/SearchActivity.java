package com.example.blogosphere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchActivity extends Activity {

    ListView searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String[] searchTag = {"Javascript","Programming","Android Dev","Software Enginnering","Machine Learning","Technology","UX","iOS Dev","Design","Science","Business","Psychology","Education","Artificial Inteligence"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.search_one_tagname,R.id.searchTagtxt,searchTag);

        searchList = findViewById(R.id.seachTagList);
        searchList.setAdapter(adapter);
    }

//    Back to home
    public void backToHome(View view) {
        startActivity(new Intent(getApplicationContext(),Home.class));
        overridePendingTransition(0, 0);
    }
}