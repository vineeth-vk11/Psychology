package com.psychology.EntriesUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.psychology.MainActivity;
import com.psychology.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class PAEntryActivity extends AppCompatActivity {

    AutoCompleteTextView affirmation;

    Button selectDate, submit, skip;

    String selectedAffirmationDate = "";

    ArrayList<String> Affirmations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paentry);

        Affirmations.add("I have limitless potential");
        Affirmations.add("I’m exactly where I need to be. I see myself in my dream job");
        Affirmations.add("I have all the best skills and knowledge to deliver for this job");
        Affirmations.add("I have exceptional talents and capabilities and I will nail it");
        Affirmations.add("I am courageous enough to face and conquer my fears");
        Affirmations.add("I am confident in my self-worth");
        Affirmations.add("I am the best at what I d and I’m going to create exceptional results for my organization");
        Affirmations.add("This is the opportunity I need to show the world that I’m the best");
        Affirmations.add("I am living my life to the fullest");
        Affirmations.add("I am more effective when I focus and take care of myself");
        Affirmations.add("This is my game and I am here to show them how to win");
        Affirmations.add("I do my work for a purpose. I am going to shine at it");

        affirmation = findViewById(R.id.affirmation_edit);
        submit = findViewById(R.id.submit);
        skip = findViewById(R.id.skip);
        selectDate = findViewById(R.id.affirmationDate);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.dropdown_menu_popup_item,
                Affirmations
        );

        affirmation.setAdapter(adapter);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredAffirmation = affirmation.getText().toString().trim();

                if(TextUtils.isEmpty(enteredAffirmation)){
                    Toast.makeText(getApplicationContext(), "Enter an affirmation to continue", Toast.LENGTH_SHORT).show();
                }
                else if(selectedAffirmationDate.equals("")){
                    Toast.makeText(getApplicationContext(), "Select an affirmation date to continue", Toast.LENGTH_SHORT).show();
                }
                else{

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("affirmation", enteredAffirmation);
                    data.put("affirmationDate", selectedAffirmationDate);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("PA").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {

                            Toast.makeText(getApplicationContext(), "PA Entry added successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void handleDateButton(){

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);
                selectDate.setText("Affimation Date - " + DateFormat.format("dd/MM/yyyy", calendar));

                selectedAffirmationDate = String.valueOf(DateFormat.format("dd/MM/yyyy", calendar));
            }
        }, year, month, date);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, 1);

        datePickerDialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());

        datePickerDialog.show();
    }
}