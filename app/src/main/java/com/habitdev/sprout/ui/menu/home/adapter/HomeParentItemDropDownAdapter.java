package com.habitdev.sprout.ui.menu.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.firestore.HabitFireStore;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;

import java.util.List;
import java.util.Locale;

public class HomeParentItemDropDownAdapter extends ArrayAdapter<String> {

    List<String> habitTitlesList;
    List<HabitFireStore> habitFireStoreList;
    HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;

    public HomeParentItemDropDownAdapter(@NonNull Context context, @NonNull List<String> habitTitlesList) {
        super(context, 0, habitTitlesList);
        this.habitTitlesList = habitTitlesList;
    }

    public void setHabitWithSubroutinesViewModel(HabitWithSubroutinesViewModel habitWithSubroutinesViewModel) {
        this.habitWithSubroutinesViewModel = habitWithSubroutinesViewModel;
    }

    public void setHabitFireStoreList(List<HabitFireStore> habitFireStoreList) {
        this.habitFireStoreList = habitFireStoreList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.adapter_add_default_parent_habit_item, parent, false);
        }

        TextView rating = itemView.findViewById(R.id.home_add_default_habit_rating);
        TextView habitTitle = itemView.findViewById(R.id.home_add_default_habit_title);

        habitTitle.setText(habitTitlesList.get(position));

        for (HabitFireStore habitFireStore : habitFireStoreList) {
            if (habitTitlesList.get(position).trim().equalsIgnoreCase(habitFireStore.getTitle().trim())) {
                if (habitFireStore.getRating() == 0) {
                    rating.setVisibility(View.GONE);
                } else {
                    rating.setVisibility(View.VISIBLE);
                    rating.setText(String.format(Locale.getDefault(), "%d", habitFireStore.getRating()));
                }
                break;
            }
        }
        return itemView;
    }

    @Override
    public int getCount() {
        return habitTitlesList.size();
    }
}
