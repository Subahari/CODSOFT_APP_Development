package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Button buttonGetStarted = findViewById(R.id.buttonGetStarted);
        buttonGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the main to-do list activity or any other desired activity
                openMainActivity();
            }
        });
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: Finish the landing page activity to prevent going back to it
    }
}

