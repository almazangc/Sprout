package com.habitdev.sprout.ui.menu.home.adapter;

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
import com.habitdev.sprout.utill.SubroutineDiffUtil;

import java.util.List;

public class HomeAddDefaultHabitParentItemAdapter extends RecyclerView.Adapter<HomeAddDefaultHabitParentItemAdapter.SubroutinesViewHolder> {

    private List<Subroutines> oldSubroutineList;

    public HomeAddDefaultHabitParentItemAdapter() {}

    public void setOldSubroutineList(List<Subroutines> oldSubroutineList) {
        this.oldSubroutineList = oldSubroutineList;
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
        holder.bindSubroutine(oldSubroutineList.get(position));
    }

    @Override
    public int getItemCount() {
        return oldSubroutineList.size();
    }

    public void setNewSubroutineList(List<Subroutines> newSubroutineList) {
        DiffUtil.Callback DIFF_CALLBACK = new SubroutineDiffUtil(oldSubroutineList, newSubroutineList);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
        oldSubroutineList.clear();
        oldSubroutineList.addAll(newSubroutineList);
        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class SubroutinesViewHolder extends RecyclerView.ViewHolder {

        final RelativeLayout itemContainer;
        final TextView subroutine_title, subroutine_description;
        final Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin;

        public SubroutinesViewHolder(@NonNull View itemView) {
            super(itemView);
            itemContainer = itemView.findViewById(R.id.home_add_default_subroutine_child_item);
            subroutine_title = itemView.findViewById(R.id.home_add_default_subroutine_title);
            subroutine_description = itemView.findViewById(R.id.home_add_default_subroutine_description);

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_cloud_selector);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_amethyst_selector);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_sunflower_selector);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_nephritis_selector);
            bright_sky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_brightsky_blue_selector);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_alzarin_selector);
        }

        void bindSubroutine(Subroutines subroutine) {
            subroutine_title.setText(subroutine.getSubroutine());
            subroutine_description.setText(subroutine.getDescription());

            if (subroutine.getColor().equals(AppColor.ALZARIN.getColor())) {
                itemContainer.setBackground(alzarin);
            } else if (subroutine.getColor().equals(AppColor.AMETHYST.getColor())) {
                itemContainer.setBackground(amethyst);
            } else if (subroutine.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
                itemContainer.setBackground(bright_sky_blue);
            } else if (subroutine.getColor().equals(AppColor.NEPHRITIS.getColor())) {
                itemContainer.setBackground(nephritis);
            } else if (subroutine.getColor().equals(AppColor.SUNFLOWER.getColor())) {
                itemContainer.setBackground(sunflower);
            } else {
                itemContainer.setBackground(cloud);
            }
        }
    }
}
