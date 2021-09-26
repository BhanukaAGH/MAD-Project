package com.example.madproject2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  private  TextView  textview;
  Context context;
    private  DbHandler dbhandle;
    private UserModel modeler;
    private ImageView  profile ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview =(TextView)findViewById(R.id.editpro);
        context= this;
        dbhandle = new DbHandler(context);
        profile =findViewById(R.id.Update_profile_image);
        modeler = new UserModel();
        modeler = dbhandle.getonerecord(1);
        profile.setImageBitmap(modeler.getImage());


        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,Edit_section.class);

                startActivity(intent);

            }
        });



    }
}