package com.habitdev.sprout.ui.menu.subroutine.adapter;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.enums.TimeMilestone;
import com.habitdev.sprout.utill.DateTimeElapsedUtil;
import com.habitdev.sprout.utill.SubroutineDiffUtil;

import java.util.List;

public class SubroutineChildItemAdapter extends RecyclerView.Adapter<SubroutineChildItemAdapter.ChildItemViewHolder> {

    private List<Subroutines> oldSubroutineList;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;

    public SubroutineChildItemAdapter() {

    }

    public void setHabitWithSubroutinesViewModel(HabitWithSubroutinesViewModel habitWithSubroutinesViewModel) {
        this.habitWithSubroutinesViewModel = habitWithSubroutinesViewModel;
    }

    public void setOldSubroutineList(List<Subroutines> oldSubroutineList) {
        this.oldSubroutineList = oldSubroutineList;
    }

    @NonNull
    @Override
    public SubroutineChildItemAdapter.ChildItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChildItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_subroutine_child_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SubroutineChildItemAdapter.ChildItemViewHolder holder, int position) {
        holder.bindDate(oldSubroutineList.get(position), habitWithSubroutinesViewModel);

        Subroutines subroutine = oldSubroutineList.get(position);
        Habits habit = habitWithSubroutinesViewModel.getHabitByUID(subroutine.getFk_habit_uid());

        DateTimeElapsedUtil dateTimeElapsedUtil = new DateTimeElapsedUtil(habit.getDate_started());
        dateTimeElapsedUtil.calculateElapsedDateTime();

        if (dateTimeElapsedUtil.getElapsed_day() >= TimeMilestone.AVG_HABIT_BREAK_DAY.getDays()) {
            holder.UpVote.setVisibility(View.VISIBLE);
            holder.DownVote.setVisibility(View.VISIBLE);
        } else {
            holder.UpVote.setVisibility(View.GONE);
            holder.DownVote.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return oldSubroutineList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setNewSubroutineList(List<Subroutines> newSubroutineList) {
        DiffUtil.Callback DIFF_CALLBACK = new SubroutineDiffUtil(oldSubroutineList, newSubroutineList);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
        this.oldSubroutineList = newSubroutineList;
        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(this);
    }

    public static class ChildItemViewHolder extends RecyclerView.ViewHolder{

        final RelativeLayout itemLayout;
        final TextView Title, Description;
        final Button UpVote, DownVote, MarkAsDone;
        final Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin, markedAsDone, unMarkAsDone;

        public ChildItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.subroutine_child_item_layout);
            Title = itemView.findViewById(R.id.subroutine);
            Description = itemView.findViewById(R.id.home_item_on_click_habit_description);

            UpVote = itemView.findViewById(R.id.btn_upvote_subroutine);
            DownVote = itemView.findViewById(R.id.btn_downvote_subroutine);
            MarkAsDone = itemView.findViewById(R.id.mark_as_done);

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_cloud_selector);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_amethyst_selector);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_sunflower_selector);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_nephritis_selector);
            bright_sky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_brightsky_blue_selector);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_alzarin_selector);

            markedAsDone = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_done);
            unMarkAsDone = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_not_done);
        }

        void bindDate(Subroutines subroutine, HabitWithSubroutinesViewModel habitWithSubroutinesViewModel) {

            if (subroutine.getColor().equals(AppColor.ALZARIN.getColor())) {
                itemLayout.setBackground(alzarin);
            } else if (subroutine.getColor().equals(AppColor.AMETHYST.getColor())) {
                itemLayout.setBackground(amethyst);
            } else if (subroutine.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
                itemLayout.setBackground(bright_sky_blue);
            } else if (subroutine.getColor().equals(AppColor.NEPHRITIS.getColor())) {
                itemLayout.setBackground(nephritis);
            } else if (subroutine.getColor().equals(AppColor.SUNFLOWER.getColor())) {
                itemLayout.setBackground(sunflower);
            } else {
                itemLayout.setBackground(cloud);
            }

            isMarkedAsDone(subroutine.isMarkDone());

            itemLayout.setOnClickListener(view -> {
                Habits habit = habitWithSubroutinesViewModel.getHabitByUID(subroutine.getFk_habit_uid());

                if (MarkAsDone.getBackground() == markedAsDone) {
                    isMarkedAsDone(false);
                    subroutine.setMarkDone(false);
                    subroutine.setTotal_completed(subroutine.getTotal_completed()-1);

                    habit.setCompleted_subroutine(habit.getCompleted_subroutine()-1);
                } else if (MarkAsDone.getBackground() == unMarkAsDone) {
                    isMarkedAsDone(true);
                    subroutine.setMarkDone(true);
                    subroutine.setTotal_completed(subroutine.getTotal_completed()+1);

                    habit.setCompleted_subroutine(habit.getCompleted_subroutine()+1);
                }

                habitWithSubroutinesViewModel.updateSubroutine(subroutine);
                habitWithSubroutinesViewModel.updateHabit(habit);
            });

            Title.setText(subroutine.getSubroutine());
            Description.setText(subroutine.getDescription());

            UpVote.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "UpVote", Toast.LENGTH_SHORT).show();
            });

            DownVote.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "DownVote", Toast.LENGTH_SHORT).show();
            });
        }

        void isMarkedAsDone(boolean isMarkedAsDone){
            if (isMarkedAsDone){
                MarkAsDone.setBackground(markedAsDone);
                Title.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                Description.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                MarkAsDone.setBackground(unMarkAsDone);
                Title.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
                Description.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            }
        }
    }
}
