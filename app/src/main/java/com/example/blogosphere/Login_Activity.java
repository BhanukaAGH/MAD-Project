package com.example.blogosphere;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.UserModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class Login_Activity extends AppCompatActivity {

    private static final String PASSWORD_PATTERN =
            "^(?=.*?[A-Z])(?=(.*[a-z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W]){1,})(?!.*\\s).{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    TextInputLayout LoginInputEmail;
    TextInputLayout LoginInputPass;
    TextView txt_register;
    Button loginBtn;
    DBHelper myDB;
    int userID;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Login_Activity.this,FirstView.class));
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginInputEmail = findViewById(R.id.inLayLogEmail);
        LoginInputPass = findViewById(R.id.inLayLogPass);
        txt_register = findViewById(R.id.tvRegisterHere);
        loginBtn = findViewById(R.id.btnLogin);

        myDB = new DBHelper(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail()  | !validatePassword()) {
                    return;
                }

                String email = LoginInputEmail.getEditText().getText().toString();
                String password = LoginInputPass.getEditText().getText().toString();

                UserModel user = new UserModel();
                user.setEmail(email);
                user.setPassword(password);

                boolean checkLogin = myDB.login(user);

                if(checkLogin){
                    userID = myDB.getLoginUserID(email);
                    Toasty.success(getApplicationContext(), "Login Successful.", Toast.LENGTH_SHORT,false).show();
                    Intent loginIntent = new Intent(Login_Activity.this, Home.class);
                    loginIntent.putExtra("UserID", userID);
                    startActivity(loginIntent);
                    overridePendingTransition(0, 0);
                }else{
                    Toasty.error(getApplicationContext(), "Invalid Credentials.", Toast.LENGTH_SHORT,false).show();
                }
            }
        });

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this, Register_Activity.class));
                overridePendingTransition(0, 0);
            }
        });

    }

    private boolean validateEmail(){
        String emailInput = LoginInputEmail.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()){
            LoginInputEmail.setError("Please enter valid email");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            LoginInputEmail.setError("Please enter a valid email address");
            return false;
        }else{
            LoginInputEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String passwordInput = LoginInputPass.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            LoginInputPass.setError("Please enter valid password");
            return false;
        }else if(!pattern.matcher(passwordInput).matches()){
            LoginInputPass.setError("password must contain at least eight characters, at least one number and both lower and uppercase letters and special characters");
            return false;
        } else {
            LoginInputPass.setError(null);
            return true;
        }
    }
}