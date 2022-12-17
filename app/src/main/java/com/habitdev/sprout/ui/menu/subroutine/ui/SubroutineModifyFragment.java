package com.habitdev.sprout.ui.menu.subroutine.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentSubroutineModifyBinding;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.ui.menu.subroutine.adapter.SubroutineModifyParentItemAdapter;
import com.habitdev.sprout.ui.menu.subroutine.adapter.SubroutineModifyParentOnclickListener;
import com.habitdev.sprout.ui.menu.subroutine.ui.dialog.SubroutineModifyParentItemAdapterDialogFragment;

import java.util.List;

public class SubroutineModifyFragment extends Fragment implements SubroutineModifyParentOnclickListener {

    private FragmentSubroutineModifyBinding binding;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private Habits habit;
    private SubroutineModifyParentItemAdapter adapter;

    public interface onClickBackPress {
        void returnSubroutineFragment();
    }

    private onClickBackPress mOnClickBackPress;

    public void setmOnClickBackPress(onClickBackPress mOnClickBackPress) {
        this.mOnClickBackPress = mOnClickBackPress;
    }

    public void setHabit(Habits habit) {
        this.habit = habit;
    }

    public SubroutineModifyFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubroutineModifyBinding.inflate(inflater, container, false);
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        setViewContent();
        onClickInsert();
        onBackPress();
        return binding.getRoot();
    }

    private void setViewContent() {
        setSubroutineRecyclerView();
        binding.subroutineModifyTitle.setText(habit.getHabit());
        setHabitTitleBackground();
    }

    private void setSubroutineRecyclerView() {
        adapter = new SubroutineModifyParentItemAdapter(habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid()));
        binding.subroutineModifyRecyclerView.setAdapter(adapter);
        adapter.setmSubroutineModifyOnclickListener(this);

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall);
        binding.subroutineModifyRecyclerView.setLayoutAnimation(animationController);

        habitWithSubroutinesViewModel
                .getAllSubroutinesOnReformHabitLiveData(habit.getPk_habit_uid())
                .observe(getViewLifecycleOwner(), adapter::setNewSubroutineList);
//        setItemTouchHelper(adapter);
    }

