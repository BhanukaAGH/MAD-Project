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

public class Register_Activity extends AppCompatActivity {

    private static final String PASSWORD_PATTERN =
            "^(?=.*?[A-Z])(?=(.*[a-z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W]){1,})(?!.*\\s).{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    TextInputLayout RegisterInputEmail;
    TextInputLayout RegisterInputPass;
    TextInputLayout RegisterInputConfirmPass;
    TextView txt_login;
    Button registerBtn;
    DBHelper myDB;

    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterInputEmail = findViewById(R.id.inLayLogEmail);
        RegisterInputPass = findViewById(R.id.inLayLogPass);
        RegisterInputConfirmPass = findViewById(R.id.inLayLogConfirmPass);
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
                String Confirm_password = RegisterInputConfirmPass.getEditText().getText().toString();

                if (password.equals(Confirm_password)) {
                    UserModel user = new UserModel();
                    user.setEmail(email);
                    user.setPassword(password);

                    boolean checkUserAlreadyExists = myDB.checkUserExists(user);
                    if (checkUserAlreadyExists) {
                        Toasty.normal(getApplicationContext(), "User already Exists. Please Sign In").show();

                    } else {
                        boolean registerSuccess = myDB.userRegister(user);
                        if (registerSuccess) {
                            userID = myDB.getLoginUserID(email);
                            Toasty.success(getApplicationContext(), "Registration Successful.", Toast.LENGTH_SHORT, false).show();
                            Intent registerIntent = new Intent(Register_Activity.this, Home.class);
                            registerIntent.putExtra("UserID", userID);
                            startActivity(registerIntent);
                        } else {
                            Toasty.error(getApplicationContext(), "Registration Failed.", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                } else {
                    Toasty.error(getApplicationContext(), "Password is not matched.", Toast.LENGTH_SHORT, false).show();
                }
            }
        });

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register_Activity.this, com.example.blogosphere.Login_Activity.class));
                overridePendingTransition(0, 0);
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