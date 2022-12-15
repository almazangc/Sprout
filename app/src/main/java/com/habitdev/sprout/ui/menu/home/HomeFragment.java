package com.habitdev.sprout.ui.menu.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentHomeBinding;
import com.habitdev.sprout.ui.menu.home.adapter.HomeParentItemAdapter;
import com.habitdev.sprout.ui.menu.home.enums.ConfigurationKeys;
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
        HomeItemOnClickFragment.onItemOnClickReturnHome,
        HomeParentItemAdapterModifyDialogFragment.onHabitModifyListener {

    private FragmentHomeBinding binding;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private List<Habits> habitsList;

    private static AddDefaultHabitFragment addDefaultHabitFragment = new AddDefaultHabitFragment();
    private static AddNewHabitFragment addNewHabitHomeFragment = new AddNewHabitFragment();
    private static HomeItemOnClickFragment homeItemOnClickFragment = new HomeItemOnClickFragment();
    private final HomeParentItemAdapter homeParentItemAdapter = new HomeParentItemAdapter();

    private static boolean isOnAddDefault = false;
    private static boolean isOnAddNew = false;
    private static boolean isOnItemClick = false;
    private static boolean isOnModify = false;

    private static int position;
    private static Habits habitOnModify;
    private static Bundle savedInstanceState;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        if (savedInstanceState != null) HomeFragment.savedInstanceState = savedInstanceState;
        attachListener();
        setRecyclerViewAdapter();
        fabVisibility();
        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (savedInstanceState != null) {
            isOnAddDefault = savedInstanceState.getBoolean(ConfigurationKeys.IS_ON_ADD_DEFAULT.getValue());
            isOnAddNew = savedInstanceState.getBoolean(ConfigurationKeys.IS_ON_ADD_NEW.getValue());
            isOnItemClick = savedInstanceState.getBoolean(ConfigurationKeys.IS_ON_ITEM_CLICK.getValue());
            isOnModify = savedInstanceState.getBoolean(ConfigurationKeys.IS_ON_MODIFY.getValue());

            if (isOnAddDefault) {
                addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);
                changeFragment(addDefaultHabitFragment);
            }

            if (isOnAddNew) {
                addNewHabitHomeFragment.setmOnReturnHome(this);
                changeFragment(addNewHabitHomeFragment);
            }

            if (isOnItemClick){
                position = savedInstanceState.getInt(ConfigurationKeys.POSITION.getValue());
                onItemClick(position);
            }

            if (isOnModify) {
                habitOnModify = (Habits) (savedInstanceState).getSerializable(ConfigurationKeys.HABIT.getValue());
                position = savedInstanceState.getInt(ConfigurationKeys.POSITION.getValue());
                onClickHabitModify(habitOnModify, position);
            }
        }
    }

    private void attachListener() {
        addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);
        addNewHabitHomeFragment.setmOnReturnHome(this);
        homeItemOnClickFragment.setmOnItemOnClickReturnHome(this);
        homeParentItemAdapter.setHomeParentItemOnclickListener(this);
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
        setRefreshListener();
