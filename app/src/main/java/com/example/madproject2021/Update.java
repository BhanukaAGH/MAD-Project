package com.example.madproject2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class Update extends AppCompatActivity {

    private EditText name;
    private  EditText email;
    private EditText about;
    private Button Update;
    private ImageView upateimage;
    private TextView select;
    Context context;
    private DbHandler db;
    private  UserModel uermodel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        context=this;

        db = new DbHandler(context);
        uermodel = new UserModel();
        name = findViewById(R.id.UpdateName);
        email =findViewById(R.id.UpdateEmail);
        about = findViewById(R.id.UdateAbout);
        Update = findViewById(R.id.UpadteBtn);
        upateimage =findViewById(R.id.Update_profile_image);
        select = findViewById(R.id.select_image);

        uermodel= db.getonerecord(1);
        name.setText(uermodel.getName());
        email.setText(uermodel.getEmail());
        about.setText(uermodel.getAbout());
        upateimage.setImageBitmap(uermodel.getImage());





        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emalivalidate =  email.getText().toString();
                if(isValidEmailAddress(emalivalidate)){
                    uermodel.setId(1);
                    uermodel.setName(name.getText().toString());
                    uermodel.setEmail(email.getText().toString());
                    uermodel.setAbout(about.getText().toString());
                    int row= db.Upatesave(uermodel);

                    if(!(row==0)){
                        Toast.makeText(context,"Succufully Upadated the Rows",Toast.LENGTH_SHORT).show();
                    }

                    else {
                        Toast.makeText(context,"Succufully Upadated the Rows",Toast.LENGTH_SHORT).show();
                    }

                    startActivity(new Intent(context, Edit_section.class));
                }

                else {
                    email.setText("Invalid Foramat");
                    email.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);


                    }

            }
        });

    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}