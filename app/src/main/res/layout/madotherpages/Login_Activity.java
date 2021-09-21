package com.example.madotherpages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madotherpages.database.DBHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class Login_Activity extends AppCompatActivity {

    private static final String PASSWORD_PATTERN =
            "^(?=.*?[A-Z])(?=(.*[a-z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W]){1,})(?!.*\\s).{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    TextInputLayout LoginInputEmail;
    TextInputLayout LoginInputPass;
    TextView txt_register;
    Button loginBtn;
    DBHelper myDB;

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

                boolean checkLogin = myDB.login(email,password);
                if(checkLogin){
                    Toast.makeText(getApplicationContext(), "Login Successful.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login_Activity.this, HomePage.class));
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid Credentials.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this, Register_Activity.class));
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