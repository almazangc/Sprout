package com.habitdev.sprout.ui.menu.home.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.interfaces.IRecyclerView;

import java.util.List;

public class HomeParentItemAdapter extends RecyclerView.Adapter<HomeParentItemAdapter.HabitViewHolder> {

    private List<Habits> habits;
    private final com.habitdev.sprout.interfaces.IRecyclerView IRecyclerView;

    public HomeParentItemAdapter(List<Habits> habits, IRecyclerView IRecyclerView) {
        this.habits = habits;
        this.IRecyclerView = IRecyclerView;
    }

    @NonNull
    @Override
    public HomeParentItemAdapter.HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeParentItemAdapter.HabitViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_parent_habit_item, parent, false), IRecyclerView
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HomeParentItemAdapter.HabitViewHolder holder, int position) {
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

        public HabitViewHolder(@NonNull View itemView, IRecyclerView IRecyclerView) {
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(IRecyclerView != null){
                        int position = getBindingAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            IRecyclerView.onItemClick(position);
                        }
                    }
                }
            });
        }

        void bindHabit(Habits habit) {
            habitHeader.setText(habit.getHabit());

            if (habit.getDescription().trim().isEmpty()) {
                habitDescription.setVisibility(View.GONE);
            } else {
                habitDescription.setText(habit.getDescription());
            }

            dateStarted.setText(habit.getDate_started());

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
