package com.prototype.sprout.ui.menu.home.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prototype.sprout.R;
import com.prototype.sprout.database.habits_with_subroutines.Habits;

import java.util.List;

public class HomeParentAdapterItem extends RecyclerView.Adapter<HomeParentAdapterItem.HabitViewHolder> {

    private List<Habits> habits;

    public HomeParentAdapterItem(List<Habits> habits) {
        this.habits = habits;
    }

    @NonNull
    @Override
    public HomeParentAdapterItem.HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeParentAdapterItem.HabitViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_parent_habit_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HomeParentAdapterItem.HabitViewHolder holder, int position) {
        holder.bindHabit(habits.get(position));
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setHabits(List<Habits> habits) {
        this.habits = habits;
        notifyDataSetChanged();
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder {

        TextView habitHeader, habitDescription, dateStarted, completedSubroutine, daysOfAbstinence, totalReplase;
        Button upvote, downvote, modify, relapse, drop;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            habitHeader = itemView.findViewById(R.id.header);
            habitDescription = itemView.findViewById(R.id.description);
            dateStarted = itemView.findViewById(R.id.date_started);
            completedSubroutine = itemView.findViewById(R.id.completed_subroutine);
            daysOfAbstinence = itemView.findViewById(R.id.total_days_of_abstinence);
            totalReplase = itemView.findViewById(R.id.total_relapse);

            upvote = itemView.findViewById(R.id.home_upvote_btn);
            downvote = itemView.findViewById(R.id.home_downvote_btn);

            modify = itemView.findViewById(R.id.home_modify_btn);
            relapse = itemView.findViewById(R.id.home_relapse_btn);
            drop = itemView.findViewById(R.id.home_drop_btn);
        }

        void bindHabit(Habits habit) {
            habitHeader.setText(habit.getHabit());

            if (habit.getDescription().trim().isEmpty()) {
                habitDescription.setVisibility(View.GONE);
            } else {
                habitDescription.setText(habit.getDescription());
            }

//            dateStarted.setVisibility(View.GONE);

            dateStarted.setText("November 7 2022, 14:25PM");

            completedSubroutine.setText(String.valueOf(habit.getTotal_subroutine()));
            daysOfAbstinence.setText(String.valueOf(habit.getAbstinence()));
            totalReplase.setText(String.valueOf(habit.getRelapse()));

            upvote.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "Upvote", Toast.LENGTH_SHORT).show();
            });

            downvote.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "Downvote", Toast.LENGTH_SHORT).show();
            });

            modify.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "Modify", Toast.LENGTH_SHORT).show();
            });

            relapse.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "Relapse", Toast.LENGTH_SHORT).show();
            });

            drop.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "Drop", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
