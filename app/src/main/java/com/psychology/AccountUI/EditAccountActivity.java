package com.psychology.AccountUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.psychology.LoginUI.LoginDetailsCollectionActivity;
import com.psychology.MainActivity;
import com.psychology.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class EditAccountActivity extends AppCompatActivity {

    ArrayList<String> ages = new ArrayList<>();
    EditText name, age;
    Button getStarted;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RadioGroup genders;
    RadioButton selectedGenderButton;

    String gender;

    EditText workDays;
    boolean[] checkedWorkDays;
    ArrayList<Integer> userWorkDaysSelections = new ArrayList<>();

    Button startTime, endTime;
    int tHour, tMinute;

    String workStartTime = "", workEndTime = "";

    EditText company, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        age = findViewById(R.id.age_edit);
        name = findViewById(R.id.name_edit);
        getStarted = findViewById(R.id.getStarted);
        genders = findViewById(R.id.radioGroup);
        workDays = findViewById(R.id.working_days_edit);
        endTime = findViewById(R.id.workEndTime);
        startTime = findViewById(R.id.workStartTime);
        company = findViewById(R.id.company_edit);
        role = findViewById(R.id.designation_edit);

        final String[] WORKDAYS = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        checkedWorkDays = new boolean[WORKDAYS.length];

        workDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditAccountActivity.this);
                builder.setTitle("Work Days");
                builder.setMultiChoiceItems(WORKDAYS, checkedWorkDays, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            if(! userWorkDaysSelections.contains(position)){
                                userWorkDaysSelections.add(position);
                            }
                        }
                        else if(userWorkDaysSelections.contains(position)){
                            userWorkDaysSelections.remove(userWorkDaysSelections.indexOf(position));
                        }
                    }
                });

                builder.setCancelable(false);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selections = "";
                        for(int i =0; i<userWorkDaysSelections.size();i++){
                            selections = selections + WORKDAYS[userWorkDaysSelections.get(i)];
                            if( i != userWorkDaysSelections.size() - 1){
                                selections = selections + ", ";
                            }
                        }
                        workDays.setText(selections);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        checkedWorkDays = new boolean[WORKDAYS.length];
                        userWorkDaysSelections.clear();
                        workDays.setText("");
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton("start");
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton("end");
            }
        });

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredName = name.getText().toString().trim();
                String selectedAge = age.getText().toString().trim();
                String enteredCompanyName = company.getText().toString().trim();
                String enteredWorkingRole = role.getText().toString().trim();
                String selectedWorkingDays = workDays.getText().toString().trim();

                int selectedGender = genders.getCheckedRadioButtonId();

                if(selectedGender == -1){
                    Toast.makeText(getApplicationContext(), "Please select your gender",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(enteredName)){
                    Toast.makeText(getApplicationContext(), "Please enter your name",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(selectedAge)){
                    Toast.makeText(getApplicationContext(), "Please enter your age", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(selectedAge) > 100 || Integer.parseInt(selectedAge) <18){
                    Toast.makeText(getApplicationContext(), "Please enter a age between 18 and 100", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(enteredCompanyName)){
                    Toast.makeText(getApplicationContext(), "Please enter the company name you work at", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(enteredWorkingRole)){
                    Toast.makeText(getApplicationContext(), "Please enter your designation at work", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(selectedWorkingDays)){
                    Toast.makeText(getApplicationContext(), "Please select your working days", Toast.LENGTH_SHORT).show();
                }
                else if(workStartTime.equals("")){
                    Toast.makeText(getApplicationContext(), "Please select your work starting time", Toast.LENGTH_SHORT).show();
                }
                else if(workEndTime.equals("")){
                    Toast.makeText(getApplicationContext(), "Please select your work ending time", Toast.LENGTH_SHORT).show();
                }
                else {
                    selectedGenderButton = findViewById(selectedGender);
                    if(selectedGenderButton.getText().toString().equals("Male")){
                        gender = "Male";
                    }
                    else if(selectedGenderButton.getText().toString().equals("Female")){
                        gender = "Female";
                    }

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("name",enteredName);
                    data.put("age",Integer.parseInt(selectedAge));
                    data.put("gender",gender);
                    data.put("company", enteredCompanyName);
                    data.put("designation", enteredWorkingRole);
                    data.put("workingDays", userWorkDaysSelections);
                    data.put("workingDaysText", selectedWorkingDays);
                    data.put("workStartTime", workStartTime);
                    data.put("workEndingTime", workEndTime);

                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });
                }
            }
        });

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();

                name.setText(documentSnapshot.getString("name"));
                age.setText(String.valueOf((Long) documentSnapshot.get("age")));

                String gender = documentSnapshot.getString("gender");

                if(gender.equals("Male")){
                    genders.check(R.id.group1);
                }
                else{
                    genders.check(R.id.group2);
                }

                company.setText(documentSnapshot.getString("company"));
                role.setText(documentSnapshot.getString("designation"));
                workDays.setText(documentSnapshot.getString("workingDaysText"));
                startTime.setText("Work Start Time - " + documentSnapshot.getString("workStartTime"));
                endTime.setText("Work End Time - " + documentSnapshot.getString("workEndingTime"));

                workStartTime = documentSnapshot.getString("workStartTime");
                workEndTime = documentSnapshot.getString("workEndingTime");
            }
        });
    }

    private void handleTimeButton(String type){
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

                if(type.equals("start")){
                    startTime.setText("Work Start Time - " + DateFormat.format("hh:mm aa", calendar));
                    workStartTime = String.valueOf(DateFormat.format("hh:mm aa", calendar));
                }
                else{
                    endTime.setText("Work End Time - " + DateFormat.format("hh:mm aa", calendar));
                    workEndTime = String.valueOf(DateFormat.format("hh:mm aa", calendar));
                }
            }
        },Hour,Minute,false);
        timePickerDialog.show();
    }

}