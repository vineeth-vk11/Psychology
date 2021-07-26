package com.psychology.EntriesUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.psychology.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ERTEntryActivity extends AppCompatActivity {

    Button verySad, sad, normal, happy, veryHappy;
    String emotionSelected = "";

    Button submit, skip;

    int existingVerySad = 0;
    int existingSad = 0;
    int existingNormal = 0;
    int existingHappy = 0;
    int existingVeryHappy = 0;

    Date c = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    String formattedDate = df.format(c);

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ertentry);

        verySad = findViewById(R.id.verySad);
        sad = findViewById(R.id.sad);
        normal = findViewById(R.id.normal);
        happy = findViewById(R.id.happy);
        veryHappy = findViewById(R.id.veryHappy);
        submit = findViewById(R.id.submit);
        skip = findViewById(R.id.skip);

        verySad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emotionSelected = "Very Sad";

                verySad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_verysadcolor, 0, 0, 0);
                sad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sad, 0, 0, 0);
                normal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_normal, 0, 0, 0);
                happy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_happy, 0, 0, 0);
                veryHappy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_veryhappy, 0, 0, 0);
            }
        });

        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emotionSelected = "Sad";

                verySad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_verysad, 0, 0, 0);
                sad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sadcolor, 0, 0, 0);
                normal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_normal, 0, 0, 0);
                happy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_happy, 0, 0, 0);
                veryHappy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_veryhappy, 0, 0, 0);
            }
        });

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emotionSelected = "Normal";

                verySad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_verysad, 0, 0, 0);
                sad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sad, 0, 0, 0);
                normal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_normalcolor, 0, 0, 0);
                happy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_happy, 0, 0, 0);
                veryHappy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_veryhappy, 0, 0, 0);
            }
        });

        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emotionSelected = "Happy";

                verySad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_verysad, 0, 0, 0);
                sad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sad, 0, 0, 0);
                normal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_normal, 0, 0, 0);
                happy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_happycolor, 0, 0, 0);
                veryHappy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_veryhappy, 0, 0, 0);
            }
        });

        veryHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emotionSelected = "Very Happy";

                verySad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_verysad, 0, 0, 0);
                sad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sad, 0, 0, 0);
                normal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_normal, 0, 0, 0);
                happy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_happy, 0, 0, 0);
                veryHappy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_veryhappycolor, 0, 0, 0);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(emotionSelected)){
                    Toast.makeText(getApplicationContext(), "Select an emotion to continue",Toast.LENGTH_SHORT).show();
                }
                else{
                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("ERTbyDates").document(formattedDate).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            if(documentSnapshot.exists()){

                                existingVerySad = (int)(long)documentSnapshot.get("verySad");
                                existingSad = (int)(long)documentSnapshot.get("sad");
                                existingNormal = (int)(long)documentSnapshot.get("normal");
                                existingHappy = (int)(long)documentSnapshot.get("happy");
                                existingVeryHappy = (int)(long)documentSnapshot.get("veryHappy");

                                if(emotionSelected.equals("Very Sad")){
                                    existingVerySad += 1;
                                }
                                else if(emotionSelected.equals("Sad")){
                                    existingSad += 1;
                                }
                                else if(emotionSelected.equals("Normal")){
                                    existingNormal += 1;
                                }
                                else if(emotionSelected.equals("Happy")){
                                    existingHappy += 1;
                                }
                                else if(emotionSelected.equals("Very Happy")){
                                    existingVeryHappy += 1;
                                }
                                else {
                                    existingNormal += 1;
                                }

                                saveData1();
                            }
                            else {

                                if(emotionSelected.equals("Very Sad")){
                                    existingVerySad += 1;
                                }
                                else if(emotionSelected.equals("Sad")){
                                    existingSad += 1;
                                }
                                else if(emotionSelected.equals("Normal")){
                                    existingNormal += 1;
                                }
                                else if(emotionSelected.equals("Happy")){
                                    existingHappy += 1;
                                }
                                else if(emotionSelected.equals("Very Happy")){
                                    existingVeryHappy += 1;
                                }
                                else {
                                    existingNormal += 1;
                                }

                                saveData1();
                            }
                        }
                    });


                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MREntryActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void saveData1(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("verySad", existingVerySad);
        data.put("sad", existingSad);
        data.put("normal", existingNormal);
        data.put("happy", existingHappy);
        data.put("veryHappy", existingVeryHappy);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("ERTbyDates").document(formattedDate).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                saveData();
            }
        });
    }

    private void saveData(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("emotion", emotionSelected);
        data.put("date", formattedDate);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("ERT").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(getApplicationContext(), "ERT Entry Added", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MREntryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}