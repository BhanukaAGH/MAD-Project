package com.example.blogosphere;

import android.app.Activity;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.UserModel;

import java.io.IOException;

public class Edit_section extends AppCompatActivity {

    private Context context;
    private EditText name;
    private EditText email;
    private EditText aboutuser;
    private Button btnUpdateView;
    private Button choosebtn;
    private Button save;
    private Button deleteaccount;
    private ImageView imgeview;
    private DBHelper myDB;
    private Bitmap selectimage;
    UserModel user;

    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_section);

        context = this;
        myDB = new DBHelper(context);

        userID = getIntent().getIntExtra("UserID",0);
        user = myDB.getUserbyID(userID);

        name = findViewById(R.id.editname);
        email = findViewById(R.id.editemail);
        choosebtn = findViewById(R.id.user_name);
        save = findViewById(R.id.savebtn);
        aboutuser = findViewById(R.id.editabout);
        imgeview = findViewById(R.id.Update_profile_image);
        btnUpdateView = findViewById(R.id.upate);
        deleteaccount = findViewById(R.id.delete);

        if(user.getImage() != null ){
            imgeview.setImageBitmap(user.getImage());
        }
        name.setText(user.getName());
        email.setText(user.getEmail());
        aboutuser.setText(user.getAbout());

        String checkname = name.getText().toString();
        String checkemail = email.getText().toString();


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
                String About = aboutuser.getText().toString();
                user.setName(name.getText().toString());
                user.setImage(selectimage);
                user.setAbout(About);
                int nu = myDB.imageinsert(userID,name.getText().toString(), selectimage,aboutuser.getText().toString());
                Intent reM = new Intent(Edit_section.this,Edit_section.class);
                reM.putExtra("UserID", userID);
                startActivity(reM);
                overridePendingTransition(0,0);

            }
        });

        btnUpdateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(context, UpdateUser.class);
                homeIntent.putExtra("UserID", userID);
                startActivity(homeIntent);
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
                        int state = myDB.deleteaccount(user.getId());
                        if (!(state == 0)) {
                            Toast.makeText(context, "Succufully Deleted the Account", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Cannot Delete Your Account", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent noIntent = new Intent(context, Edit_section.class);
                        noIntent.putExtra("UserID", userID);
                        startActivity(noIntent);
                    }
                });
                builder.show();
            }
        });

        if (checkemail == "") {
            email.setFocusableInTouchMode(true);
        } else if (!(checkemail == "")) {
            email.setFocusable(false);
        }
    }

    private ActivityResultLauncher<Intent> gallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri imageuri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                            imgeview.setImageBitmap(bitmap);
                            selectimage = bitmap;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
    );
}