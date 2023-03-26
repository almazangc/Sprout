package com.habitdev.sprout.ui.menu.subroutine.adapter;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.firestore.SubroutineFireStoreViewModel;
import com.habitdev.sprout.database.habit.model.firestore.SubroutineFireStore;
import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.database.habit.model.room.Subroutines;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.enums.TimeMilestone;
import com.habitdev.sprout.utill.DateTimeElapsedUtil;
import com.habitdev.sprout.utill.SubroutineDiffUtil;

import java.util.List;

public class SubroutineChildItemAdapter extends RecyclerView.Adapter<SubroutineChildItemAdapter.ChildItemViewHolder> {

    private List<Subroutines> oldSubroutineList;
    private List<SubroutineFireStore> oldSubroutineFireStoreList;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private SubroutineFireStoreViewModel subroutineFireStoreViewModel;
    private final boolean isCustomHabit;

    public SubroutineChildItemAdapter(boolean modifiable) {
        isCustomHabit = modifiable;
    }

    public void setHabitWithSubroutinesViewModel(HabitWithSubroutinesViewModel habitWithSubroutinesViewModel) {
        this.habitWithSubroutinesViewModel = habitWithSubroutinesViewModel;
    }

    public void setSubroutineFireStoreViewModel(SubroutineFireStoreViewModel subroutineFireStoreViewModel) {
        this.subroutineFireStoreViewModel = subroutineFireStoreViewModel;
    }

    public void setOldSubroutineList(List<Subroutines> oldSubroutineList) {
        this.oldSubroutineList = oldSubroutineList;
    }

