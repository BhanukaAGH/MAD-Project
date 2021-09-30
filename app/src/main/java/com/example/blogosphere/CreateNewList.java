package com.example.blogosphere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.ListModal;
import com.example.blogosphere.database.UserModel;

import java.util.Date;

public class CreateNewList extends AppCompatActivity {

    private EditText ET_EnterTopic, ET_EnterDiscription;
    private Button btn_create;

    private DBHelper myDB;
    private Context context;
    UserModel user;

    Date date;

    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_list);

        ET_EnterTopic = findViewById(R.id.ET_EnterEditTopic);
        ET_EnterDiscription = findViewById(R.id.ET_EnterDiscription);
        btn_create = findViewById(R.id.btn_create);

        context = this;
        myDB = new DBHelper(context);

        // Get login user object
        userID = getIntent().getIntExtra("UserID",0);
        user = myDB.getUserbyID(userID);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String topic = ET_EnterTopic.getText().toString();
                String description = ET_EnterDiscription.getText().toString();
                int userID = 1;

                date = new Date();
                int story_Count = 1;

                ListModal listModel = new ListModal(userID, story_Count, topic, description, String.format("%tB %<te, %<tY", date));

                myDB.addToTheList(listModel);
                Toast.makeText(context, "You have successfully created a list", Toast.LENGTH_SHORT).show();
                Intent intentbookmark = new Intent(context, Bookmarks.class);
                intentbookmark.putExtra("UserID", userID);
                startActivity(intentbookmark);
            }
        });
    }
}