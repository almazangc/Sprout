package com.habitdev.sprout.ui.menu.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
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
import com.habitdev.sprout.enums.HomeConfigurationKeys;
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
        AddDefaultHabitFragment.OnAddDefaultReturnHome,
        AddNewHabitFragment.OnAddNewHabitReturnHome,
        HomeItemOnClickFragment.OnItemOnClickReturnHome,
        HomeParentItemAdapterModifyDialogFragment.OnHabitModifyListener {

    private static AddDefaultHabitFragment addDefaultHabitFragment = new AddDefaultHabitFragment();
    private static AddNewHabitFragment addNewHabitHomeFragment = new AddNewHabitFragment();
    private static HomeItemOnClickFragment homeItemOnClickFragment = new HomeItemOnClickFragment();
    private static boolean isOnAddDefault = false;
    private static boolean isOnAddNew = false;
    private static boolean isOnItemClick = false;
    private static boolean isOnModify = false;
    private static boolean isOnFabDialog = false;
    private static int position = 0;
    private static Habits habitOnModify = null;
    private static Bundle savedInstanceState = null;
    private final HomeParentItemAdapter homeParentItemAdapter = new HomeParentItemAdapter();
    private FragmentHomeBinding binding = null;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private List<Habits> habitsList = null;

    public HomeFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);
        addNewHabitHomeFragment.setmOnReturnHome(this);
        homeItemOnClickFragment.setmOnItemOnClickReturnHome(this);
        homeParentItemAdapter.setHomeParentItemOnclickListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            HomeFragment.savedInstanceState = savedInstanceState;
        }

        setRecyclerViewAdapter();
        fabVisibility();
        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            isOnAddDefault = savedInstanceState.getBoolean(HomeConfigurationKeys.IS_ON_ADD_DEFAULT.getValue());
            isOnAddNew = savedInstanceState.getBoolean(HomeConfigurationKeys.IS_ON_ADD_NEW.getValue());
            isOnItemClick = savedInstanceState.getBoolean(HomeConfigurationKeys.IS_ON_ITEM_CLICK.getValue());
            isOnModify = savedInstanceState.getBoolean(HomeConfigurationKeys.IS_ON_MODIFY.getValue());

            if (isOnAddDefault) {
                addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);
                if (!addDefaultHabitFragment.isAdded()) {
                    changeFragment(addDefaultHabitFragment);
                }
            }

            if (isOnAddNew) {
                addNewHabitHomeFragment.setmOnReturnHome(this);
                if (!addNewHabitHomeFragment.isAdded()) {
                    changeFragment(addNewHabitHomeFragment);
                }
            }

            if (isOnItemClick) {
                position = savedInstanceState.getInt(HomeConfigurationKeys.POSITION.getValue());
                onItemClick(position);
            }

            if (isOnModify) {
                habitOnModify = (Habits) (savedInstanceState).getSerializable(HomeConfigurationKeys.HABIT.getValue());
                position = savedInstanceState.getInt(HomeConfigurationKeys.POSITION.getValue());
                onClickHabitModify(habitOnModify, position);
            }

            if (isOnFabDialog) {
                displayFabDialog();
            }
        }
    }

    private void setRecyclerViewAdapter() {
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        habitsList = habitWithSubroutinesViewModel.getAllHabitOnReform();

        homeParentItemAdapter.setOldHabitList(habitsList);
        binding.homeRecyclerView.setAdapter(homeParentItemAdapter);

        setEmptyRVBackground(homeParentItemAdapter);

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall);
        binding.homeRecyclerView.setLayoutAnimation(animationController);

        setRecyclerViewObserver(homeParentItemAdapter);
        setRefreshListener();
