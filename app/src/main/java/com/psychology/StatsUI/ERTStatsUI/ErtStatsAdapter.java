package com.psychology.StatsUI.ERTStatsUI;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.psychology.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ErtStatsAdapter extends RecyclerView.Adapter<ErtStatsViewHolder> {

    Context context;
    ArrayList<ErtStatsModel> ertStatsModelArrayList;
    TextView entriesMean;

    public ErtStatsAdapter(Context context, ArrayList<ErtStatsModel> ertStatsModelArrayList, TextView entriesMean) {
        this.context = context;
        this.ertStatsModelArrayList = ertStatsModelArrayList;
        this.entriesMean = entriesMean;
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    int verySadTotal = 0, sadTotal = 0, normalTotal = 0, happyTotal = 0, veryHappyTotal = 0, total = 0;

    @NonNull
    @Override
    public ErtStatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item_ert_stats, parent, false);
        return new ErtStatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ErtStatsViewHolder holder, int position) {

        String date = ertStatsModelArrayList.get(position).getDate();

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("ERTbyDates").document(date)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();

                ArrayList<PieEntry> chartData = new ArrayList<>();

                if(documentSnapshot.exists()){

                    chartData.add(new PieEntry((int)(long)documentSnapshot.get("verySad"), "Very Sad"));
                    chartData.add(new PieEntry((int)(long)documentSnapshot.get("sad"), "Sad"));
                    chartData.add(new PieEntry((int)(long)documentSnapshot.get("normal"), "Normal"));
                    chartData.add(new PieEntry((int)(long)documentSnapshot.get("happy"), "Happy"));
                    chartData.add(new PieEntry((int)(long)documentSnapshot.get("veryHappy"), "Very Happy"));

                    verySadTotal += (int)(long)documentSnapshot.get("verySad");
                    sadTotal = (int)(long)documentSnapshot.get("sad");
                    normalTotal = (int)(long)documentSnapshot.get("normal");
                    happyTotal = (int)(long)documentSnapshot.get("happy");
                    veryHappyTotal = (int)(long)documentSnapshot.get("veryHappy");

                    Log.i("1", String.valueOf(verySadTotal));
                    Log.i("2", String.valueOf(sadTotal));

                    PieDataSet pieDataSet = new PieDataSet(chartData,"" );
                    pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                    PieData pieData = new PieData(pieDataSet);

                    holder.pieChart.setData(pieData);
                    holder.pieChart.getDescription().setEnabled(false);
                    holder.pieChart.setCenterText(ertStatsModelArrayList.get(position).getDate());
                    holder.pieChart.setCenterTextSize(5f);
                    holder.pieChart.setDrawEntryLabels(false);
                    holder.pieChart.getData().setDrawValues(false);
                    holder.pieChart.getLegend().setEnabled(false);
                    holder.pieChart.animate();
                    holder.pieChart.invalidate();

                    int total = verySadTotal + 2*sadTotal + 3*normalTotal + 4*happyTotal + 5*veryHappyTotal;
                    int totalNumber = veryHappyTotal + sadTotal + normalTotal + happyTotal + veryHappyTotal;
                    float average = (float)total/(float)totalNumber;

                    entriesMean.setText("Mean of all your entries - " + average);
                }
                else {
                    chartData.add(new PieEntry(0, "Very Sad"));
                    chartData.add(new PieEntry(0, "Sad"));
                    chartData.add(new PieEntry(0, "Normal"));
                    chartData.add(new PieEntry(0, "Happy"));
                    chartData.add(new PieEntry(0, "Very Happy"));


                    PieDataSet pieDataSet = new PieDataSet(chartData,"" );
                    pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                    PieData pieData = new PieData(pieDataSet);

                    holder.pieChart.setData(pieData);
                    holder.pieChart.getDescription().setEnabled(false);
                    holder.pieChart.setCenterText(ertStatsModelArrayList.get(position).getDate());
                    holder.pieChart.setCenterTextSize(5f);
                    holder.pieChart.setDrawEntryLabels(false);
                    holder.pieChart.getData().setDrawValues(false);
                    holder.pieChart.getLegend().setEnabled(false);
                    holder.pieChart.animate();
                    holder.pieChart.invalidate();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return ertStatsModelArrayList.size();
    }
}