//    private void setItemTouchHelper(SubroutineModifyParentItemAdapter adapter) {
//
////        ItemTouchHelper itemTouchHelper_setup;
////        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.END) {
////            @Override
////            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
////                return false;
////            }
////
////            @Override
////            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
////                SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder VH;
////                VH = (SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder) viewHolder;
////                if (direction == ItemTouchHelper.END) {
//////                    if (mOnSwipeView != null) mOnSwipeView.itemOnSwipeView();
////
////                    if (habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid()).size() > 2) {
////                        Subroutines subroutine = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid()).get(viewHolder.getItemViewType());
////                        habitWithSubroutinesViewModel.deleteSubroutine(subroutine);
////                    } else {
////                        Toast.makeText(requireActivity(), "Required minimum of (2) subroutines", Toast.LENGTH_SHORT).show();
////                    }
////                    adapter.notifyItemChanged(VH.getAbsoluteAdapterPosition());
////                }
////            }
////        };
//
////        itemTouchHelper_setup =  new ItemTouchHelper(simpleCallback);
////        itemTouchHelper_setup.attachToRecyclerView(binding.subroutineModifyRecyclerView);
//
//        ItemTouchHelper.Callback itemTouchHelperCallback = new ItemTouchHelper.Callback() {
//
//            @Override
//            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//                return makeMovementFlags(0, ItemTouchHelper.END | ItemTouchHelper.START);
//            }
//
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public boolean isLongPressDragEnabled() {
//                return false;
//            }
//
//            @Override
//            public boolean isItemViewSwipeEnabled() {
//                return true;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//                SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder VH;
//
//                if (viewHolder instanceof SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder){
//                    VH = (SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder) viewHolder;
//
//                }
//
//                if (direction == ItemTouchHelper.END) {
////                    if (mOnSwipeView != null) mOnSwipeView.itemOnSwipeView();
//
//                    if (habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid()).size() > 2) {
//                        Subroutines subroutine = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid()).get(viewHolder.getItemViewType());
//                        habitWithSubroutinesViewModel.deleteSubroutine(subroutine);
//                    } else {
//                        Toast.makeText(requireActivity(), "Required minimum of (2) subroutines", Toast.LENGTH_SHORT).show();
//                    }
//                    adapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
//                }
//            }
//        };
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
//        itemTouchHelper.attachToRecyclerView(binding.subroutineModifyRecyclerView);
//    }

    private void setHabitTitleBackground() {

        final Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin;

        cloud = ContextCompat.getDrawable(requireContext(), R.drawable.background_topbar_view_cloud);
        amethyst = ContextCompat.getDrawable(requireContext(), R.drawable.background_top_bar_view_amethyst);
        sunflower = ContextCompat.getDrawable(requireContext(), R.drawable.background_topbar_view_sunflower);
        nephritis = ContextCompat.getDrawable(requireContext(), R.drawable.background_topbar_view_nephritis);
        bright_sky_blue = ContextCompat.getDrawable(requireContext(), R.drawable.background_topbar_view_brightsky_blue);
        alzarin = ContextCompat.getDrawable(requireContext(), R.drawable.background_topbar_view_alzarin);

        if (habit.getColor().equals(AppColor.ALZARIN.getColor())) {
            binding.subroutineModifyTitle.setBackground(alzarin);
        } else if (habit.getColor().equals(AppColor.AMETHYST.getColor())) {
            binding.subroutineModifyTitle.setBackground(amethyst);
        } else if (habit.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
            binding.subroutineModifyTitle.setBackground(bright_sky_blue);
        } else if (habit.getColor().equals(AppColor.NEPHRITIS.getColor())) {
            binding.subroutineModifyTitle.setBackground(nephritis);
        } else if (habit.getColor().equals(AppColor.SUNFLOWER.getColor())) {
            binding.subroutineModifyTitle.setBackground(sunflower);
        } else {
            binding.subroutineModifyTitle.setBackground(cloud);
        }
    }

    @Override
    public void onItemClick(int position) {

        final Subroutines subroutine = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid()).get(position);

        SubroutineModifyParentItemAdapterDialogFragment dialog = new SubroutineModifyParentItemAdapterDialogFragment(subroutine);

        dialog.setTargetFragment(getChildFragmentManager()
                .findFragmentById(SubroutineModifyFragment.this.getId()), 1);
        dialog.show(getChildFragmentManager(), "ModifySubroutineOnClickDialog");

        dialog.setmOnUpdateClickListener(new SubroutineModifyParentItemAdapterDialogFragment.OnUpdateClickListener() {
            @Override
            public void onClickUpdate(Subroutines subroutine) {
                habitWithSubroutinesViewModel.updateSubroutine(subroutine);
            }
        });
    }

    @Override
    public void onItemDelete(int position) {
        if (habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid()).size() > 2) {
            final Subroutines subroutine = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid()).get(position);
            habitWithSubroutinesViewModel.deleteSubroutine(subroutine);
            updateTotalSubroutine();
        } else {
            Toast.makeText(requireActivity(), "Required minimum of (2) subroutines", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyItemChanged(position);
    }

    private void onClickInsert() {
        binding.subroutineModifyInsertSubroutineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireActivity(), "Insert New", Toast.LENGTH_SHORT).show();
                SubroutineModifyParentItemAdapterDialogFragment dialog = new SubroutineModifyParentItemAdapterDialogFragment();
                dialog.setTargetFragment(getChildFragmentManager()
                        .findFragmentById(SubroutineModifyFragment.this.getId()), 1);
                dialog.show(getChildFragmentManager(), "ModifySubroutineOnClickDialog");
                dialog.setmOnInsertClickListener(new SubroutineModifyParentItemAdapterDialogFragment.OnInsertClickListener() {
                    @Override
                    public void onClickInsert(Subroutines subroutines) {
                        subroutines.setFk_habit_uid(habit.getPk_habit_uid());
                        habitWithSubroutinesViewModel.insertSubroutine(subroutines);
                        updateTotalSubroutine();
                    }
                });
            }
        });
    }

    private void updateTotalSubroutine() {
        final List<Subroutines> subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid());
        habit.setTotal_subroutine(subroutinesList.size());
        habitWithSubroutinesViewModel.updateHabit(habit);
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOnClickBackPress != null) mOnClickBackPress.returnSubroutineFragment();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        habitWithSubroutinesViewModel = null;
        adapter = null;
    }
}