package com.habitdev.sprout.ui.menu.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.databinding.FragmentHomeBinding;
import com.habitdev.sprout.ui.menu.home.adapter.HomeParentItemAdapter;
import com.habitdev.sprout.ui.menu.home.ui.HomeItemOnClickFragment;
import com.habitdev.sprout.ui.menu.home.ui.dialog.HomeOnFabClickDialogFragment;
import com.habitdev.sprout.ui.menu.home.ui.dialog.HomeParentItemAdapterModifyDialogFragment;
import com.habitdev.sprout.ui.menu.home.ui.fab_.custom_.AddNewHabitFragment;
import com.habitdev.sprout.ui.menu.home.ui.fab_.predefined_.AddDefaultHabitFragment;

import java.util.List;

public class HomeFragment extends Fragment
        implements
        HomeParentItemAdapter.HomeParentItemOnClickListener,
        AddDefaultHabitFragment.onAddDefaultReturnHome,
        AddNewHabitFragment.onAddNewHabitReturnHome,
        HomeItemOnClickFragment.onItemOnClickReturnHome {

    private FragmentHomeBinding binding;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private List<Habits> habitsList;

    private AddDefaultHabitFragment addDefaultHabitFragment = new AddDefaultHabitFragment();
    private AddNewHabitFragment addHabitHomeFragment = new AddNewHabitFragment();
    private HomeItemOnClickFragment homeItemOnClickFragment = new HomeItemOnClickFragment();
    private HomeParentItemAdapter homeParentItemAdapter = new HomeParentItemAdapter();

    public HomeFragment() {
        addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);
        addHabitHomeFragment.setmOnReturnHome(this);
        homeItemOnClickFragment.setmOnItemOnClickReturnHome(this);
        homeParentItemAdapter.setHomeParentItemOnclickListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        setRecyclerViewAdapter();

        binding.homeSwipeRefreshLayout.setOnRefreshListener(() -> {
            Toast.makeText(requireContext(), "Home Refresh, For Online Data Fetch", Toast.LENGTH_SHORT).show();
            binding.homeSwipeRefreshLayout.setRefreshing(false);
        });

        fabVisibility();
        onBackPress();

        return binding.getRoot();
    }

    private void setRecyclerViewAdapter() {
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        habitsList = habitWithSubroutinesViewModel.getAllHabitOnReform();
        homeParentItemAdapter.setOldHabitList(habitsList);

        binding.homeRecyclerView.setAdapter(homeParentItemAdapter);
        setEmptyRVBackground(homeParentItemAdapter);

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall);
        binding.homeRecyclerView.setLayoutAnimation(animationController);

        recyclerViewObserver(homeParentItemAdapter);
        recyclerViewItemTouchHelper(homeParentItemAdapter);
    }

    private void setEmptyRVBackground(HomeParentItemAdapter adapter){
        if (adapter.getItemCount() > 0){
            binding.homeEmptyLottieRecyclerView.setVisibility(View.INVISIBLE);
            binding.homeEmptyLbl.setVisibility(View.INVISIBLE);
        } else {
            binding.homeEmptyLottieRecyclerView.setVisibility(View.VISIBLE);
            binding.homeEmptyLbl.setVisibility(View.VISIBLE);
        }
    }

    private void recyclerViewItemTouchHelper(HomeParentItemAdapter homeParentItemAdapter) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.END);
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
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.END) {
                    Habits habits = habitsList.get(viewHolder.getBindingAdapterPosition());
                    habits.setOnReform(false);
                    habitWithSubroutinesViewModel.updateHabit(habits);
                    homeParentItemAdapter.cancelTimer();
                    homeParentItemAdapter.notifyItemRemoved(viewHolder.getBindingAdapterPosition());
                    Toast.makeText(requireActivity(), "Habit not on reform anymore", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Attach on Recycler View
        itemTouchHelper.attachToRecyclerView(binding.homeRecyclerView);
    }

    private void fabVisibility() {
        habitWithSubroutinesViewModel.getGetHabitOnReformCount().observe(getViewLifecycleOwner(), count -> {
            if (count <= 1) {
                binding.homeFab.setVisibility(View.VISIBLE);
                binding.homeFab.setClickable(true);
                FabButton();
            } else {
                binding.homeFab.setVisibility(View.GONE);
                binding.homeFab.setClickable(false);
            }
        });
    }

    private void recyclerViewObserver(HomeParentItemAdapter homeParentItemAdapter) {
        habitWithSubroutinesViewModel.getAllHabitOnReformLiveData().observe(getViewLifecycleOwner(), habits -> {
            homeParentItemAdapter.setNewHabitList(habits);
            habitsList = habits;
            setEmptyRVBackground(homeParentItemAdapter);
        });
    }

    @Override
    public void onItemClick(int position) {
        homeItemOnClickFragment.setHabit(habitsList.get(position));
        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(HomeFragment.this.getTag())
                .replace(binding.homeFrameLayout.getId(), homeItemOnClickFragment)
                .commit();
        binding.homeContainer.setVisibility(View.GONE);
    }

    @Override
    public void onClickHabitModify(Habits habit) {
        HomeParentItemAdapterModifyDialogFragment dialog = new HomeParentItemAdapterModifyDialogFragment(habitWithSubroutinesViewModel, habit);
        dialog.setTargetFragment(getChildFragmentManager().findFragmentById(HomeFragment.this.getId()), 1);
        dialog.show(getChildFragmentManager(), "Modify Habit Dialog");
    }

    @Override
    public void onClickHabitRelapse(Habits habit) {
        habit.setRelapse(habit.getRelapse() + 1);
        habitWithSubroutinesViewModel.updateHabit(habit);
    }

    @Override
    public void onClickHabitDrop(Habits habit) {
        habit.setOnReform(false);
        habitWithSubroutinesViewModel.updateHabit(habit);
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Handle on Back press
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void FabButton() {
        binding.homeFab.setOnClickListener(view -> {
            HomeOnFabClickDialogFragment dialog = new HomeOnFabClickDialogFragment();
            dialog.setTargetFragment(getChildFragmentManager()
                    .findFragmentById(HomeFragment.this.getId()), 1);
            dialog.show(getChildFragmentManager(), "HomeFabOnClickDialog");

            dialog.setOnClickListener(new HomeOnFabClickDialogFragment.onClickListener() {
                @Override
                public void onPredefinedClick() {
                    getChildFragmentManager()
                            .beginTransaction()
                            .addToBackStack(HomeFragment.this.getTag())
                            .add(binding.homeFrameLayout.getId(), addDefaultHabitFragment)
                            .commit();
                    binding.homeContainer.setVisibility(View.GONE);
                }

                @Override
                public void onUserDefineClick() {
                    getChildFragmentManager()
                            .beginTransaction()
                            .addToBackStack(HomeFragment.this.getTag())
                            .add(binding.homeFrameLayout.getId(), addHabitHomeFragment)
                            .commit();
                    binding.homeContainer.setVisibility(View.GONE);
                }
            });
        });
    }

    @Override
    public void onAddDefaultHabitClickReturnHome() {
        removeChildFragment(addDefaultHabitFragment);
    }

    @Override
    public void onAddNewHabitClickReturnHome() {
        removeChildFragment(addHabitHomeFragment);
    }

    @Override
    public void onHomeItemOnClickReturnHome() {
        removeChildFragment(homeItemOnClickFragment);
    }

    private void removeChildFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
        binding.homeContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        addDefaultHabitFragment.setmOnAddDefaultReturnHome(null);
//        addHabitHomeFragment.setmOnReturnHome(null);
//        homeItemOnClickFragment.setmOnItemOnClickReturnHome(null);
//        homeParentItemAdapter.setHomeParentItemOnclickListener(null);
        habitWithSubroutinesViewModel = null;
        habitsList = null;
        binding = null;

    }
}