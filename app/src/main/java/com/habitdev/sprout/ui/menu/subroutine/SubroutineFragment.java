package com.habitdev.sprout.ui.menu.subroutine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.habitdev.sprout.database.achievement.AchievementViewModel;
import com.habitdev.sprout.database.achievement.model.Achievement;
import com.habitdev.sprout.database.habit.firestore.SubroutineFireStoreViewModel;
import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.databinding.FragmentSubroutineBinding;
import com.habitdev.sprout.enums.SubroutineConfigurationKeys;
import com.habitdev.sprout.ui.menu.OnBackPressDialogFragment;
import com.habitdev.sprout.ui.menu.subroutine.adapter.SubroutineParentItemAdapter;
import com.habitdev.sprout.ui.menu.subroutine.ui.SubroutineModifyFragment;
import com.habitdev.sprout.utill.dialog.CompletedAchievementDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SubroutineFragment extends Fragment
        implements
        SubroutineParentItemAdapter.OnClickListener,
        SubroutineModifyFragment.OnReturnSubroutine {

    private FragmentSubroutineBinding binding;
    private static SubroutineModifyFragment subroutineModifyFragment = new SubroutineModifyFragment();
    private static boolean isOnSubroutineModify;
    private static ArrayList<Integer> arrayList = new ArrayList<>();

    public SubroutineFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        subroutineModifyFragment.setmOnClickBackPress(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSubroutineBinding.inflate(inflater, container, false);

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            isOnSubroutineModify = savedInstanceState.getBoolean(SubroutineConfigurationKeys.IS_ON_SUBROUTINE_MODIFY.getKey());
        }

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SubroutineConfigurationKeys.SUBROUTINE_ADAPTER_SHAREDPREF.getKey(), Context.MODE_PRIVATE);
        if (!sharedPreferences.getAll().isEmpty()) {
            setArrayList();
        }

        setRecyclerViewAdapter();
        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isOnSubroutineModify) {
            setSubroutineModifyFragment();
        }
    }

    /**
     * Initialized and sets adapter for recyclerView
     */
    private void setRecyclerViewAdapter() {
        if (binding.subroutineRecyclerView.getAdapter() == null) {
            HabitWithSubroutinesViewModel habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
            SubroutineFireStoreViewModel subroutineFireStoreViewModel = new ViewModelProvider(requireActivity()).get(SubroutineFireStoreViewModel.class);

            binding.subroutineRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

            SubroutineParentItemAdapter parentAdapterItem = new SubroutineParentItemAdapter();
            parentAdapterItem.setOldHabitList(new ArrayList<>(habitWithSubroutinesViewModel.getAllHabitOnReform()));

            setEmptyRVBackground(parentAdapterItem);

            parentAdapterItem.setmOnClickListener(this);
            parentAdapterItem.setSubroutineLifecycleOwner(getViewLifecycleOwner());
            parentAdapterItem.setHabitWithSubroutinesViewModel(habitWithSubroutinesViewModel);
            parentAdapterItem.setSubroutineFireStoreViewModel(subroutineFireStoreViewModel);

            parentAdapterItem.setArrayList(arrayList);

            binding.subroutineRecyclerView.setAdapter(parentAdapterItem);

            habitWithSubroutinesViewModel.getAllHabitOnReformLiveData().observe(getViewLifecycleOwner(), habits -> {
                parentAdapterItem.setNewHabitList(new ArrayList<>(habits));
                setEmptyRVBackground(parentAdapterItem);
            });
        }
    }

    /**
     * Displays No subroutine on reform
     *
     * @param adapter SubroutineParentItemAdapter
     */
    private void setEmptyRVBackground(SubroutineParentItemAdapter adapter) {
        if (adapter.getItemCount() > 0) {
            binding.subroutineEmptyLottieRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            binding.subroutineEmptyLottieRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Interface from SubroutineParentItemAdapter
     *
     * @param habit Habit on Modify
     */
    @Override
    public void onModifySubroutine(Habits habit) {
        new AlertDialog.Builder(requireContext())
                .setMessage("Do you want to modify the subroutines?")
                .setCancelable(false)
                .setPositiveButton("YES", (dialogInterface, i) -> {
                    isOnSubroutineModify = true;
                    subroutineModifyFragment.setHabit(habit);
                    //TODO: UPDATE UID WHEN APPDATABASE CHANGE
                    AchievementViewModel achievementViewModel = new ViewModelProvider(requireActivity()).get(AchievementViewModel.class);
                    Achievement EditCustomHabitSubroutine = achievementViewModel.getAchievementByUID(13);
                    if (!EditCustomHabitSubroutine.is_completed() ) {
                        EditCustomHabitSubroutine.setIs_completed(true);
                        EditCustomHabitSubroutine.setCurrent_progress(EditCustomHabitSubroutine.getCurrent_progress() + 1);
                        EditCustomHabitSubroutine.setDate_achieved(new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date()));
                        achievementViewModel.updateAchievement(EditCustomHabitSubroutine);
                        CompletedAchievementDialogFragment dialog = new CompletedAchievementDialogFragment(EditCustomHabitSubroutine.getTitle());
                        dialog.setTargetFragment(getChildFragmentManager()
                                .findFragmentById(SubroutineFragment.this.getId()), 1);
                        dialog.show(getChildFragmentManager(), "CompletedAchievementDialog");
                        dialog.setmOnClick(new CompletedAchievementDialogFragment.onClick() {
                            @Override
                            public void onClickOkay() {
                                dialog.dismiss();
                                setSubroutineModifyFragment();
                            }
                        });
                    } else {
                        setSubroutineModifyFragment();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void isExpanded(int position) {
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        if (!arrayList.contains(position)) {
            arrayList.add(position);
        }
    }

    @Override
    public void isHidden(int position) {
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        if (!arrayList.isEmpty()) {
            try {
                arrayList.remove((Integer) position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setSubroutineModifyFragment() {
        if (!subroutineModifyFragment.isAdded()){
            final int TRANSITION = isOnSubroutineModify ? FragmentTransaction.TRANSIT_NONE : FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN;
            getChildFragmentManager().beginTransaction()
                    .addToBackStack(SubroutineFragment.this.getTag())
                    .add(binding.subroutineFrameLayout.getId(), subroutineModifyFragment)
                    .setTransition(TRANSITION)
                    .commit();
            binding.subroutineContainer.setVisibility(View.GONE);
        }
    }

    /**
     * Interface from SubroutineModify Fragment
     */
    @Override
    public void returnSubroutineFragment() {
        isOnSubroutineModify = false;
        getChildFragmentManager()
                .beginTransaction()
                .remove(subroutineModifyFragment)
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .commit();
        subroutineModifyFragment = new SubroutineModifyFragment();
        subroutineModifyFragment.setmOnClickBackPress(this);
        binding.subroutineContainer.setVisibility(View.VISIBLE);
    }

    /**
     * Handle on BackPress
     */
    private void onBackPress() {
        final int[] keypress_count = {0};
        final boolean[] isOnBackPressDialogShowing = {false};
        final boolean[] isAchievementDialogShowing = {false};

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                keypress_count[0]++;

                new CountDownTimer(200, 200) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        if (keypress_count[0] > 1) {
                            //Dialog is displayed twice
                            OnBackPressDialogFragment dialog = new OnBackPressDialogFragment();
                            if (!isOnBackPressDialogShowing[0]) {
                                dialog.setTargetFragment(getChildFragmentManager().findFragmentById(SubroutineFragment.this.getId()), 2);
                                dialog.show(getChildFragmentManager(), "Menu.onBackPress");
                                dialog.setmOnCancelDialog(new OnBackPressDialogFragment.onCancelDialog() {
                                    @Override
                                    public void cancelDialog() {
                                        keypress_count[0] = 0;
                                        isOnBackPressDialogShowing[0] = false;
                                    }
                                });
                                isOnBackPressDialogShowing[0] = true;
                            }
                            if (!isAchievementDialogShowing[0]) {
                                //TODO: UPDATE UID WHEN APPDATABASE CHANGE
                                AchievementViewModel achievementViewModel = new ViewModelProvider(requireActivity()).get(AchievementViewModel.class);
                                Achievement CLOSEAPPPROMPT = achievementViewModel.getAchievementByUID(26);

                                if (!CLOSEAPPPROMPT.is_completed()) {
                                    CLOSEAPPPROMPT.setIs_completed(true);
                                    CLOSEAPPPROMPT.setCurrent_progress(CLOSEAPPPROMPT.getCurrent_progress() + 1);
                                    CLOSEAPPPROMPT.setDate_achieved(new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date()));
                                    CLOSEAPPPROMPT.setTitle("Close Application");
                                    CLOSEAPPPROMPT.setDescription("Unlocked by pressing back button twice");
                                    achievementViewModel.updateAchievement(CLOSEAPPPROMPT);
                                    CompletedAchievementDialogFragment completedAchievementDialogFragment = new CompletedAchievementDialogFragment(CLOSEAPPPROMPT.getTitle());
                                    completedAchievementDialogFragment.setTargetFragment(getChildFragmentManager()
                                            .findFragmentById(SubroutineFragment.this.getId()), 1);
                                    completedAchievementDialogFragment.show(getChildFragmentManager(), "CompletedAchievementDialog");
                                    isAchievementDialogShowing[0] = true;
                                }
                            }
                        } else {
                            requireActivity().moveTaskToBack(true);
                            keypress_count[0] = 0;
                        }
                        this.cancel();
                    }
                }.start();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(
                SubroutineConfigurationKeys.IS_ON_SUBROUTINE_MODIFY.getKey(),
                isOnSubroutineModify
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().getSharedPreferences(
                        SubroutineConfigurationKeys.SUBROUTINE_ADAPTER_SHAREDPREF.getKey(),
                        Context.MODE_PRIVATE
                )
                .edit()
                .putString(
                        SubroutineConfigurationKeys.ITEM_POSITION_GSON.getKey(),
                        new Gson().toJson(arrayList)
                )
                .apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        setArrayList();
    }

    private void setArrayList() {
        arrayList = new Gson()
                .fromJson(
                        requireActivity()
                                .getSharedPreferences(
                                        SubroutineConfigurationKeys.SUBROUTINE_ADAPTER_SHAREDPREF.getKey(),
                                        Context.MODE_PRIVATE
                                )
                                .getString(
                                        SubroutineConfigurationKeys.ITEM_POSITION_GSON.getKey(),
                                        null
                                ), new TypeToken<ArrayList<Integer>>() {
                        }.getType()
                );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        subroutineModifyFragment.setmOnClickBackPress(null);
    }

    /**
     * On Fragment Destroy
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}