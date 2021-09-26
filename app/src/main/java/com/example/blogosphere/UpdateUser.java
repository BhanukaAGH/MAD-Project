package com.example.blogosphere;

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

import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.UserModel;

public class UpdateUser extends AppCompatActivity {

    private EditText name;
    private  EditText email;
    private EditText about;
    private Button Update;
    private ImageView upateimage;
    private TextView select;
    Context context;
    DBHelper myDB;
    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        // Get login user object
//        if (user == null) {
//            Intent i = getIntent();
//            user = (UserModel) i.getSerializableExtra("UserObject");
//        }

        String userID = getIntent().getStringExtra("UserID");

//        System.out.println(user.getId());
        System.out.println(userID);

        context=this;
        myDB = new DBHelper(context);
        user = myDB.getonerecord(Integer.parseInt(userID));
        name = findViewById(R.id.UpdateName);
        email =findViewById(R.id.UpdateEmail);
        about = findViewById(R.id.UdateAbout);
        Update = findViewById(R.id.UpadteBtn);
        upateimage =findViewById(R.id.Update_profile_image);
        select = findViewById(R.id.select_image);
        name.setText(user.getName());
        email.setText(user.getEmail());
        about.setText(user.getAbout());
        upateimage.setImageBitmap(user.getImage());


        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emalivalidate =  email.getText().toString();
                if(isValidEmailAddress(emalivalidate)){
                    user.setName(name.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setAbout(about.getText().toString());
                    int row= myDB.Upatesave(user);
                    if(!(row==0)){
                        Toast.makeText(context,"Succufully Upadated the Rows",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context,"Succufully Upadated the Rows",Toast.LENGTH_SHORT).show();
                    }
                    Intent reIntent = new Intent(context, Edit_section.class);
                    reIntent.putExtra("UserID", user.getId());
                    startActivity(reIntent);
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
