package com.habitdev.sprout.ui.menu.home.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.interfaces.IRecyclerView;
import com.habitdev.sprout.ui.menu.home.ui.dialog.HomeParentItemAdapterModifyDialogFragment;
import com.habitdev.sprout.utill.DateTimeElapsedUtil;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class HomeParentItemAdapter extends RecyclerView.Adapter<HomeParentItemAdapter.HabitViewHolder> {

    private final com.habitdev.sprout.interfaces.IRecyclerView IRecyclerView;
    private final FragmentActivity fragmentActivity;
    private final FragmentManager fragmentManager;
    private final int HomeID;
    private List<Habits> habits;

    public HomeParentItemAdapter(List<Habits> habits, IRecyclerView IRecyclerView, FragmentActivity fragmentActivity, FragmentManager fragmentManager, int HomeID) {
        this.habits = habits;
        this.IRecyclerView = IRecyclerView;
        this.fragmentActivity = fragmentActivity;
        this.fragmentManager = fragmentManager;
        this.HomeID = HomeID;
    }

    @NonNull
    @Override
    public HomeParentItemAdapter.HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeParentItemAdapter.HabitViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_parent_habit_item, parent, false), IRecyclerView
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HomeParentItemAdapter.HabitViewHolder holder, int position) {
        holder.bindHabit(habits.get(position), fragmentActivity, fragmentManager, HomeID);
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setHabits(List<Habits> habits) {
        this.habits = habits;
        notifyDataSetChanged();
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemContainer;
        TextView habitHeader, habitDescription, dateStarted, completedSubroutine, daysOfAbstinence, totalRelapse;
        Button upVote, downVote, modify, relapse, drop;
        Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin;

        public HabitViewHolder(@NonNull View itemView, IRecyclerView IRecyclerView) {
            super(itemView);

            itemContainer = itemView.findViewById(R.id.adapter_home_parent_item_container);
            habitHeader = itemView.findViewById(R.id.header);
            habitDescription = itemView.findViewById(R.id.home_item_on_click_habit_description);
            dateStarted = itemView.findViewById(R.id.date_started);
            completedSubroutine = itemView.findViewById(R.id.completed_subroutine);
            daysOfAbstinence = itemView.findViewById(R.id.home_item_on_click_habit_total_days_of_abstinence);
            totalRelapse = itemView.findViewById(R.id.total_relapse);

            upVote = itemView.findViewById(R.id.home_upvote_btn);
            downVote = itemView.findViewById(R.id.home_downvote_btn);

            modify = itemView.findViewById(R.id.home_modify_btn);
            relapse = itemView.findViewById(R.id.home_relapse_btn);
            drop = itemView.findViewById(R.id.home_drop_btn);

            itemView.setOnClickListener(v -> {
                if (IRecyclerView != null) {
                    int position = getBindingAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        IRecyclerView.onItemClick(position);
                    }
                }
            });

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_parent_item_view_cloud);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_parent_item_view_amethyst);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_parent_item_view_sunflower);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_parent_item_view_nephritis);
            bright_sky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_parent_item_view_brightsky_blue);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_parent_item_view_alzarin);
        }

        void bindHabit(Habits habit, FragmentActivity fragmentActivity, FragmentManager fragmentManager, int HomeID) {

            if (habit.getColor().equals(AppColor.ALZARIN.getColor())) {
                itemContainer.setBackground(alzarin);
            } else if (habit.getColor().equals(AppColor.AMETHYST.getColor())) {
                itemContainer.setBackground(amethyst);
            } else if (habit.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
                itemContainer.setBackground(bright_sky_blue);
            } else if (habit.getColor().equals(AppColor.NEPHRITIS.getColor())) {
                itemContainer.setBackground(nephritis);
            } else if (habit.getColor().equals(AppColor.SUNFLOWER.getColor())) {
                itemContainer.setBackground(sunflower);
            } else {
                itemContainer.setBackground(cloud);
            }

            habitHeader.setText(habit.getHabit());

            if (habit.getDescription().trim().isEmpty()) {
                habitDescription.setVisibility(View.GONE);
            } else {
                habitDescription.setText(habit.getDescription());
            }

            dateStarted.setText(habit.getDate_started());
            completedSubroutine.setText(String.valueOf(habit.getCompleted_subroutine()));
            totalRelapse.setText((String.format(Locale.getDefault(), "%d", habit.getRelapse())));

            DateTimeElapsedUtil dateTimeElapsedUtil = new DateTimeElapsedUtil(habit.getDate_started());
            dateTimeElapsedUtil.calculateElapsedDateTime();

            //TODO: Functional But Leaking
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    fragmentActivity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            dateTimeElapsedUtil.calculateElapsedDateTime();
//                            daysOfAbstinence.setText(dateTimeElapsedUtil.getResult());
//                        }
//                    });
//                }
//            },0,1000);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            dateTimeElapsedUtil.calculateElapsedDateTime();
                            daysOfAbstinence.setText(dateTimeElapsedUtil.getResult());
                        }
                    });
                }
            }, 0, 1000);


            HabitWithSubroutinesViewModel habitWithSubroutinesViewModel = new ViewModelProvider(fragmentActivity).get(HabitWithSubroutinesViewModel.class);

            upVote.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "Upvote", Toast.LENGTH_SHORT).show();
            });

            downVote.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "DownVote", Toast.LENGTH_SHORT).show();
            });

            //TODO: SET on CLick Interface for refactor
            if (habit.isModifiable()) {
                modify.setOnClickListener(view -> {
                    HomeParentItemAdapterModifyDialogFragment dialog = new HomeParentItemAdapterModifyDialogFragment(habitWithSubroutinesViewModel, habit);
                    dialog.setTargetFragment(fragmentManager.findFragmentById(HomeID), 1);
                    dialog.show(fragmentManager, "Modify Habit Dialog");
                });
            } else {
                modify.setVisibility(View.GONE);
            }

            relapse.setOnClickListener(view -> {
                habit.setRelapse(habit.getRelapse() + 1);
                habitWithSubroutinesViewModel.updateHabit(habit);
                totalRelapse.setText(String.valueOf(habit.getRelapse()));
            });

            drop.setOnClickListener(view -> {
                habit.setOnReform(false);
                habitWithSubroutinesViewModel.updateHabit(habit);
            });
        }
    }
}
