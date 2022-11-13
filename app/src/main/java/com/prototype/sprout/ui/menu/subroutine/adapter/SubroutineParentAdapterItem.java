package com.prototype.sprout.ui.menu.subroutine.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.prototype.sprout.R;
import com.prototype.sprout.database.habit.Habit;
import com.prototype.sprout.database.habit.HabitViewModel;
import com.prototype.sprout.database.sub_routine.Routine;
import com.prototype.sprout.database.sub_routine.RoutineViewModel;
import com.prototype.sprout.ui.menu.home.adapter.HomeParentAdapterItem;

import java.util.ArrayList;
import java.util.List;

public class SubroutineParentAdapterItem extends RecyclerView.Adapter<SubroutineParentAdapterItem.ParentItemViewHolder>{

    List<Habit> habitsOnReform;
    FragmentActivity fragmentActivity;

    public SubroutineParentAdapterItem(FragmentActivity fragmentActivity, List<Habit> habitsOnReform) {
        this.fragmentActivity = fragmentActivity;
        this.habitsOnReform = habitsOnReform;
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
        holder.bindData(habitsOnReform.get(position));

        //Algorithm Here to Populate ChildView
        //Get Current Habit Title on Bind
        //Get all of its subroutine
        //Set all subroutine, iteration

        List<Routine> routines = new ArrayList<>();
        routines.add(new Routine("Title1",  "Hello"));
        routines.add(new Routine("Title2",  "Hello"));
        routines.add(new Routine("Title3",  "Hello"));
        routines.add(new Routine("Title4",  "Hello"));

        holder.childRecycleView.setLayoutManager(new LinearLayoutManager(holder.childRecycleView.getContext(), LinearLayoutManager.VERTICAL, false));
        SubroutineChildAdapterItem childAdapterItem = new SubroutineChildAdapterItem(routines);
        holder.childRecycleView.setAdapter(childAdapterItem);
        childAdapterItem.setRoutinesOnReform(routines);

//        SubroutineChildAdapterItem childAdapterItem;
//        List<Routine> subroutinesOfHabitOnReform;
//        RoutineViewModel routineViewModel;
//
//        holder.childRecycleView.setLayoutManager(new LinearLayoutManager(holder.childRecycleView.getContext(), LinearLayoutManager.VERTICAL, false));
//        routineViewModel = new ViewModelProvider(fragmentActivity).get(RoutineViewModel.class);
//
//        subroutinesOfHabitOnReform = routineViewModel.getAllRoutineList();
//        subroutinesOfHabitOnReform = new SubroutineChildAdapterItem(subroutinesOfHabitOnReform);
//        binding.homeRecyclerView.setAdapter(homeParentAdapterItem);
//
//        habitViewModel.getAllHabitOnReformLiveData().observe(getViewLifecycleOwner(), habitsOnReform -> {
//            homeParentAdapterItem.setHabits(habitsOnReform);
//        });
//
//        // Create a layout manager
//        // to assign a layout
//        // to the RecyclerView.
//
//        // Here we have assigned the layout
//        // as LinearLayout with vertical orientation
//        LinearLayoutManager layoutManager = new LinearLayoutManager(ParentItemViewHolder.ChildRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
//
//        // Since this is a nested layout, so
//        // to define how many child items
//        // should be prefetched when the
//        // child RecyclerView is nested
//        // inside the parent RecyclerView,
//        // we use the following method
//        layoutManager.setInitialPrefetchItemCount(parentItem.getChildItemList().size());
//
//        // Create an instance of the child
//        // item view adapter and set its
//        // adapter, layout manager and RecyclerViewPool
//        SubroutineChildAdapterItem subroutineChildAdapterItem = new SubroutineChildAdapterItem(parentItem.getChildItemList());
//        ParentItemViewHolder.ChildRecyclerView.setLayoutManager(layoutManager);
//        parentViewHolder.ChildRecyclerView.setAdapter(childItemAdapter);
//        ParentItemViewHolder.ChildRecyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return habitsOnReform.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setHabitsOnReform(List<Habit> habitsOnReform) {
        this.habitsOnReform = habitsOnReform;
    }

    static class ParentItemViewHolder extends RecyclerView.ViewHolder {

        //Components Here
        Button habitsOnReformTitle;
        RecyclerView childRecycleView;

        public ParentItemViewHolder(@NonNull View itemView) {
            super(itemView);
            //Connecting ID
            habitsOnReformTitle = itemView.findViewById(R.id.habitOnReform_title);
            childRecycleView = itemView.findViewById(R.id.subroutine_child_recyclerview);
        }

        void bindData(Habit habitOnReform){
            //Setting Data
            habitsOnReformTitle.setText(habitOnReform.getHabit());
            habitsOnReformTitle.setOnClickListener(view -> {
                childRecycleView.getVisibility();
                switch (childRecycleView.getVisibility()){
                    case View.GONE:
                        childRecycleView.setVisibility(View.VISIBLE);
                        break;
                    default:
                        childRecycleView.setVisibility(View.GONE);
                        break;
                }
            });
        }
    }
}
