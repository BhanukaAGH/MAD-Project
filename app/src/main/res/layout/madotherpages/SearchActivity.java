package com.example.madotherpages;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

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
}