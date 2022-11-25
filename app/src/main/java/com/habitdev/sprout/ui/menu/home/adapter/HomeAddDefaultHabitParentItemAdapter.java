package com.habitdev.sprout.ui.menu.home.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.Subroutines;

import java.util.List;

public class HomeAddDefaultHabitParentItemAdapter extends RecyclerView.Adapter<HomeAddDefaultHabitParentItemAdapter.SubroutinesViewHolder> {

    private List<Subroutines> subroutines;

    public HomeAddDefaultHabitParentItemAdapter(List<Subroutines> subroutines) {
        this.subroutines = subroutines;
    }

    @NonNull
    @Override
    public SubroutinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubroutinesViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_add_default_parent_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SubroutinesViewHolder holder, int position) {
        holder.bindSubroutine(subroutines.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d("tag", "getItemCount: " + subroutines.size());
        return subroutines.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSubroutines(List<Subroutines> subroutines) {
        this.subroutines = subroutines;
        getItemCount();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class SubroutinesViewHolder extends RecyclerView.ViewHolder {

        TextView subroutine_title, subroutine_description;

        public SubroutinesViewHolder(@NonNull View itemView) {
            super(itemView);
            subroutine_title = itemView.findViewById(R.id.home_add_default_subroutine_title);
            subroutine_description = itemView.findViewById(R.id.home_add_default_subroutine_description);
        }

        void bindSubroutine(Subroutines subroutines) {
            subroutine_title.setText(subroutines.getSubroutine());
            subroutine_description.setText(subroutines.getDescription());
        }
    }
}
