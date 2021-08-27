package com.psychology.OnboardingUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.psychology.R;

public class InitialMessageActivity extends AppCompatActivity {

    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_message);

        message = findViewById(R.id.message);

        message.setText(getResources().getString(R.string.app_name) + " is your own self tracking app for superior mental health at workplace.");

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(getApplicationContext(), InitialVideoActivity.class);
                startActivity(intent);
                finish();
            }
        }, secondsDelayed * 2000);
    }
}