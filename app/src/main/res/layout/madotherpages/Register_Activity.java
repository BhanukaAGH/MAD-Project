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

public class Register_Activity extends AppCompatActivity {

    private static final String PASSWORD_PATTERN =
            "^(?=.*?[A-Z])(?=(.*[a-z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W]){1,})(?!.*\\s).{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    TextInputLayout RegisterInputEmail;
    TextInputLayout RegisterInputPass;
    TextView txt_login;
    Button registerBtn;
    DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterInputEmail = findViewById(R.id.inLayLogEmail);
        RegisterInputPass = findViewById(R.id.inLayLogPass);
        txt_login = findViewById(R.id.tvLoginHere);
        registerBtn = findViewById(R.id.btnRegister);

        myDB = new DBHelper(this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail() | !validatePassword()) {
                    return;
                }

                String email = RegisterInputEmail.getEditText().getText().toString();
                String password = RegisterInputPass.getEditText().getText().toString();

                boolean checkUserAlreadyExists = myDB.checkUserExists(email);
                if (checkUserAlreadyExists) {
                    Toast.makeText(getApplicationContext(), "User already Exists. \n Please Sign In", Toast.LENGTH_SHORT).show();

                } else {
                    boolean registerSuccess = myDB.userRegister(email, password);
                    if (registerSuccess) {
                        Toast.makeText(getApplicationContext(), "Registration Successful.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register_Activity.this, HomePage.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Registration Failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register_Activity.this, Login_Activity.class));
            }
        });

    }

    private boolean validateEmail() {
        String emailInput = RegisterInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            RegisterInputEmail.setError("Please enter valid email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            RegisterInputEmail.setError("Please enter a valid email address");
            return false;
        } else {
            RegisterInputEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = RegisterInputPass.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            RegisterInputPass.setError("Please enter valid password");
            return false;
        } else if (!pattern.matcher(passwordInput).matches()) {
            RegisterInputPass.setError("password must contain at least eight characters, at least one number and both lower and uppercase letters and special characters");
            return false;
        } else {
            RegisterInputPass.setError(null);
            return true;
        }
    }
}