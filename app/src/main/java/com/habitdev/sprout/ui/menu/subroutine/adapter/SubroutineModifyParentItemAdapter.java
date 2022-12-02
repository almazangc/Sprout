package com.habitdev.sprout.ui.menu.subroutine.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.database.habit.model.Subroutines;

import java.util.List;

public class SubroutineModifyParentItemAdapter extends ListAdapter<List<Subroutines>, SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder> {

    protected SubroutineModifyParentItemAdapter(@NonNull DiffUtil.ItemCallback<List<Subroutines>> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public static class SubroutineModifyViewHolder extends RecyclerView.ViewHolder {
        public SubroutineModifyViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bindSubroutine(Subroutines subroutines) {

        }
    }
}
