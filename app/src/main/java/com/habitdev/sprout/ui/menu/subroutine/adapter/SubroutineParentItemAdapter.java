package com.habitdev.sprout.ui.menu.subroutine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentSubroutineBinding;
import com.habitdev.sprout.ui.menu.subroutine.ui.AddNewSubroutineFragment;

import java.util.List;

public class SubroutineParentItemAdapter extends RecyclerView.Adapter<SubroutineParentItemAdapter.ParentItemViewHolder> {

    private List<Habits> habitsOnReform;
    private LifecycleOwner lifecycleOwner;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private FragmentSubroutineBinding binding;

    //for continues scroll loop
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public SubroutineParentItemAdapter(ViewModelStoreOwner viewModelStoreOwner, LifecycleOwner lifecycleOwner, List<Habits> habitsOnReform, FragmentSubroutineBinding binding) {
        this.lifecycleOwner = lifecycleOwner;
        this.habitsOnReform = habitsOnReform;
        this.habitWithSubroutinesViewModel = new ViewModelProvider(viewModelStoreOwner).get(HabitWithSubroutinesViewModel.class);
        this.binding = binding;
    }

    @NonNull
    @Override
    public ParentItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ParentItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_subroutine_parent_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ParentItemViewHolder holder, int position) {
        long uid = holder.bindData(habitsOnReform.get(position));

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
        Button habitsOnReformTitle, modifySubroutineBtn;
        RecyclerView childRecycleView;

        public ParentItemViewHolder(@NonNull View itemView) {
            super(itemView);
            habitsOnReformTitle = itemView.findViewById(R.id.habitOnReform_title);
            modifySubroutineBtn = itemView.findViewById(R.id.modify_subroutine);
            childRecycleView = itemView.findViewById(R.id.subroutine_child_recyclerview);
        }

        long bindData(Habits habitOnReform) {
            //Setting Data
            String buttonLabel = habitOnReform.getHabit() + " [ " + habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habitOnReform.getPk_habit_uid()).size() + " ]";

            habitsOnReformTitle.setText(buttonLabel);
            habitsOnReformTitle.setOnClickListener(view -> {
                childRecycleView.getVisibility();
                if (childRecycleView.getVisibility() == View.GONE) {
                    childRecycleView.setVisibility(View.VISIBLE);
                } else {
                    childRecycleView.setVisibility(View.GONE);
                }
            });

            if (habitOnReform.isModifiable()){
                modifySubroutineBtn.setOnClickListener(view -> {
                    FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(binding.subroutineFrameLayout.getId(), new AddNewSubroutineFragment())
                            .commit();
                    binding.subroutineContainer.setVisibility(View.GONE);
                    setOnDestroyAdapter();
                });
            } else {
                modifySubroutineBtn.setVisibility(View.GONE);
            }

            return habitOnReform.getPk_habit_uid();
        }

        void setOnDestroyAdapter(){
            habitWithSubroutinesViewModel.getAllHabitOnReformLiveData().removeObservers(lifecycleOwner);
            habitWithSubroutinesViewModel = null;
            lifecycleOwner = null;
            binding = null;
        }
    }
}
