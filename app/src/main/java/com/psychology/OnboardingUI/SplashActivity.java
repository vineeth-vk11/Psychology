package com.psychology.OnboardingUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.psychology.LoginUI.LoginDetailsCollectionActivity;
import com.psychology.LoginUI.LoginMainActivity;
import com.psychology.MainActivity;
import com.psychology.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        Boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {

                if(isFirstTime){
                    Intent intent = new Intent(getApplicationContext(), InitialMessageActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    redirect();
                }
            }
        }, secondsDelayed * 2000);

    }

    private void redirect(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if(documentSnapshot.exists()){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), LoginDetailsCollectionActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
        else {
            Intent intent = new Intent(getApplicationContext(), LoginMainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}