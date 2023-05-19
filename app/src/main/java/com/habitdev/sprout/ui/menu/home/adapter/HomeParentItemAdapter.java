package com.habitdev.sprout.ui.menu.home.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.enums.TimeMilestone;
import com.habitdev.sprout.utill.diffutils.DateTimeElapsedUtil;
import com.habitdev.sprout.utill.diffutils.HabitDiffUtil;

import java.util.List;
import java.util.Locale;

public class HomeParentItemAdapter extends RecyclerView.Adapter<HomeParentItemAdapter.HabitViewHolder> {

    private List<Habits> oldHabitList;
    private HomeParentItemOnClickListener homeParentItemOnclickListener;

    public HomeParentItemAdapter() {}

    public interface HomeParentItemOnClickListener {
        void onItemClick(int position);
        void onClickHabitModify(Habits habit, int position);
        void onClickHabitRelapse(Habits habit);
        void onClickHabitDrop(Habits habit);
        void onClickUpvoteHabit(Habits habit);
        void onClickDownvoteHabit(Habits habits);
    }

    public void setHomeParentItemOnclickListener(HomeParentItemOnClickListener homeParentItemOnclickListener) {
        this.homeParentItemOnclickListener = homeParentItemOnclickListener;
    }

    public void setOldHabitList(List<Habits> oldHabitList) {
        this.oldHabitList = oldHabitList;
    }