    public void setOldSubroutineFireStoreList(List<SubroutineFireStore> oldSubroutineFireStoreList) {
        this.oldSubroutineFireStoreList = oldSubroutineFireStoreList;
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
        holder.bindData(oldSubroutineList.get(position), habitWithSubroutinesViewModel, subroutineFireStoreViewModel, oldSubroutineFireStoreList);

        Subroutines subroutine = oldSubroutineList.get(position);
        Habits habit = habitWithSubroutinesViewModel.getHabitByUID(subroutine.getFk_habit_uid());

        DateTimeElapsedUtil dateTimeElapsedUtil = new DateTimeElapsedUtil(habit.getDate_started());
        dateTimeElapsedUtil.calculateElapsedDateTime();

        if (dateTimeElapsedUtil.getElapsed_day() >= TimeMilestone.MIN_HABIT_BREAK_DAY.getDays()) {
            if (!isCustomHabit) {
                holder.upVote.setVisibility(View.VISIBLE);
                holder.downVote.setVisibility(View.VISIBLE);
            } else {
                holder.upVote.setVisibility(View.GONE);
                holder.downVote.setVisibility(View.GONE);
            }
        } else {
            holder.upVote.setVisibility(View.GONE);
            holder.downVote.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return oldSubroutineList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setNewSubroutineList(List<Subroutines> newSubroutineList) {
        DiffUtil.Callback DIFF_CALLBACK = new SubroutineDiffUtil(oldSubroutineList, newSubroutineList);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
        this.oldSubroutineList.clear();
        this.oldSubroutineList.addAll(newSubroutineList);
        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(this);
    }

    public static class ChildItemViewHolder extends RecyclerView.ViewHolder {

        final RelativeLayout itemLayout;
        final TextView subroutineTitle, subroutineDescription;
        final Button upVote, downVote, markAsDone;
        final Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin, markedAsDone, unMarkAsDone;

        public ChildItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.subroutine_child_item_layout);
            subroutineTitle = itemView.findViewById(R.id.subroutine);
            subroutineDescription = itemView.findViewById(R.id.home_item_on_click_habit_description);

            upVote = itemView.findViewById(R.id.btn_upvote_subroutine);
            downVote = itemView.findViewById(R.id.btn_downvote_subroutine);
            markAsDone = itemView.findViewById(R.id.mark_as_done);

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_cloud_selector);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_amethyst_selector);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_sunflower_selector);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_nephritis_selector);
            bright_sky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_brightsky_blue_selector);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_alzarin_selector);

            markedAsDone = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_done);
            unMarkAsDone = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_not_done);
        }

        void bindData(Subroutines subroutine, HabitWithSubroutinesViewModel habitWithSubroutinesViewModel, SubroutineFireStoreViewModel subroutineFireStoreViewModel, List<SubroutineFireStore> subroutineFireStoreList) {

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

            isMarkedAsDone(subroutine.isMarkDone());

            itemLayout.setOnClickListener(view -> {
                Habits habit = habitWithSubroutinesViewModel.getHabitByUID(subroutine.getFk_habit_uid());

                if (markAsDone.getBackground() == markedAsDone) {
                    isMarkedAsDone(false);
                    subroutine.setMarkDone(false);
                    subroutine.setTotal_completed(subroutine.getTotal_completed() - 1);

                    habit.setCompleted_subroutine(habit.getCompleted_subroutine() - 1);
                } else if (markAsDone.getBackground() == unMarkAsDone) {
                    isMarkedAsDone(true);
                    subroutine.setMarkDone(true);
                    subroutine.setTotal_completed(subroutine.getTotal_completed() + 1);

                    habit.setCompleted_subroutine(habit.getCompleted_subroutine() + 1);
                }

                habitWithSubroutinesViewModel.updateSubroutine(subroutine);
                habitWithSubroutinesViewModel.updateHabit(habit);
            });

            subroutineTitle.setText(subroutine.getSubroutine());
            subroutineDescription.setText(subroutine.getDescription());

            upVote.setOnClickListener(view -> {

                SubroutineFireStore subroutineFireStore = new SubroutineFireStore();

                if (subroutineFireStoreList != null) {
                    for (SubroutineFireStore subroutineFireStoreItem : subroutineFireStoreList) {
                        if (subroutineFireStoreItem.getFk_habit_uid() == subroutineFireStore.getFk_habit_uid() && subroutineFireStoreItem.getTitle().equals(subroutine.getSubroutine())) {
                            subroutineFireStore = subroutineFireStoreItem;
                            break;
                        }
                    }

                    Log.d("tag", "bindData: " + subroutineFireStore.toString());

                    if (subroutineFireStore != null){
                        subroutine.setUpvote(subroutineFireStore.getUpvote());
                        subroutine.setDownvote(subroutineFireStore.getDownvote());
                        habitWithSubroutinesViewModel.updateSubroutine(subroutine);
                    }
                }

                Toast toast = Toast.makeText(itemView.getContext(), "", Toast.LENGTH_SHORT);
                switch (subroutine.getVote_status()) {
                    case 0:
                        if (subroutineFireStore.getTitle() != null) {
                            subroutineFireStore.setUpvote(subroutineFireStore.getUpvote() + 1);
                            subroutineFireStoreViewModel.updateSubroutine(subroutineFireStore);
                        }

                        subroutine.setUpvote(subroutine.getUpvote() + 1);
                        subroutine.setVote_status(1);
                        habitWithSubroutinesViewModel.updateSubroutine(subroutine);
                        toast.setText("Upvoted");
                        toast.show();
                        break;
                    case 1:
                        toast.setText("Already upvoted");
                        toast.show();
                        break;
                    case -1:
                        if (subroutineFireStore.getTitle() != null) {
                            subroutineFireStore.setDownvote(subroutineFireStore.getDownvote() - 1);
                            subroutineFireStoreViewModel.updateSubroutine(subroutineFireStore);
                        }

                        subroutine.setDownvote(subroutine.getDownvote() - 1);
                        subroutine.setVote_status(0);
                        habitWithSubroutinesViewModel.updateSubroutine(subroutine);
                        toast.setText("Downvote was removed");
                        toast.show();
                        break;
                }
            });


            downVote.setOnClickListener(view -> {

                SubroutineFireStore subroutineFireStore = new SubroutineFireStore();

                if (subroutineFireStoreList != null) {
                    for (SubroutineFireStore subroutineFireStoreItem : subroutineFireStoreList) {
                        if (subroutineFireStoreItem.getFk_habit_uid() == subroutineFireStore.getFk_habit_uid() && subroutineFireStoreItem.getTitle().equals(subroutine.getSubroutine())) {
                            subroutineFireStore = subroutineFireStoreItem;
                            break;
                        }
                    }

                    Log.d("tag", "bindData: " + subroutineFireStore.toString());

                    if (subroutineFireStore != null){
                        subroutine.setUpvote(subroutineFireStore.getUpvote());
                        subroutine.setDownvote(subroutineFireStore.getDownvote());
                        habitWithSubroutinesViewModel.updateSubroutine(subroutine);
                    }
                }

                Toast toast = Toast.makeText(itemView.getContext(), "", Toast.LENGTH_SHORT);
                switch (subroutine.getVote_status()) {
                    case 0:
                        if (subroutineFireStore.getTitle() != null) {
                            subroutineFireStore.setDownvote(subroutineFireStore.getDownvote() + 1);
                            subroutineFireStoreViewModel.updateSubroutine(subroutineFireStore);
                        }

                        subroutine.setDownvote(subroutine.getDownvote() + 1);
                        subroutine.setVote_status(-1);
                        habitWithSubroutinesViewModel.updateSubroutine(subroutine);
                        toast.setText("Downvoted");
                        toast.show();
                        break;
                    case 1:
                        if (subroutineFireStore.getTitle() != null) {
                            subroutineFireStore.setUpvote(subroutineFireStore.getUpvote() - 1);
                            subroutineFireStoreViewModel.updateSubroutine(subroutineFireStore);
                        }

                        subroutine.setUpvote(subroutine.getUpvote() - 1);
                        subroutine.setVote_status(0);
                        habitWithSubroutinesViewModel.updateSubroutine(subroutine);
                        toast.setText("Upvote was removed");
                        toast.show();
                        break;
                    case -1:
                        toast.setText("Already downvoted");
                        toast.show();
                        break;
                }
            });
        }

        void isMarkedAsDone(boolean isMarkedAsDone) {
            if (isMarkedAsDone) {
                markAsDone.setBackground(markedAsDone);
                subroutineTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                subroutineDescription.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                markAsDone.setBackground(unMarkAsDone);
                subroutineTitle.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
                subroutineDescription.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            }
        }
    }
}
