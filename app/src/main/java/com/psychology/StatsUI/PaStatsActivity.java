package com.psychology.StatsUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.psychology.R;
import com.psychology.StatsUI.MrStatsUI.MrStatsAdapter;
import com.psychology.StatsUI.MrStatsUI.MrStatsModel;
import com.psychology.StatsUI.PaStatsUI.PaStatsAdapter;
import com.psychology.StatsUI.PaStatsUI.PaStatsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class PaStatsActivity extends AppCompatActivity {

    RecyclerView PaStatsRecycler;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<PaStatsModel> paStatsModelArrayList = new ArrayList<>();

    EditText startDate, endDate;

    int selectedStartDay, selectedStartMonth, selectedStartYear;

    Date startDateD, endDateD, dateD;

    TextView statsNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pa_stats);

        startDate = findViewById(R.id.start_date_edit);
        endDate = findViewById(R.id.end_date_edit);

        statsNumber = findViewById(R.id.mr_stats_number);

        PaStatsRecycler = findViewById(R.id.pa_stats_recycler);
        PaStatsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        PaStatsRecycler.setHasFixedSize(true);
        PaStatsRecycler.setNestedScrollingEnabled(false);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(startDate.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Please select a start date first", Toast.LENGTH_SHORT).show();
                }
                else{
                    handleDateButton1();
                }
            }
        });

    }

    private void getEntries(){

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            startDateD = format.parse(startDate.getText().toString().trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            endDateD = format.parse(endDate.getText().toString().trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("PA").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                paStatsModelArrayList.clear();

                for(DocumentSnapshot documentSnapshot: task.getResult()){

                    PaStatsModel paStatsModel = new PaStatsModel();
                    paStatsModel.setDate(documentSnapshot.getString("affirmationDate"));
                    paStatsModel.setAffirmation(documentSnapshot.getString("affirmation"));

                    try {
                        dateD = format.parse(documentSnapshot.getString("affirmationDate"));
                        paStatsModel.setDateD(dateD);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    assert dateD != null;
                    if(dateD.compareTo(startDateD) >= 0 && dateD.compareTo(endDateD) <= 0){
                        paStatsModelArrayList.add(paStatsModel);
                    }

                }

                Collections.sort(paStatsModelArrayList);
                Collections.reverse(paStatsModelArrayList);

                int number = paStatsModelArrayList.size();

                statsNumber.setText("You have " + String.valueOf(number) + " Positive Affirmations in the selected range of time period");
                PaStatsAdapter paStatsAdapter = new PaStatsAdapter(getApplicationContext(), paStatsModelArrayList);
                PaStatsRecycler.setAdapter(paStatsAdapter);
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

                selectedStartDay = dayOfMonth;
                selectedStartMonth = month;
                selectedStartYear = year;

                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);
                startDate.setText(DateFormat.format("dd/MM/yyyy", calendar));
            }
        }, year, month, date);

        datePickerDialog.show();
    }

    private void handleDateButton1(){

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);
                endDate.setText(DateFormat.format("dd/MM/yyyy", calendar));
                getEntries();
            }
        }, year, month, date);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(selectedStartYear, selectedStartMonth, selectedStartDay  + 1,
                0, 0, 0);

        datePickerDialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());

        datePickerDialog.show();
    }
}