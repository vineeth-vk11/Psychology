package com.psychology.StatsUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
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
import com.psychology.EntriesUI.ERTEntryActivity;
import com.psychology.EntriesUI.MREntryActivity;
import com.psychology.R;
import com.psychology.StatsUI.ERTStatsUI.ErtStatsAdapter;
import com.psychology.StatsUI.ERTStatsUI.ErtStatsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ErtStatsActivity extends AppCompatActivity {

    RecyclerView ertStatsRecyclerView;
    ArrayList<ErtStatsModel> ertStatsModelArrayList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText startDate, endDate;
    int selectedStartDay, selectedStartMonth, selectedStartYear;
    Date startDateD, endDateD, dateD;

    TextView meanOfEntries;

    Button addMore, skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ert_stats);

        ertStatsRecyclerView = findViewById(R.id.ert_stats_recycler);
        ertStatsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        ertStatsRecyclerView.setHasFixedSize(true);
        ertStatsRecyclerView.setNestedScrollingEnabled(false);

        startDate = findViewById(R.id.start_date_edit);
        endDate = findViewById(R.id.end_date_edit);

        meanOfEntries = findViewById(R.id.meanEntries);

        addMore = findViewById(R.id.addMore);
        skip = findViewById(R.id.skip);

        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ERTEntryActivity.class);
                startActivity(intent);
                finish();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                startDate.setText(DateFormat.format("dd-MM-yyyy", calendar));
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
                endDate.setText(DateFormat.format("dd-MM-yyyy", calendar));
                getDates(startDate.getText().toString(), endDate.getText().toString());
            }
        }, year, month, date);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(selectedStartYear, selectedStartMonth, selectedStartDay  + 1,
                0, 0, 0);

        datePickerDialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());

        datePickerDialog.show();
    }

    private ArrayList<String> getDates(String startingDate, String endingDate){

        List<Date> dates = new ArrayList<Date>();
        ArrayList<String> datesList = new ArrayList<>();

        String str_date = startingDate;
        String end_date = endingDate;

        SimpleDateFormat formatter ;

        formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate = null;
        try {
            startDate = (Date)formatter.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate = null;
        try {
            endDate = (Date)formatter.parse(end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long interval = 24*1000 * 60 * 60; // 1 hour in millis
        long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
        long curTime = startDate.getTime();
        while (curTime <= endTime) {
            dates.add(new Date(curTime));
            curTime += interval;
        }

        ertStatsModelArrayList.clear();

        for(int i=0;i<dates.size();i++){
            Date lDate =(Date)dates.get(i);
            String ds = formatter.format(lDate);
            System.out.println(" Date is ..." + ds);
            datesList.add(ds);

            ErtStatsModel ertStatsModel = new ErtStatsModel();
            ertStatsModel.setDate(ds);

            ertStatsModelArrayList.add(ertStatsModel);
        }

        ErtStatsAdapter ertStatsAdapter = new ErtStatsAdapter(getApplicationContext(), ertStatsModelArrayList, meanOfEntries);
        ertStatsRecyclerView.setAdapter(ertStatsAdapter);

        return datesList;
    }
}