//        recyclerViewItemTouchHelper(homeParentItemAdapter);
    }

    private void setRefreshListener() {
        binding.homeSwipeRefreshLayout.setOnRefreshListener(() -> {
            Toast.makeText(requireContext(), "Home Refresh, For Online Data Fetch", Toast.LENGTH_SHORT).show();
            binding.homeSwipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setEmptyRVBackground(@NonNull HomeParentItemAdapter adapter) {
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
//        Log.d(TAG, "fabVisibility: ");
        binding.homeFab.setVisibility(View.VISIBLE);
        binding.homeFab.setClickable(true);
        binding.homeFab.setOnClickListener(view -> displayFabDialog());
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

    private void setRecyclerViewObserver(@NonNull HomeParentItemAdapter homeParentItemAdapter) {
        habitWithSubroutinesViewModel.getAllHabitOnReformLiveData().observe(getViewLifecycleOwner(), habits -> {
            homeParentItemAdapter.setNewHabitList(habits);
            habitsList = habits;
            setEmptyRVBackground(homeParentItemAdapter); //adapts on ui changes
        });
    }

    @Override
    public void onItemClick(int position) {

        final int FRAGMENT_TRANSITION = !isOnItemClick ? FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN : FragmentTransaction.TRANSIT_NONE;

        HomeFragment.position = position;
        isOnItemClick = true;

        homeItemOnClickFragment.setHabit(habitsList.get(position));
        homeItemOnClickFragment.setPosition(position);
        homeItemOnClickFragment.setAdapter_ref(homeParentItemAdapter);

        if (!homeItemOnClickFragment.isAdded()) {
            getChildFragmentManager()
                    .beginTransaction()
                    .addToBackStack(HomeFragment.this.getTag())
                    .replace(binding.homeFrameLayout.getId(), homeItemOnClickFragment)
                    .setTransition(FRAGMENT_TRANSITION)
                    .commit();
        }
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
        if (savedInstanceState != null && !savedInstanceState.isEmpty())
            savedInstanceState.putBoolean(HomeConfigurationKeys.IS_ON_MODIFY.getValue(), false);
        isOnModify = false;
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
                    //Dismiss snack bar
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

    private void displayFabDialog() {

        isOnFabDialog = true;

        HomeOnFabClickDialogFragment dialog = new HomeOnFabClickDialogFragment();
        dialog.setTargetFragment(getChildFragmentManager()
                .findFragmentById(HomeFragment.this.getId()), 1);
        dialog.show(getChildFragmentManager(), "HomeFabOnClickDialog");

        dialog.setOnClickListener(new HomeOnFabClickDialogFragment.OnClickListener() {
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
                if (!addDefaultHabitFragment.isAdded()) {
                    changeFragment(addDefaultHabitFragment);
                }
                isOnAddDefault = true;
                isOnFabDialog = false;
//                    } else {
//                        Toast.makeText(requireActivity(), "No Available Habits", Toast.LENGTH_LONG).show();
//                    }
            }

            @Override
            public void onUserDefineClick() {
                if (!addNewHabitHomeFragment.isAdded()) {
                    changeFragment(addNewHabitHomeFragment);
                }
                isOnAddNew = true;
                isOnFabDialog = false;
            }

            @Override
            public void onDismissDialog() {
                isOnFabDialog = false;
            }
        });
    }

    @Override
    public void onAddDefaultHabitClickReturnHome() {
        removeChildFragment(addDefaultHabitFragment);
        addDefaultHabitFragment.setmOnAddDefaultReturnHome(null);
        addDefaultHabitFragment = new AddDefaultHabitFragment();
        addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);

        if (savedInstanceState != null ) {
            savedInstanceState.putBoolean(HomeConfigurationKeys.IS_ON_ADD_DEFAULT.getValue(), false);
        }

        isOnAddDefault = false;
    }

    @Override
    public void onAddNewHabitClickReturnHome() {
        removeChildFragment(addNewHabitHomeFragment);
        addNewHabitHomeFragment.setmOnReturnHome(null);
        addNewHabitHomeFragment = new AddNewHabitFragment();
        addNewHabitHomeFragment.setmOnReturnHome(this);
        if (savedInstanceState != null)
            savedInstanceState.putBoolean(HomeConfigurationKeys.IS_ON_ADD_NEW.getValue(), false);

        isOnAddNew = false;
    }

    @Override
    public void onHomeItemOnClickReturnHome() {
        removeChildFragment(homeItemOnClickFragment);
        homeItemOnClickFragment.setmOnItemOnClickReturnHome(null);

        homeItemOnClickFragment = new HomeItemOnClickFragment();
        homeItemOnClickFragment.setmOnItemOnClickReturnHome(this);

        if (savedInstanceState != null && !savedInstanceState.isEmpty())
            savedInstanceState.putBoolean(HomeConfigurationKeys.IS_ON_ITEM_CLICK.getValue(), false);

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

        final int FRAGMENT_TRANSITION = (!isOnAddDefault && !isOnAddNew) ? FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN : FragmentTransaction.TRANSIT_NONE;

        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(HomeFragment.this.getTag())
                .add(binding.homeFrameLayout.getId(), fragment)
                .setTransition(FRAGMENT_TRANSITION)
                .commit();

        binding.homeContainer.setVisibility(View.GONE);
    }

    /**
     * Save states prior to configuration changes.
     *
     * @param outState savedState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(HomeConfigurationKeys.IS_ON_ADD_DEFAULT.getValue(), isOnAddDefault);
        outState.putBoolean(HomeConfigurationKeys.IS_ON_ADD_NEW.getValue(), isOnAddNew);

        outState.putBoolean(HomeConfigurationKeys.IS_ON_ITEM_CLICK.getValue(), isOnItemClick);
        if (isOnItemClick) {
            outState.putInt(HomeConfigurationKeys.POSITION.getValue(), position);
        }
    }

    /**
     * Save states on shared preferences when leaving home menu
     */
    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(HomeConfigurationKeys.HOME_SHAREDPREF.getValue(), Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putBoolean(HomeConfigurationKeys.IS_ON_ADD_DEFAULT.getValue(), isOnAddDefault)
                .putBoolean(HomeConfigurationKeys.IS_ON_ADD_NEW.getValue(), isOnAddNew)
                .putBoolean(HomeConfigurationKeys.IS_ON_ITEM_CLICK.getValue(), isOnItemClick)
                .putBoolean(HomeConfigurationKeys.IS_ON_MODIFY.getValue(), isOnModify)
                .apply();

        if (isOnItemClick)
            sharedPreferences.edit().putInt(HomeConfigurationKeys.POSITION.getValue(), position).apply();
    }

    /**
     * Restore states from shared preferences when re-entering home menu
     */
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(HomeConfigurationKeys.HOME_SHAREDPREF.getValue(), Context.MODE_PRIVATE);
        if (sharedPreferences.contains(HomeConfigurationKeys.IS_ON_ADD_DEFAULT.getValue()) ||
                sharedPreferences.contains(HomeConfigurationKeys.IS_ON_ADD_NEW.getValue()) ||
                sharedPreferences.contains(HomeConfigurationKeys.IS_ON_ITEM_CLICK.getValue()) ||
                sharedPreferences.contains(HomeConfigurationKeys.IS_ON_MODIFY.getValue())) {

            isOnAddDefault = sharedPreferences.getBoolean(HomeConfigurationKeys.IS_ON_ADD_DEFAULT.getValue(), false);
            isOnAddNew = sharedPreferences.getBoolean(HomeConfigurationKeys.IS_ON_ADD_NEW.getValue(), false);
            isOnItemClick = sharedPreferences.getBoolean(HomeConfigurationKeys.IS_ON_ITEM_CLICK.getValue(), false);
            isOnModify = sharedPreferences.getBoolean(HomeConfigurationKeys.IS_ON_MODIFY.getValue(), false);

            if (isOnAddDefault) {
                addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);
                if (!addDefaultHabitFragment.isAdded()) {
                    changeFragment(addDefaultHabitFragment);
                }
            }

            if (isOnAddNew) {
                addNewHabitHomeFragment.setmOnReturnHome(this);
                if (!addNewHabitHomeFragment.isAdded()) {
                    changeFragment(addNewHabitHomeFragment);
                }
            }

            if (isOnItemClick) {
                position = sharedPreferences.getInt(HomeConfigurationKeys.POSITION.getValue(), 0);
                onItemClick(position);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        habitWithSubroutinesViewModel = null;
        habitsList = null;
        binding = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addDefaultHabitFragment.setmOnAddDefaultReturnHome(null);
        addNewHabitHomeFragment.setmOnReturnHome(null);
        homeItemOnClickFragment.setmOnItemOnClickReturnHome(null);
        homeParentItemAdapter.setHomeParentItemOnclickListener(null);
    }
}