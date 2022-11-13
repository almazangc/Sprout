package com.prototype.sprout.ui.menu.subroutine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prototype.sprout.R;
import com.prototype.sprout.database.sub_routine.Routine;

import java.util.List;

public class SubroutineChildAdapterItem extends RecyclerView.Adapter<SubroutineChildAdapterItem.ChildItemViewHolder> {

    List<Routine> routinesOnReform;

    public SubroutineChildAdapterItem(List<Routine> routinesOnReform) {
        this.routinesOnReform = routinesOnReform;
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
        holder.bindDate(routinesOnReform.get(position));
    }

    @Override
    public int getItemCount() {
        return routinesOnReform.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setRoutinesOnReform(List<Routine> routinesOnReform) {
        this.routinesOnReform = routinesOnReform;
    }

    static class ChildItemViewHolder extends RecyclerView.ViewHolder {

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

        void bindDate(Routine routine) {
            subroutine.setText(routine.getRoutine());
            description.setText("\t\tLorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam porta pharetra magna, eu laoreet urna condimentum a. Vivamus dapibus ante eget nunc ultrices bibendum. Duis consectetur fermentum nibh nec porta. Ut in fringilla leo. Aenean quis tortor vel tortor finibus cursus vel vel tellus. Nullam tellus enim, viverra et imperdiet sed, egestas id ante. Fusce rhoncus odio molestie elit ornare vulputate.");
            totalStreak.setText("999");

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
