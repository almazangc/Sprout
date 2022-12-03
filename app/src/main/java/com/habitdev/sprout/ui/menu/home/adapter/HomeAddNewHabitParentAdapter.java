package com.habitdev.sprout.ui.menu.home.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.enums.AppColor;

import java.util.List;

public class HomeAddNewHabitParentAdapter extends RecyclerView.Adapter<HomeAddNewHabitParentAdapter.AddNewHabitSubroutineViewHolder>{

    private List<Subroutines> subroutinesList;

    private OnClickListener onClickListener;

    public interface OnClickListener{
        void onDelete(Subroutines subroutine);
        void onModify(Subroutines subroutine);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public HomeAddNewHabitParentAdapter(List<Subroutines> subroutinesList) {
        this.subroutinesList = subroutinesList;
    }

    @NonNull
    @Override
    public HomeAddNewHabitParentAdapter.AddNewHabitSubroutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeAddNewHabitParentAdapter.AddNewHabitSubroutineViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_add_new_habit_parent_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAddNewHabitParentAdapter.AddNewHabitSubroutineViewHolder holder, int position) {
        holder.bindSubroutine(subroutinesList.get(position), onClickListener);
    }

    @Override
    public int getItemCount() {
        return subroutinesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @SuppressLint("NotifyDataSetChanged: Use ListAdapter")
    public void setSubroutinesList(List<Subroutines> subroutinesList) {
        this.subroutinesList = subroutinesList;
        Log.d("tag", "setSubroutinesList: " + subroutinesList.toString());
        notifyDataSetChanged();
    }

    public static class AddNewHabitSubroutineViewHolder extends RecyclerView.ViewHolder {

        View color_indicator;
        TextView title, description;
        Button modify, delete;
        Drawable cloud, alzarin, amethyst, bright_sky_blue, nephritis, sunflower;

        public AddNewHabitSubroutineViewHolder(@NonNull View itemView) {
            super(itemView);

            color_indicator = itemView.findViewById(R.id.adapter_home_add_new_habit_subroutine_color_indicator);
            title = itemView.findViewById(R.id.adapter_home_add_new_habit_subroutine_title);
            description = itemView.findViewById(R.id.adapter_home_add_new_habit_subroutine_description);
            modify = itemView.findViewById(R.id.adapter_home_add_new_habit_subroutine_modify_subroutine_btn);
            delete = itemView.findViewById(R.id.adapter_home_add_new_habit_subroutine_remove_subroutine_btn);

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_color_indicator_clouds);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_color_indicator_alzarin);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_color_indicator_amethyst);
            bright_sky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_color_indicator_brightsky_blue);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_color_indicator_nephritis);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_color_indicator_sunflower);
        }

        void bindSubroutine(Subroutines subroutine, OnClickListener onClickListener) {
            if (subroutine.getColor().equals(AppColor.ALZARIN.getColor())) {
                color_indicator.setBackground(alzarin);
            } else if (subroutine.getColor().equals(AppColor.AMETHYST.getColor())) {
                color_indicator.setBackground(amethyst);
            } else if (subroutine.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
                color_indicator.setBackground(bright_sky_blue);
            } else if (subroutine.getColor().equals(AppColor.NEPHRITIS.getColor())) {
                color_indicator.setBackground(nephritis);
            } else if (subroutine.getColor().equals(AppColor.SUNFLOWER.getColor())) {
                color_indicator.setBackground(sunflower);
            } else {
                color_indicator.setBackground(cloud);
            }

            title.setText(subroutine.getSubroutine());
            description.setText(subroutine.getDescription());

            if (subroutine.getModifiable()) {
                modify.setOnClickListener(view -> {
                    if (onClickListener != null) onClickListener.onModify(subroutine);
                });
            } else {
                modify.setVisibility(View.GONE);
            }

            delete.setOnClickListener(view -> {
                if (onClickListener != null) onClickListener.onDelete(subroutine);
            });
        }
    }
}