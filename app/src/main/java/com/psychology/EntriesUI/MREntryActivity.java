package com.psychology.EntriesUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.psychology.R;

import java.util.Calendar;
import java.util.HashMap;

public class MREntryActivity extends AppCompatActivity {

    Button eventDate, eventTime;

    int tHour, tMinute;

    String selectedEventTime = "", selectedEventDate = "";

    EditText event;

    Button submit, skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrentry);

        eventDate = findViewById(R.id.eventDate);
        eventTime = findViewById(R.id.eventTime);
        event = findViewById(R.id.event_edit);
        submit = findViewById(R.id.submit);
        skip = findViewById(R.id.skip);

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });

        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredEvent = event.getText().toString().trim();

                if(TextUtils.isEmpty(enteredEvent)){
                    Toast.makeText(getApplicationContext(), "Please enter an event to continue", Toast.LENGTH_SHORT).show();
                }
                else if(selectedEventDate.equals("")){
                    Toast.makeText(getApplicationContext(), "Please select an event date to continue", Toast.LENGTH_SHORT).show();
                }
                else if(selectedEventTime.equals("")){
                    Toast.makeText(getApplicationContext(), "Please select an event time to continue", Toast.LENGTH_SHORT).show();
                }
                else{
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("event", enteredEvent);
                    data.put("eventDate", selectedEventDate);
                    data.put("eventTime", selectedEventTime);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("MR").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(getApplicationContext(), "MR Entry added", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), PAEntryActivity.class);
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
                Intent intent = new Intent(getApplicationContext(), PAEntryActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void handleTimeButton(){
        Calendar calendar = Calendar.getInstance();
        int Hour = calendar.get(Calendar.HOUR);
        int Minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                tHour = hourOfDay;
                tMinute = minute;

                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0,tHour,tMinute);

                String time = hourOfDay + ":" + minute;
                eventTime.setText("Event Time - " + DateFormat.format("hh:mm aa", calendar));
                selectedEventTime = String.valueOf(DateFormat.format("hh:mm aa", calendar));
            }
        },Hour,Minute,false);
        timePickerDialog.show();
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
                eventDate.setText("Event Date - " + DateFormat.format("dd/MM/yyyy", calendar));

                selectedEventDate = String.valueOf(DateFormat.format("dd/MM/yyyy", calendar));
            }
        }, year, month, date);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, 1);

        datePickerDialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());

        datePickerDialog.show();
    }
}