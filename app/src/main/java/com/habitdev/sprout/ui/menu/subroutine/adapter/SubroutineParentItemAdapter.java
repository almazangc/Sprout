package com.habitdev.sprout.ui.menu.subroutine.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentSubroutineBinding;
import com.habitdev.sprout.enums.AppColor;

import java.util.List;

public class SubroutineParentItemAdapter extends RecyclerView.Adapter<SubroutineParentItemAdapter.ParentItemViewHolder>{

    private List<Habits> habitsOnReform;
    private LifecycleOwner lifecycleOwner;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;

    public interface OnClickListener{
        void onModifySubroutine(Habits habit);
    }

    private OnClickListener mOnClickListener;

    public void setmOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public void setHabitWithSubroutinesViewModel(HabitWithSubroutinesViewModel habitWithSubroutinesViewModel) {
        this.habitWithSubroutinesViewModel = habitWithSubroutinesViewModel;
    }

    //for continues scroll loop
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

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
        long uid = holder.bindData(habitsOnReform.get(position), mOnClickListener);

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(holder.childRecycleView.getContext(), R.anim.layout_animation);
        holder.childRecycleView.setLayoutAnimation(animationController);
        holder.childRecycleView.setVisibility(View.GONE);

        List<Subroutines> habitWithSubroutines;

        habitWithSubroutines = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(uid);

        SubroutineChildItemAdapter childAdapterItem = new SubroutineChildItemAdapter(habitWithSubroutines);
        holder.childRecycleView.setAdapter(childAdapterItem);

        //Can be removed: for continuous flow of recycler
        holder.childRecycleView.setRecycledViewPool(viewPool);
        childAdapterItem.setHabitWithSubroutines(habitWithSubroutines);

        habitWithSubroutinesViewModel.getAllSubroutinesOnReformHabitLiveData(uid).observe(lifecycleOwner, childAdapterItem::setHabitWithSubroutines);
    }

    @Override
    public int getItemCount() {
        return habitsOnReform.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setHabitsOnReform(List<Habits> habitsOnReform) {
        this.habitsOnReform = habitsOnReform;
    }

    public class ParentItemViewHolder extends RecyclerView.ViewHolder {

        //Components Here
        CardView ItemCardView;
        Button HabitsTitle, ModifySubroutine;
        RecyclerView childRecycleView;
        ColorStateList cs_cloud, cs_amethyst, cs_sunflower, cs_nephritis, cs_bright_sky_blue, cs_alzarin;

        public ParentItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemCardView = itemView.findViewById(R.id.subroutine_parent_item_card_view);
            HabitsTitle = itemView.findViewById(R.id.subroutine_parent_item_habit_title);
            ModifySubroutine = itemView.findViewById(R.id.subroutine_parent_item_modify_subroutine);
            childRecycleView = itemView.findViewById(R.id.subroutine_child_recyclerview);

            cs_cloud = ContextCompat.getColorStateList(itemView.getContext(), R.color.CLOUDS);
            cs_amethyst = ContextCompat.getColorStateList(itemView.getContext(), R.color.AMETHYST);
            cs_sunflower = ContextCompat.getColorStateList(itemView.getContext(), R.color.SUNFLOWER);
            cs_nephritis = ContextCompat.getColorStateList(itemView.getContext(), R.color.NEPHRITIS);
            cs_bright_sky_blue = ContextCompat.getColorStateList(itemView.getContext(), R.color.BRIGHT_SKY_BLUE);
            cs_alzarin = ContextCompat.getColorStateList(itemView.getContext(), R.color.ALIZARIN);
        }

        long bindData(Habits habit, OnClickListener onClickListener) {

            if (habit.getColor().equals(AppColor.ALZARIN.getColor())){
                ItemCardView.setBackgroundTintList(cs_alzarin);
            } else if (habit.getColor().equals(AppColor.AMETHYST.getColor())){
                ItemCardView.setBackgroundTintList(cs_amethyst);
            } else if (habit.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())){
                ItemCardView.setBackgroundTintList(cs_bright_sky_blue);
            } else if (habit.getColor().equals(AppColor.NEPHRITIS.getColor())){
                ItemCardView.setBackgroundTintList(cs_nephritis);
            } else if (habit.getColor().equals(AppColor.SUNFLOWER.getColor())){
                ItemCardView.setBackgroundTintList(cs_sunflower);
            } else {
                ItemCardView.setBackgroundTintList(cs_cloud);
            }

            String buttonLabel = habit.getHabit() + " [ " + habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid()).size() + " ]";

            HabitsTitle.setText(buttonLabel);
            HabitsTitle.setOnClickListener(view -> {
                childRecycleView.getVisibility();
                if (childRecycleView.getVisibility() == View.GONE) {
                    childRecycleView.setVisibility(View.VISIBLE);
                } else {
                    childRecycleView.setVisibility(View.GONE);
                }
            });

            if (habit.isModifiable()){
                ModifySubroutine.setOnClickListener(view -> {
                    onClickListener.onModifySubroutine(habit);
                    setOnDestroyAdapter();
                });
            } else {
                ModifySubroutine.setVisibility(View.GONE);
            }

            return habit.getPk_habit_uid();
        }

        void setOnDestroyAdapter(){
            habitWithSubroutinesViewModel.getAllHabitOnReformLiveData().removeObservers(lifecycleOwner);
            habitWithSubroutinesViewModel = null;
            lifecycleOwner = null;
        }
    }
}
