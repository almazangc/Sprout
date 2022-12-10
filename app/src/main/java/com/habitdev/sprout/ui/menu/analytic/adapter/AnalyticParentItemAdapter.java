package com.habitdev.sprout.ui.menu.analytic.adapter;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.utill.DateTimeElapsedUtil;
import com.habitdev.sprout.utill.HabitDiffUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AnalyticParentItemAdapter extends RecyclerView.Adapter<AnalyticParentItemAdapter.AnalyticParentViewHolder> {

    private List<Habits> oldHabitsList;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;

    public AnalyticParentItemAdapter() {
        oldHabitsList = new ArrayList<>();
    }

    public void setOldHabitsList(List<Habits> oldHabitsList) {
        this.oldHabitsList = oldHabitsList;
    }

    public void setHabitWithSubroutinesViewModel(HabitWithSubroutinesViewModel habitWithSubroutinesViewModel) {
        this.habitWithSubroutinesViewModel = habitWithSubroutinesViewModel;
    }

    @NonNull
    @Override
    public AnalyticParentItemAdapter.AnalyticParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnalyticParentViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_analytic_parent_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AnalyticParentItemAdapter.AnalyticParentViewHolder holder, int position) {
        holder.bindHabit(oldHabitsList.get(position));

        DateTimeElapsedUtil dateTimeElapsedUtil = new DateTimeElapsedUtil(oldHabitsList.get(position).getDate_started());

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    dateTimeElapsedUtil.calculateElapsedDateTime();
                    holder.elapsedAbstinence.setText(dateTimeElapsedUtil.getResult());
                });
            }
        }, 0, 1000);

        Habits habit = oldHabitsList.get(position);
        List<Subroutines> subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid());

        int skip_subroutine = 0;
        for (Subroutines subroutine : subroutinesList){
            skip_subroutine += subroutine.getTotal_skips();
        }

        int completed_subroutine = habit.getCompleted_subroutine();
        int total_subroutine = completed_subroutine + skip_subroutine;

        String pieChartbl = completed_subroutine + "/" + total_subroutine;
        holder.pieChartLbl.setText(pieChartbl);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(completed_subroutine, "completed"));
        pieEntries.add(new PieEntry(skip_subroutine, "skips"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Habit");
        //set color

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(holder.pieChart));
        pieData.setValueTextSize(5f);

        holder.pieChart.setData(pieData);
        holder.pieChart.setDrawHoleEnabled(false);
        holder.pieChart.setUsePercentValues(true);
    }

    @Override
    public int getItemCount() {
        return oldHabitsList.size();
    }

    public void setNewHabitList(List<Habits> newHabitList) {
        DiffUtil.Callback DIFF_CALLBACK = new HabitDiffUtil(oldHabitsList, newHabitList);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
        this.oldHabitsList = newHabitList;
        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(this);
    }

    public static class AnalyticParentViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemContainer;
        PieChart pieChart;
        TextView pieChartLbl, totalSubroutine, title, description, dateStarted, elapsedAbstinence, relapse;

        Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin;

        public AnalyticParentViewHolder(@NonNull View itemView) {
            super(itemView);
            itemContainer = itemView.findViewById(R.id.adapter_home_parent_item_container);

            pieChart = itemView.findViewById(R.id.adapter_analytic_parent_item_pie_chart_total_completed_subroutine);
            pieChartLbl = itemView.findViewById(R.id.adapter_analytic_parent_item_pie_chart_lbl);
            totalSubroutine = itemView.findViewById(R.id.adapter_analytic_parent_item_habit_total_subroutine);
            title = itemView.findViewById(R.id.adapter_analytic_parent_item_habit_title);
            description = itemView.findViewById(R.id.adapter_analytic_parent_item_habit_description);
            dateStarted = itemView.findViewById(R.id.adapter_analytic_parent_item_date_started);
            elapsedAbstinence = itemView.findViewById(R.id.adapter_analytic_parent_item_elapsed_abstinence);
            relapse = itemView.findViewById(R.id.adapter_analytic_parent_item_total_relapse);

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_parent_item_view_cloud_selector);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_parent_item_view_amethyst_selector);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_parent_item_view_sunflower_selector);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_parent_item_view_nephritis_selector);
            bright_sky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_parent_item_view_brightsky_blue_selector);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_parent_item_view_alzarin_selector);
        }

        void bindHabit(Habits habit) {



            if (habit.getColor().equals(AppColor.ALZARIN.getColor())) {
                itemContainer.setBackground(alzarin);
            } else if (habit.getColor().equals(AppColor.AMETHYST.getColor())) {
                itemContainer.setBackground(amethyst);
            } else if (habit.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
                itemContainer.setBackground(bright_sky_blue);
            } else if (habit.getColor().equals(AppColor.NEPHRITIS.getColor())) {
                itemContainer.setBackground(nephritis);
            } else if (habit.getColor().equals(AppColor.SUNFLOWER.getColor())) {
                itemContainer.setBackground(sunflower);
            } else {
                itemContainer.setBackground(cloud);
            }

            totalSubroutine.setText("[ " + String.valueOf(habit.getTotal_subroutine()) + " ]");
            title.setText(habit.getHabit());
            description.setText(habit.getDescription());
            dateStarted.setText(habit.getDate_started());

            relapse.setText(String.valueOf(habit.getRelapse()));


        }
    }
}
