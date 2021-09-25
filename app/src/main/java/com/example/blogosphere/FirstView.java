package com.example.blogosphere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstView extends AppCompatActivity {

    Button startReadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_view);

        startReadingView = findViewById(R.id.btnStartReading);

        startReadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstView.this,Login_Activity.class));
                overridePendingTransition(0,0);
            }
        });
    }
}