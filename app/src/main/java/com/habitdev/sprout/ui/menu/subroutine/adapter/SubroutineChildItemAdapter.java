package com.habitdev.sprout.ui.menu.subroutine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.Subroutines;

import java.util.List;

public class SubroutineChildItemAdapter extends RecyclerView.Adapter<SubroutineChildItemAdapter.ChildItemViewHolder> {

    List<Subroutines> subroutines;

    public SubroutineChildItemAdapter(List<Subroutines> subroutines) {
        this.subroutines = subroutines;
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
        holder.bindDate(subroutines.get(position));
    }

    @Override
    public int getItemCount() {
        return subroutines.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setHabitWithSubroutines(List<Subroutines> subroutines) {
        this.subroutines = subroutines;
    }

    public static class ChildItemViewHolder extends RecyclerView.ViewHolder {

        TextView subroutine, description;
        Button upvote, downvote, markAsDone;

        public ChildItemViewHolder(@NonNull View itemView) {
            super(itemView);
            subroutine = itemView.findViewById(R.id.subroutine);
            description = itemView.findViewById(R.id.home_item_on_click_habit_description);

            upvote = itemView.findViewById(R.id.btn_upvote_subroutine);
            downvote = itemView.findViewById(R.id.btn_downvote_subroutine);
            markAsDone = itemView.findViewById(R.id.mark_as_done);
        }

        void bindDate(Subroutines subroutines) {

            subroutine.setText(subroutines.getSubroutine());
            description.setText(subroutines.getDescription());

            upvote.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "UpVote", Toast.LENGTH_SHORT).show();
            });

            downvote.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "DownVote", Toast.LENGTH_SHORT).show();
            });

            markAsDone.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "Marked as Done", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
