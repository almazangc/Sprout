package com.habitdev.sprout.ui.menu.subroutine.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.utill.HabitDiffUtil;

import java.util.List;

//User List Adapter
public class SubroutineParentItemAdapter extends RecyclerView.Adapter<SubroutineParentItemAdapter.ParentItemViewHolder> {

    private List<Habits> oldHabitList;
    protected HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    protected LifecycleOwner subroutineLifecycleOwner;

    public interface OnClickListener {
        void onModifySubroutine(Habits habit);
    }

    private OnClickListener mOnClickListener;

    public void setmOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }


    public void setOldHabitList(List<Habits> oldHabitList) {
        this.oldHabitList = oldHabitList;
    }

    public void setHabitWithSubroutinesViewModel(HabitWithSubroutinesViewModel habitWithSubroutinesViewModel) {
        this.habitWithSubroutinesViewModel = habitWithSubroutinesViewModel;
    }

    public void setSubroutineLifecycleOwner(LifecycleOwner subroutineLifecycleOwner) {
        this.subroutineLifecycleOwner = subroutineLifecycleOwner;
    }

    public SubroutineParentItemAdapter() {}

    @NonNull
    @Override
    public ParentItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ParentItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_subroutine_parent_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ParentItemViewHolder holder, int position) {

        long uid = holder.bindData(oldHabitList.get(position), mOnClickListener);

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(holder.childRecycleView.getContext(), R.anim.layout_animation_fall);
        holder.childRecycleView.setLayoutAnimation(animationController);

        List<Subroutines> habitWithSubroutines;

        habitWithSubroutines = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(uid);
        SubroutineChildItemAdapter childAdapterItem = new SubroutineChildItemAdapter();
        childAdapterItem.setOldSubroutineList(habitWithSubroutines);
        childAdapterItem.setHabitWithSubroutinesViewModel(habitWithSubroutinesViewModel);
        holder.childRecycleView.setAdapter(childAdapterItem);

        habitWithSubroutinesViewModel.getAllSubroutinesOnReformHabitLiveData(uid).observe(subroutineLifecycleOwner, childAdapterItem::setNewSubroutineList);

        setItemTouchHelper(holder, childAdapterItem);
    }

    private void setItemTouchHelper(SubroutineParentItemAdapter.ParentItemViewHolder holder, SubroutineChildItemAdapter childAdapterItem) {

        ItemTouchHelper itemTouchHelper;
        ItemTouchHelper.Callback itemTouchHelperCallback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.END | ItemTouchHelper.START);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction) {
                    case ItemTouchHelper.END:
                    case ItemTouchHelper.START:
                        SubroutineChildItemAdapter.ChildItemViewHolder childItemViewHolder;
                        childItemViewHolder = (SubroutineChildItemAdapter.ChildItemViewHolder) viewHolder;
                        childAdapterItem.notifyItemChanged(childItemViewHolder.getAbsoluteAdapterPosition());
                        break;
                }
            }
        };
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(holder.childRecycleView);
    }


    @Override
    public int getItemCount() {
        return oldHabitList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setNewHabitList(List<Habits> newHabitList) {
        DiffUtil.Callback DIFF_CALLBACK = new HabitDiffUtil(oldHabitList, newHabitList);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
        oldHabitList = newHabitList;
        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(this);
    }

    public static class ParentItemViewHolder extends RecyclerView.ViewHolder {

        final RelativeLayout itemContainer;
        final TextView HabitsTitle;
        final Button ModifySubroutine;
        final RecyclerView childRecycleView;
        final Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin;

        public ParentItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemContainer = itemView.findViewById(R.id.subroutine_parent_item_layout);
            HabitsTitle = itemView.findViewById(R.id.subroutine_parent_item_habit_title);
            ModifySubroutine = itemView.findViewById(R.id.subroutine_parent_item_modify_subroutine);
            childRecycleView = itemView.findViewById(R.id.subroutine_child_recyclerview);

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_cloud_selector);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_amethyst_selector);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_sunflower_selector);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_nephritis_selector);
            bright_sky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_brightsky_blue_selector);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_alzarin_selector);
        }

        @SuppressLint("ClickableViewAccessibility")
        long bindData(Habits habit, OnClickListener mOnClickListener) {

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

            HabitsTitle.setText(habit.getHabit());

            HabitsTitle.setPadding(padding_inPx(10), padding_inPx(0), padding_inPx(10), padding_inPx(5));

            itemContainer.setOnClickListener(view -> {
                childRecycleView.getVisibility();
                if (childRecycleView.getVisibility() == View.GONE) {
                    LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(childRecycleView.getContext(), R.anim.layout_animation_fall);
                    childRecycleView.setLayoutAnimation(animationController);
                    childRecycleView.setVisibility(View.VISIBLE);
                } else {
                    LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(childRecycleView.getContext(), R.anim.layout_animation_up);
                    childRecycleView.setLayoutAnimation(animationController);
                    if (!childRecycleView.isAnimating()) childRecycleView.setVisibility(View.GONE);
                }
            });

            itemContainer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        HabitsTitle.setPadding(padding_inPx(10), padding_inPx(5), padding_inPx(10), padding_inPx(0));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        HabitsTitle.setPadding(padding_inPx(10), padding_inPx(0), padding_inPx(10), padding_inPx(5));
                    }
                    return false;
                }
            });

            if (habit.isModifiable()) {
                ModifySubroutine.setOnClickListener(view -> {
                    mOnClickListener.onModifySubroutine(habit); //OnclickListener Here
                });
            } else {
                ModifySubroutine.setVisibility(View.GONE);
            }

            return habit.getPk_habit_uid();
        }

        int padding_inPx(int dp) {
            final float scale = itemContainer.getResources().getDisplayMetrics().density;
            return (int) (dp * scale + 0.5f);
        }
    }
}

