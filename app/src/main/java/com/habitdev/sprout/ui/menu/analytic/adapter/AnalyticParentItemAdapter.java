package com.habitdev.sprout.ui.menu.analytic.adapter;

import android.graphics.Paint;
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
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.database.habit.model.room.Subroutines;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.utill.diffutils.DateTimeElapsedUtil;
import com.habitdev.sprout.utill.diffutils.HabitDiffUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.threeten.bp.DayOfWeek;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AnalyticParentItemAdapter extends RecyclerView.Adapter<AnalyticParentItemAdapter.AnalyticParentViewHolder> {

    private List<Habits> oldHabitsList;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;

    public interface onItemClick{
        void analyticOnItemClick(int position);
    }

    private onItemClick mOnItemClick;

    public void setmOnItemClick(onItemClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
    }

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
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_analytic_parent_item, parent, false),
                mOnItemClick
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

        if (total_subroutine > 0){

            holder.pieChart.setVisibility(View.VISIBLE);

            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            ArrayList<Integer> colors = new ArrayList<>();
            if (completed_subroutine > 0){
                pieEntries.add(new PieEntry(completed_subroutine, "Completed: " + completed_subroutine));
                colors.add(ContextCompat.getColor(holder.itemView.getContext(), R.color.BRIGHT_SKY_BLUE));
            }
            if (skip_subroutine > 0){
                pieEntries.add(new PieEntry(skip_subroutine, "Skips: " + skip_subroutine));
                colors.add(ContextCompat.getColor(holder.itemView.getContext(), R.color.ALIZARIN));
            }

            PieDataSet pieDataSet = new PieDataSet(pieEntries, "Total: " + total_subroutine);
            pieDataSet.setSliceSpace(5f);
            pieDataSet.setColors(colors);

            PieData pieData = new PieData(pieDataSet);
            pieData.setDrawValues(true);
            pieData.setValueFormatter(new PercentFormatter(holder.pieChart));
            pieData.setValueTextSize(15f);
            pieData.setValueTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.NIGHT));
            pieData.setHighlightEnabled(true);

            holder.pieChart.setData(pieData); // set date entry
            holder.pieChart.invalidate(); // refresh
            holder.pieChart.setDrawHoleEnabled(true); // donut hole
            holder.pieChart.setUsePercentValues(true); // convert to percentage
//        holder.pieChart.setEntryLabelColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.NIGHT)); //entry lbl color
//        holder.pieChart.setDrawCenterText(true); // true by default
//        holder.pieChart.setCenterText(completed_subroutine + "/" + total_subroutine); //center lbl content
//        holder.pieChart.setCenterTextSize(15f); // center lbl text size
            holder.pieChart.setDrawEntryLabels(false); //entry label hidden or not
//        holder.pieChart.setEntryLabelColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.NIGHT));
            holder.pieChart.setDrawSlicesUnderHole(false); // dunno
//        holder.pieChart.setHoleRadius(10f); // hole radius
            holder.pieChart.setHoleColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.TRANSPARENT));

            Description description = new Description();
            description.setText("Pie Chart Label");
            description.setTextAlign(Paint.Align.RIGHT);
            description.setTextSize(15f);
            description.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.CORAL_RED));
            holder.pieChart.setDescription(description);
            holder.pieChart.getDescription().setEnabled(false); // Hide description
            holder.pieChart.animateY(1000, Easing.EaseInOutBack);

            Legend legend = holder.pieChart.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            legend.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.NIGHT));
            legend.setTextSize(15f);
            legend.setDrawInside(false); // set drawable false
            legend.setEnabled(true); // enable by default
        } else {
            holder.pieChart.setVisibility(View.GONE);
        }

        dateTimeElapsedUtil.setDate_started(habit.getDate_started());
        dateTimeElapsedUtil.convertToDate();
        Date date = dateTimeElapsedUtil.getStart_date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(simpleDateFormat.format(date));
        simpleDateFormat.applyPattern("MM");
        int month = Integer.parseInt(simpleDateFormat.format(date));
        simpleDateFormat.applyPattern("d");
        int day = Integer.parseInt(simpleDateFormat.format(date));

        holder.calendarView
                .setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        holder.calendarView.
                setDateSelected(CalendarDay.from(year, month, day), true);
//        holder.calendarView.
//                setDateSelected(CalendarDay.today(), true);
        holder.calendarView
                .setAllowClickDaysOutsideCurrentMonth(false);

        holder.calendarView
                .state()
                .edit()
                .setFirstDayOfWeek(DayOfWeek.of(Calendar.SUNDAY))
                .setMaximumDate(CalendarDay.from(year, month, day)) // CalendarDay.today()
                .setMinimumDate(CalendarDay.from(year, month, day))
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .setShowWeekDays(true)
                .commit();
    }

    @Override
    public int getItemCount() {
        return oldHabitsList.size();
    }

    public void setNewHabitList(List<Habits> newHabitList) {
        DiffUtil.Callback DIFF_CALLBACK = new HabitDiffUtil(oldHabitsList, newHabitList);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
        this.oldHabitsList.clear();
        this.oldHabitsList.addAll(newHabitList);
        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(this);
    }

    public static class AnalyticParentViewHolder extends RecyclerView.ViewHolder {

        final RelativeLayout itemContainer;
        final PieChart pieChart;
        final MaterialCalendarView calendarView;
        final TextView totalSubroutine, title, description, dateStarted, elapsedAbstinence, relapse;
        final Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin;

        public AnalyticParentViewHolder(@NonNull View itemView, onItemClick mOnItemClick) {
            super(itemView);
            itemContainer = itemView.findViewById(R.id.adapter_home_parent_item_container);

            pieChart = itemView.findViewById(R.id.adapter_analytic_parent_item_pie_chart_total_completed_subroutine);
            calendarView = itemView.findViewById(R.id.adapter_analytic_parent_item_calendar);

            totalSubroutine = itemView.findViewById(R.id.adapter_analytic_parent_item_habit_total_subroutine);
            title = itemView.findViewById(R.id.adapter_analytic_parent_item_habit_title);
            description = itemView.findViewById(R.id.adapter_analytic_parent_item_habit_description);
            dateStarted = itemView.findViewById(R.id.adapter_analytic_parent_item_date_started);
            elapsedAbstinence = itemView.findViewById(R.id.adapter_analytic_parent_item_elapsed_abstinence);
            relapse = itemView.findViewById(R.id.adapter_analytic_parent_item_total_relapse);

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_cloud_selector);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_amethyst_selector);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_sunflower_selector);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_nephritis_selector);
            bright_sky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_brightsky_blue_selector);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_alzarin_selector);

            itemContainer.setOnClickListener(view -> {
                if (mOnItemClick != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mOnItemClick.analyticOnItemClick(position);
                    }
                }
            });
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

            totalSubroutine.setText(String.valueOf(habit.getTotal_subroutine()));
            title.setText(habit.getHabit());
            description.setText(habit.getDescription());
            dateStarted.setText(habit.getDate_started());
            relapse.setText(String.valueOf(habit.getRelapse()));
        }
    }
}
