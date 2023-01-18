package com.habitdev.sprout.ui.menu.analytic.adapter;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.room.Subroutines;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.utill.SubroutineDiffUtil;

import java.util.ArrayList;
import java.util.List;

public class AnalyticItemOnClickParentItemAdapter extends RecyclerView.Adapter<AnalyticItemOnClickParentItemAdapter.AnalyticItemOnClickParentViewHolder> {

    private List<Subroutines> oldSubroutinesList;

    public AnalyticItemOnClickParentItemAdapter() {
        oldSubroutinesList = new ArrayList<>();
    }

    public void setOldSubroutinesList(List<Subroutines> oldSubroutinesList) {
        this.oldSubroutinesList = oldSubroutinesList;
    }

    @NonNull
    @Override
    public AnalyticItemOnClickParentItemAdapter.AnalyticItemOnClickParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnalyticItemOnClickParentViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_analytic_item_on_click_parent_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AnalyticItemOnClickParentItemAdapter.AnalyticItemOnClickParentViewHolder holder, int position) {
        holder.bindSubroutine(oldSubroutinesList.get(position));
    }

    @Override
    public int getItemCount() {
        return oldSubroutinesList.size();
    }

    public void setNewSubroutineList(List<Subroutines> newSubroutineList) {
        DiffUtil.Callback DIFF_CALLBACK = new SubroutineDiffUtil(oldSubroutinesList, newSubroutineList);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
        oldSubroutinesList = newSubroutineList;
        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(this);
    }

    public static class AnalyticItemOnClickParentViewHolder extends RecyclerView.ViewHolder {

        final RelativeLayout itemContainer;
        final PieChart pieChart;
        final TextView title, description, longestStreak, maxStreak;
        final Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin;

        public AnalyticItemOnClickParentViewHolder(@NonNull View itemView) {
            super(itemView);

            itemContainer = itemView.findViewById(R.id.adapter_analytic_item_on_click_container);
            pieChart = itemView.findViewById(R.id.adapter_analytic_item_on_click_subroutine_pie_chart);

            title = itemView.findViewById(R.id.adapter_analytic_item_on_click_subroutine_title);
            description = itemView.findViewById(R.id.adapter_analytic_item_on_click_subroutine_description);
            longestStreak = itemView.findViewById(R.id.adapter_analytic_item_on_click_subroutine_longest_streak);
            maxStreak = itemView.findViewById(R.id.adapter_analytic_item_on_click_subroutine_max_streak);

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_cloud_selector);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_amethyst_selector);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_sunflower_selector);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_nephritis_selector);
            bright_sky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_brightsky_blue_selector);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_alzarin_selector);
        }

        void bindSubroutine(Subroutines subroutine) {

            if (subroutine.getColor().equals(AppColor.ALZARIN.getColor())) {
                itemContainer.setBackground(alzarin);
            } else if (subroutine.getColor().equals(AppColor.AMETHYST.getColor())) {
                itemContainer.setBackground(amethyst);
            } else if (subroutine.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
                itemContainer.setBackground(bright_sky_blue);
            } else if (subroutine.getColor().equals(AppColor.NEPHRITIS.getColor())) {
                itemContainer.setBackground(nephritis);
            } else if (subroutine.getColor().equals(AppColor.SUNFLOWER.getColor())) {
                itemContainer.setBackground(sunflower);
            } else {
                itemContainer.setBackground(cloud);
            }

            int skip_subroutine = subroutine.getTotal_skips();
            int completed_subroutine = subroutine.getTotal_completed();
            int total_subroutine = skip_subroutine + completed_subroutine;

            if (total_subroutine > 0 ){

                pieChart.setVisibility(View.VISIBLE);

                ArrayList<PieEntry> pieEntries = new ArrayList<>();
                ArrayList<Integer> colors = new ArrayList<>();
                if (completed_subroutine > 0){
                    pieEntries.add(new PieEntry(completed_subroutine, "Completed: " + completed_subroutine));
                    colors.add(ContextCompat.getColor(itemView.getContext(), R.color.BRIGHT_SKY_BLUE));
                }
                if (skip_subroutine > 0){
                    pieEntries.add(new PieEntry(skip_subroutine, "Skips: " + skip_subroutine));
                    colors.add(ContextCompat.getColor(itemView.getContext(), R.color.ALIZARIN));
                }

                PieDataSet pieDataSet = new PieDataSet(pieEntries, "Total: " + total_subroutine);
                pieDataSet.setSliceSpace(5f);
                pieDataSet.setColors(colors);

                PieData pieData = new PieData(pieDataSet);
                pieData.setDrawValues(true);
                pieData.setValueFormatter(new PercentFormatter(pieChart));
                pieData.setValueTextSize(15f);
                pieData.setValueTextColor(ContextCompat.getColor(itemView.getContext(), R.color.NIGHT));
                pieData.setHighlightEnabled(true);

                pieChart.setData(pieData);
                pieChart.invalidate();
                pieChart.setDrawHoleEnabled(true);
                pieChart.setUsePercentValues(true);
                pieChart.setDrawEntryLabels(false);
                pieChart.setDrawSlicesUnderHole(false);
                pieChart.setHoleColor(ContextCompat.getColor(itemView.getContext(), R.color.TRANSPARENT));

                Description pie_description = new Description();
                pie_description.setText("Pie Chart Label");
                pie_description.setTextAlign(Paint.Align.RIGHT);
                pie_description.setTextSize(15f);
                pie_description.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.CORAL_RED));
                pieChart.setDescription(pie_description);
                pieChart.getDescription().setEnabled(false); // Hide description
                pieChart.animateY(1000, Easing.EaseInOutBack);

                Legend legend = pieChart.getLegend();
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                legend.setOrientation(Legend.LegendOrientation.VERTICAL);
                legend.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.NIGHT));
                legend.setTextSize(15f);
                legend.setDrawInside(false); // set drawable false
            } else {
                pieChart.setVisibility(View.GONE);
            }

            title.setText(subroutine.getSubroutine());
            description.setText(subroutine.getDescription());
            longestStreak.setText(String.valueOf(subroutine.getLongest_streak()));
            maxStreak.setText(String.valueOf(subroutine.getMax_streak()));
        }
    }
}