//        recyclerViewItemTouchHelper(homeParentItemAdapter);
    }

    private void setRefreshListener() {
        binding.homeSwipeRefreshLayout.setOnRefreshListener(() -> {
            Toast.makeText(requireContext(), "Home Refresh, For Online Data Fetch", Toast.LENGTH_SHORT).show();
            binding.homeSwipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setEmptyRVBackground(HomeParentItemAdapter adapter) {
        if (adapter.getItemCount() > 0) {
            binding.homeEmptyLottieRecyclerView.setVisibility(View.INVISIBLE);
            binding.homeEmptyLbl.setVisibility(View.INVISIBLE);
        } else {
            binding.homeEmptyLottieRecyclerView.setVisibility(View.VISIBLE);
            binding.homeEmptyLbl.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Control Number of habits can be added with a maximum of 2
     */
    private void fabVisibility() {
        binding.homeFab.setVisibility(View.VISIBLE);
        binding.homeFab.setClickable(true);
        FabButton();
//        habitWithSubroutinesViewModel.getGetHabitOnReformCount().observe(getViewLifecycleOwner(), count -> {
//            if (count <= 1) {
//                binding.homeFab.setVisibility(View.VISIBLE);
//                binding.homeFab.setClickable(true);
//                FabButton();
//            } else {
//                binding.homeFab.setVisibility(View.GONE);
//                binding.homeFab.setClickable(false);
//            }
//        });
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
        HomeFragment.position = position;
        isOnItemClick = true;

        homeItemOnClickFragment.setHabit(habitsList.get(position));
        homeItemOnClickFragment.setPosition(position);
        homeItemOnClickFragment.setAdapter_ref(homeParentItemAdapter);
        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(HomeFragment.this.getTag())
                .replace(binding.homeFrameLayout.getId(), homeItemOnClickFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN)
                .commit();
        binding.homeContainer.setVisibility(View.GONE);
    }

    @Override
    public void onClickHabitModify(Habits habit, int position) {
        isOnModify = true;
        habitOnModify = habit;
        HomeFragment.position = position;

        HomeParentItemAdapterModifyDialogFragment onModifydialogFragment = new HomeParentItemAdapterModifyDialogFragment(habit, position);
        onModifydialogFragment.setAdapter_ref(homeParentItemAdapter);
        onModifydialogFragment.setmOnHabitModifyListener(this);

        onModifydialogFragment.setTargetFragment(getChildFragmentManager().findFragmentById(HomeFragment.this.getId()), 1);
        onModifydialogFragment.show(getChildFragmentManager(), "Modify Habit Dialog");
    }

    @Override
    public void onDialogDismiss() {
        if (savedInstanceState != null)
            savedInstanceState.putBoolean(ConfigurationKeys.IS_ON_MODIFY.getValue(), false);
    }

    @Override
    public void onClickHabitRelapse(Habits habit) {
        habit.setRelapse(habit.getRelapse() + 1);
        habitWithSubroutinesViewModel.updateHabit(habit);
    }

    @Override
    public void onClickHabitDrop(Habits habit) {

        habit.setOnReform(false);
        habit.setRelapse(0);
        habit.setCompleted_subroutine(0);
        habitWithSubroutinesViewModel.updateHabit(habit);

        List<Subroutines> subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid());
        for (Subroutines subroutine : subroutinesList) {
            subroutine.setLongest_streak(0);
            subroutine.setMax_streak(0);
            subroutine.setMarkDone(false);
            subroutine.setTotal_skips(0);
            subroutine.setTotal_completed(0);
            habitWithSubroutinesViewModel.updateSubroutine(subroutine);
        }

        Snackbar.make(binding.getRoot(), Html.fromHtml("<b>" + habit.getHabit() + "</b>: all progress has been lost"), Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", view -> {
                })
                .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.PETER_RIVER))
                .setTextColor(getResources().getColor(R.color.NIGHT))
                .setBackgroundTint(getResources().getColor(R.color.CLOUDS))
                .show();
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().moveTaskToBack(true);
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
//                    List<Habits> habitsList = habitWithSubroutinesViewModel.getAllHabitListLiveData().getValue();
//                    List<Habits> availableHabits = new ArrayList<>();
//
//                    for (Habits habits : habitsList){
//                        if (!habits.isOnReform()) availableHabits.add(habits);
//                    }
//
//                    if (!availableHabits.isEmpty()) {
                    changeFragment(addDefaultHabitFragment);
                    isOnAddDefault = true;
//                    } else {
//                        Toast.makeText(requireActivity(), "No Available Habits", Toast.LENGTH_LONG).show();
//                    }
                }

                @Override
                public void onUserDefineClick() {
                    changeFragment(addNewHabitHomeFragment);
                    isOnAddNew = true;
                }
            });
        });
    }

    @Override
    public void onAddDefaultHabitClickReturnHome() {
        removeChildFragment(addDefaultHabitFragment);
        addDefaultHabitFragment.setmOnAddDefaultReturnHome(null);

        addDefaultHabitFragment = new AddDefaultHabitFragment();
        addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);

        if (savedInstanceState != null)
            savedInstanceState.putBoolean(ConfigurationKeys.IS_ON_ADD_DEFAULT.getValue(), false);

        isOnAddDefault = false;
    }

    @Override
    public void onAddNewHabitClickReturnHome() {
        removeChildFragment(addNewHabitHomeFragment);
        addNewHabitHomeFragment.setmOnReturnHome(null);
        addNewHabitHomeFragment = new AddNewHabitFragment();
        addNewHabitHomeFragment.setmOnReturnHome(this);
        if (savedInstanceState != null)
            savedInstanceState.putBoolean(ConfigurationKeys.IS_ON_ADD_NEW.getValue(), false);

        isOnAddNew = false;
    }

    @Override
    public void onHomeItemOnClickReturnHome() {
        removeChildFragment(homeItemOnClickFragment);
        homeItemOnClickFragment.setmOnItemOnClickReturnHome(null);
        homeItemOnClickFragment = new HomeItemOnClickFragment();
        homeItemOnClickFragment.setmOnItemOnClickReturnHome(this);
        if (savedInstanceState != null)
            savedInstanceState.putBoolean(ConfigurationKeys.IS_ON_ITEM_CLICK.getValue(), false);

        isOnItemClick = false;
    }

    private void removeChildFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .commit();
        binding.homeContainer.setVisibility(View.VISIBLE);
    }

    private void changeFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(HomeFragment.this.getTag())
                .add(binding.homeFrameLayout.getId(), fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN)
                .commit();
        binding.homeContainer.setVisibility(View.GONE);
    }

    /**
     * Handle Configuration Changes
     *
     * @param outState savedState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ConfigurationKeys.IS_ON_ADD_DEFAULT.getValue(), isOnAddDefault);
        outState.putBoolean(ConfigurationKeys.IS_ON_ADD_NEW.getValue(), isOnAddNew);

        outState.putBoolean(ConfigurationKeys.IS_ON_ITEM_CLICK.getValue(), isOnItemClick);
        if (isOnItemClick) {
            outState.putInt(ConfigurationKeys.POSITION.getValue(), position);
        }

        outState.putBoolean(ConfigurationKeys.IS_ON_MODIFY.getValue(), isOnModify);
        if (isOnModify) {
            outState.putSerializable(ConfigurationKeys.HABIT.getValue(), habitOnModify);
            outState.putInt(ConfigurationKeys.POSITION.getValue(), position);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.d("tag", "onPause: Home");
//        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(ConfigurationKeys.HOME_SHAREDPREF.getValue(), Context.MODE_PRIVATE);
//        sharedPreferences.edit()
//                .putBoolean(ConfigurationKeys.IS_ON_ADD_DEFAULT.getValue(), isOnAddDefault)
//                .putBoolean(ConfigurationKeys.IS_ON_ADD_NEW.getValue(), isOnAddNew)
//                .putBoolean(ConfigurationKeys.IS_ON_ITEM_CLICK.getValue(), isOnItemClick)
//                .putBoolean(ConfigurationKeys.IS_ON_MODIFY.getValue(), isOnModify)
//                .apply();

//        if (isOnModify) {
//            sharedPreferences.edit().putSerializable(ConfigurationKeys.HABIT.getValue(), habitOnModify);
//            outState.putInt(ConfigurationKeys.POSITION.getValue(), position);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.d("tag", "onResume: Home");
//        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(ConfigurationKeys.HOME_SHAREDPREF.getValue(), Context.MODE_PRIVATE);
//        isOnAddDefault = sharedPreferences.getBoolean(ConfigurationKeys.IS_ON_ADD_DEFAULT.getValue(), isOnAddDefault);
//        isOnAddNew = sharedPreferences.getBoolean(ConfigurationKeys.IS_ON_ADD_NEW.getValue(), isOnAddNew);
//        isOnItemClick = sharedPreferences.getBoolean(ConfigurationKeys.IS_ON_ITEM_CLICK.getValue(), isOnItemClick);
//        isOnModify = sharedPreferences.getBoolean(ConfigurationKeys.IS_ON_MODIFY.getValue(), isOnModify);
//
//
//        if (isOnAddDefault) {
//            try {
//                addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);
//                changeFragment(addDefaultHabitFragment);
//            } catch (Exception e){
//                //do nothing
//            }
//
//        }
//
//        if (isOnAddNew) {
//           try {
//               addNewHabitHomeFragment.setmOnReturnHome(this);
//               changeFragment(addNewHabitHomeFragment);
//           } catch (Exception e){
//               //do nothing
//           }
//        }
//
//        if (isOnItemClick) {
//            //do something
//        }
//
//        if (isOnModify) {
//            habitOnModify = (Habits) (savedInstanceState).getSerializable(ConfigurationKeys.HABIT.getValue());
//            position = savedInstanceState.getInt(ConfigurationKeys.POSITION.getValue());
//            onClickHabitModify(habitOnModify, position);
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        habitWithSubroutinesViewModel = null;
        habitsList = null;
        binding = null;
    }
}