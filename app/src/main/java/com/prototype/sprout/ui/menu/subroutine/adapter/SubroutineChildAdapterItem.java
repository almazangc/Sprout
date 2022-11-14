package com.prototype.sprout.ui.menu.subroutine.adapter;

import android.util.Log;
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
import com.prototype.sprout.database.habits_with_subroutines.Subroutines;

import java.util.List;

public class SubroutineChildAdapterItem extends RecyclerView.Adapter<SubroutineChildAdapterItem.ChildItemViewHolder> {

    List<Subroutines> subroutines;

    public SubroutineChildAdapterItem(List<Subroutines> subroutines) {
        this.subroutines = subroutines;
    }

    @NonNull
    @Override
    public SubroutineChildAdapterItem.ChildItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubroutineChildAdapterItem.ChildItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_subroutine_child_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SubroutineChildAdapterItem.ChildItemViewHolder holder, int position) {
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

    public class ChildItemViewHolder extends RecyclerView.ViewHolder {

        TextView subroutine, description, totalStreak;
        Button upvote, downvote, markAsDone;

        public ChildItemViewHolder(@NonNull View itemView) {
            super(itemView);
            subroutine = itemView.findViewById(R.id.subroutine);
            description = itemView.findViewById(R.id.description);
            totalStreak = itemView.findViewById(R.id.total_streak);

            upvote = itemView.findViewById(R.id.btn_upvote_subroutine);
            downvote = itemView.findViewById(R.id.btn_downvote_subroutine);
            markAsDone = itemView.findViewById(R.id.mark_as_done);
        }

        void bindDate(Subroutines subroutines) {

            subroutine.setText(subroutines.getSubroutine());
            description.setText(subroutines.getDescription());
            totalStreak.setText("0");

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
