package com.habitdev.sprout.ui.habit_assessment.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.room.Subroutines;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.utill.diffutils.SubroutineDiffUtil;

import java.util.ArrayList;
import java.util.List;

public class AnalysisParentItemAdapter extends RecyclerView.Adapter<AnalysisParentItemAdapter.AnalysisParentViewHolder> {

    private List<Subroutines> oldSubroutinesList;

    public AnalysisParentItemAdapter() {
        this.oldSubroutinesList = new ArrayList<>();
    }

    public void setOldSubroutinesList(List<Subroutines> oldSubroutinesList) {
        this.oldSubroutinesList = oldSubroutinesList;
    }

    @NonNull
    @Override
    public AnalysisParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnalysisParentViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_analysis_parent_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AnalysisParentViewHolder holder, int position) {
        holder.bindSubroutine(oldSubroutinesList.get(position));
    }

    @Override
    public int getItemCount() {
        return oldSubroutinesList.size();
    }

    public void setNewSubroutineList(List<Subroutines> newSubroutinesList) {
        DiffUtil.Callback DIFF_CALLBACK = new SubroutineDiffUtil(oldSubroutinesList, newSubroutinesList);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
        oldSubroutinesList.clear();
        oldSubroutinesList.addAll(newSubroutinesList);
        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(this);
    }

    public static class AnalysisParentViewHolder extends RecyclerView.ViewHolder {

        final RelativeLayout itemLayout;
        final TextView title, description;
        final Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin;

        public AnalysisParentViewHolder(@NonNull View itemView) {
            super(itemView);

            itemLayout = itemView.findViewById(R.id.adapter_analysis_parent_item);

            title = itemView.findViewById(R.id.adapter_analysis_parent_item_title);
            description = itemView.findViewById(R.id.adapter_analysis_parent_item_description);

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_cloud_selector);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_amethyst_selector);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_sunflower_selector);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_nephritis_selector);
            bright_sky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_brightsky_blue_selector);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_alzarin_selector);
        }

        public void bindSubroutine(Subroutines subroutine) {
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

            title.setText(subroutine.getSubroutine());
            description.setText(subroutine.getDescription());
        }
    }
}
