package com.psychology.StatsUI.ERTStatsUI;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.psychology.R;

public class ErtStatsViewHolder extends RecyclerView.ViewHolder {

    PieChart pieChart;

    public ErtStatsViewHolder(@NonNull View itemView) {
        super(itemView);

        pieChart = itemView.findViewById(R.id.statsChart);

    }
}