    @NonNull
    @Override
    public HomeParentItemAdapter.HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeParentItemAdapter.HabitViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_parent_habit_item, parent, false), homeParentItemOnclickListener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HomeParentItemAdapter.HabitViewHolder holder, int position) {
        holder.bindHabit(oldHabitList.get(position), homeParentItemOnclickListener);

        DateTimeElapsedUtil dateTimeElapsedUtil = new DateTimeElapsedUtil(oldHabitList.get(holder.getAbsoluteAdapterPosition()).getDate_started());
        dateTimeElapsedUtil.calculateElapsedDateTime();

        holder.runnable = new Runnable() {
            @Override
            public void run() {
                dateTimeElapsedUtil.calculateElapsedDateTime();
                holder.daysOfAbstinence.setText(dateTimeElapsedUtil.getResult());
                holder.handler.postDelayed(this, 1000);
            }
        };
        holder.handler.post(holder.runnable);

        holder.drop.setOnClickListener(v -> {
            try {
                homeParentItemOnclickListener.onClickHabitDrop(oldHabitList.get(holder.getAbsoluteAdapterPosition()));
                holder.handler.removeCallbacks(holder.runnable);
                holder.handler.removeCallbacks(holder.runnable); // use the saved runnable to remove the callbacks
                holder.handler.removeCallbacksAndMessages(null);
            } catch (Exception e) {
                Log.e("tag", "onBindViewHolder: exception", e);
            }
        });

        if (dateTimeElapsedUtil.getElapsed_day() >= TimeMilestone.MIN_HABIT_BREAK_DAY.getDays()) {
            if (!oldHabitList.get(holder.getAbsoluteAdapterPosition()).isModifiable()) {
                holder.upVote.setVisibility(View.VISIBLE);
                holder.downVote.setVisibility(View.VISIBLE);
            } else {
                holder.upVote.setVisibility(View.GONE);
                holder.downVote.setVisibility(View.GONE);
            }
            holder.drop.setText("More");
        } else {
            holder.upVote.setVisibility(View.GONE);
            holder.downVote.setVisibility(View.GONE);
            holder.drop.setText("Drop");
        }
    }

    @Override
    public void onViewRecycled(@NonNull HabitViewHolder holder) {
        super.onViewRecycled(holder);
        holder.handler.removeCallbacks(holder.runnable);
        holder.handler.removeCallbacksAndMessages(null); //handles leaking
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull HabitViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.handler.removeCallbacks(holder.runnable);
        holder.handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull HabitViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.handler.post(holder.runnable); // continue runnable when item on display
    }

    @Override
    public int getItemCount() {
        return oldHabitList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setNewHabitList(List<Habits> newHabitList) {
        DiffUtil.Callback DIFF_CALLBACK = new HabitDiffUtil(oldHabitList, newHabitList);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
        oldHabitList.clear();
        oldHabitList.addAll(newHabitList);
        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(this);
    }

    public static class HabitViewHolder extends RecyclerView.ViewHolder {

        final RelativeLayout itemContainer;
        final LinearLayout itemLayout;
        final TextView habitTitle, habitDescription, dateStarted, completedSubroutine, daysOfAbstinence, totalRelapse;
        final Button upVote, downVote, modify, relapse, drop;
        final Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin;
        Handler handler;
        Runnable runnable;

        public HabitViewHolder(@NonNull View itemView, HomeParentItemOnClickListener HomeParentItemOnclickListener) {
            super(itemView);

            itemContainer = itemView.findViewById(R.id.adapter_home_parent_item_container);
            itemLayout = itemContainer.findViewById(R.id.adapter_home_parent_item_layout);
            habitTitle = itemView.findViewById(R.id.home_item_on_click_habit_title);
            habitDescription = itemView.findViewById(R.id.home_item_on_click_habit_description);
            dateStarted = itemView.findViewById(R.id.date_started);
            completedSubroutine = itemView.findViewById(R.id.completed_subroutine);
            daysOfAbstinence = itemView.findViewById(R.id.home_item_on_click_habit_total_days_of_abstinence);
            totalRelapse = itemView.findViewById(R.id.total_relapse);

            upVote = itemView.findViewById(R.id.home_upvote_btn);
            downVote = itemView.findViewById(R.id.home_downvote_btn);

            modify = itemView.findViewById(R.id.adapter_home_parent_item_modify_btn);
            relapse = itemView.findViewById(R.id.adapter_home_parent_item_relapse_btn);
            drop = itemView.findViewById(R.id.adapter_home_parent_item_drop_btn);

            itemContainer.setOnClickListener(v -> {
                if (HomeParentItemOnclickListener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        HomeParentItemOnclickListener.onItemClick(position);
                    }
                }
            });

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_parent_item_view_cloud_selector);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_parent_item_view_amethyst_selector);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_parent_item_view_sunflower_selector);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_parent_item_view_nephritis_selector);
            bright_sky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_parent_item_view_brightsky_blue_selector);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_parent_item_view_alzarin_selector);

            handler = new Handler();
            //runnable
        }

        @SuppressLint("ClickableViewAccessibility")
        void bindHabit(Habits habit, HomeParentItemOnClickListener homeParentItemOnClickListener) {

            Habits habits = new Habits(habit);

            if (habits.getColor().equals(AppColor.ALZARIN.getColor())) {
                itemContainer.setBackground(alzarin);
            } else if (habits.getColor().equals(AppColor.AMETHYST.getColor())) {
                itemContainer.setBackground(amethyst);
            } else if (habits.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
                itemContainer.setBackground(bright_sky_blue);
            } else if (habits.getColor().equals(AppColor.NEPHRITIS.getColor())) {
                itemContainer.setBackground(nephritis);
            } else if (habits.getColor().equals(AppColor.SUNFLOWER.getColor())) {
                itemContainer.setBackground(sunflower);
            } else {
                itemContainer.setBackground(cloud);
            }

            itemContainer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                        itemContainer.setPadding(padding_inPx(10), padding_inPx(10), padding_inPx(0), padding_inPx(0));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL){
                       itemContainer.setPadding(padding_inPx(0), padding_inPx(0), padding_inPx(0), padding_inPx(0));
                    }
                    return false;
                }
            });

            habitTitle.setText(habits.getHabit());

            if (habits.getDescription().trim().isEmpty()) {
                habitDescription.setVisibility(View.GONE);
            } else {
                habitDescription.setText(habits.getDescription());
            }

            dateStarted.setText(habits.getDate_started());
            completedSubroutine.setText(String.valueOf(habits.getCompleted_subroutine()));
            totalRelapse.setText((String.format(Locale.getDefault(), "%d", habits.getRelapse())));

            upVote.setOnClickListener(view -> {
                homeParentItemOnClickListener.onClickUpvoteHabit(habits);
            });

            downVote.setOnClickListener(view -> {
                homeParentItemOnClickListener.onClickDownvoteHabit(habits);
            });

            if (habits.isModifiable()) {
                modify.setOnClickListener(view -> {
                    homeParentItemOnClickListener.onClickHabitModify(habits, getAbsoluteAdapterPosition());
                });
            } else {
                modify.setVisibility(View.GONE);
            }

            relapse.setOnClickListener(view -> {
                homeParentItemOnClickListener.onClickHabitRelapse(habits);
                totalRelapse.setText(String.valueOf(habits.getRelapse()));
            });
        }

        int padding_inPx (int dp){
            final float scale = itemView.getResources().getDisplayMetrics().density;
            return (int) (dp * scale + 0.5f);
        }
    }
}
