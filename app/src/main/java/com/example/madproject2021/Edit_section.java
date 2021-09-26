package com.example.madproject2021;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class Edit_section extends AppCompatActivity {

    private Context context;

    private  EditText name;
    private  EditText email;
    private  EditText aboutuser;
    private  Button upatedata;
    private Button choosebtn;
    private Button save;
    private Button deleteaccount;
    private ImageView imgeview;
    private DbHandler db;
    private Bitmap selectimage;
    private UserModel userModel;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_section);
        context=this;

        name= findViewById(R.id.editname);
        email= findViewById(R.id.editemail);
        choosebtn =findViewById(R.id.user_name);
        save = findViewById(R.id.savebtn);
        aboutuser = findViewById(R.id.editabout);
        db = new DbHandler(context);
        userModel = new UserModel();
        imgeview = findViewById(R.id.Update_profile_image);
        upatedata = findViewById(R.id.upate);
        deleteaccount =findViewById(R.id.delete);

        userModel = db.getonerecord(1);
        imgeview.setImageBitmap(userModel.getImage());
        name.setText(userModel.getName());
        email.setText(userModel.getEmail());
        aboutuser.setText(userModel.getAbout());
        String checkname =  name.getText().toString();
        String checkemail =  email.getText().toString();

        choosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                gallery.launch(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //UserModel userModel = new UserModel();

                String About = aboutuser.getText().toString();
                userModel.setId(1);
                userModel.setImage(selectimage);
                userModel.setAbout(About);
                db.imageinsert( userModel);
                userModel= db.getonerecord(1);
                imgeview.setImageBitmap(userModel.getImage());
                aboutuser.setText(userModel.getAbout());


            }
        });

        upatedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,Update.class));
            }
        });

        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Do You Want To Delete This Account");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      int state =  db.deleteaccount(1);
                      if(!(state==0)){
                          Toast.makeText(context,"Succufully Deleted the Account",Toast.LENGTH_SHORT).show();
                      }
                      else {
                          Toast.makeText(context,"Cannot Delete Your Account",Toast.LENGTH_SHORT).show();
                      }


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(context,Edit_section.class) );

                    }
                });

                builder.show();
            }
        });



        if(checkemail==""){
            email.setFocusableInTouchMode(true);
        }

        else if(!(checkemail=="")){
            email.setFocusable(false);
        }

        if(checkname==""){
            name.setFocusableInTouchMode(true);
        }

        else if(!(checkemail=="")){
            name.setFocusable(false);
        }


    }

    private ActivityResultLauncher<Intent> gallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri imageuri= data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                            imgeview.setImageBitmap(bitmap);
                            selectimage=bitmap;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }
            }
    );



}