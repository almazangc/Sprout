package com.habitdev.sprout.ui.menu.home.ui.fab_.custom_;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentAddNewHabitBinding;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.enums.HomeConfigurationKeys;
import com.habitdev.sprout.ui.menu.home.adapter.HomeAddNewHabitParentAdapter;
import com.habitdev.sprout.ui.menu.home.ui.dialog.HomeAddNewInsertSubroutineDialogFragment;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


public class AddNewHabitFragment extends Fragment
        implements HomeAddNewInsertSubroutineDialogFragment.OnDialogChange {

    private FragmentAddNewHabitBinding binding;
    private static HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private static Habits habit;
    private static List<Habits> habitsList;
    private static List<Subroutines> subroutinesList;
    private static Subroutines subroutine;

    private static int current_selected_color;
    private static int old_selected_color;
    private static String color = AppColor.CLOUDS.getColor();

    /**
     * <p>Identifier when fragment will removed for clearing shared pref</p>
     */
    private static boolean isFragmentOnRemoved = false;

    public interface OnAddNewHabitReturnHome {
        void onAddNewHabitClickReturnHome();
    }

    private OnAddNewHabitReturnHome mOnAddNewHabitReturnHome;

    public void setmOnReturnHome(OnAddNewHabitReturnHome mOnAddNewHabitReturnHome) {
        this.mOnAddNewHabitReturnHome = mOnAddNewHabitReturnHome;
    }

    private HomeAddNewHabitParentAdapter homeAddNewHabitParentAdapter;

    private static Bundle savedInstanceState;

    public AddNewHabitFragment() {
        habit = new Habits(
                null,
                null,
                AppColor.CLOUDS.getColor(),
                false,
                true
        );
        subroutinesList = new ArrayList<>();
    }

    @Override
    public void addSubroutine(Subroutines subroutines) {
        subroutine = subroutines;
        subroutinesList.add(subroutine);
        setRecyclerViewAdapter();
    }

    @Override
    public void modifySubroutine(Subroutines subroutines) {
        subroutine = subroutines;
        setRecyclerViewAdapter();
    }

    @Override
    public void removeSubroutine(Subroutines subroutines) {
        subroutine = subroutines;
        subroutinesList.remove(subroutines);
        setRecyclerViewAdapter();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddNewHabitBinding.inflate(inflater, container, false);

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            AddNewHabitFragment.savedInstanceState = savedInstanceState;
        }

        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);

        setHint();
        setDropDownHabits();
        setCurrentTime();
        setHabitColor();
        colorSelect();
        insertSubroutine();
        setRecyclerViewAdapter();
        insertNewHabit();
        deleteHabit();
        onBackPress();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {

            current_selected_color = savedInstanceState.getInt(HomeConfigurationKeys.CURRENT_SELECTED_COLOR.getValue());
            old_selected_color = savedInstanceState.getInt(HomeConfigurationKeys.OLD_SELECTED_COLOR.getValue());

            clearSelected();
            setSelected_color();

            binding.addNewHabitHint.setText(
                    savedInstanceState.getString(HomeConfigurationKeys.HINT_TEXT.getValue())
            );
            binding.addNewHabitTitle.setText(
                    savedInstanceState.getString(HomeConfigurationKeys.TITLE.getValue())
            );
            binding.addNewHabitDescription.setText(
                    savedInstanceState.getString(HomeConfigurationKeys.DESCRIPTION.getValue())
            );
            binding.fabAddDeleteHabit.setVisibility(
                    savedInstanceState.getInt(HomeConfigurationKeys.VIEW_VISIBILITY.getValue(), View.GONE)
            );

            if (savedInstanceState.containsKey(HomeConfigurationKeys.SUBROUTINELISTGSON.getValue())) {
                String gson_subroutine_list = savedInstanceState.getString(HomeConfigurationKeys.SUBROUTINELISTGSON.getValue(), null);

                Type listType = new TypeToken<ArrayList<Subroutines>>() {
                }.getType();
                subroutinesList = new Gson().fromJson(gson_subroutine_list, listType);

                setRecyclerViewAdapter();

            }
            setDropDownHabits();
        }
    }

    private void setHint() {
        final String REQUIRED = "Required*";
        final String EMPTY_TITLE = "Empty Title*";
        final String EMPTY_DESC = "Empty Description*";
        final String MIN_SUBROUTINE = "Required Minimum of 2 Subroutine*";

        binding.addNewHabitTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty())
                    binding.addNewHabitHint.setText(REQUIRED);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    binding.addNewHabitHint.setText(EMPTY_TITLE);
                } else {
                    if (binding.addNewHabitDescription.getText().toString().trim().isEmpty()) {
                        binding.addNewHabitHint.setText(EMPTY_DESC);
                    } else {
                        if (!binding.addNewHabitHint.getText().toString().trim().equals(MIN_SUBROUTINE)) {
                            binding.addNewHabitHint.setText("");
                        }
                    }
                }
            }
        });
        binding.addNewHabitDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty())
                    binding.addNewHabitHint.setText(REQUIRED);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    binding.addNewHabitHint.setText(EMPTY_DESC);
                } else {
                    if (binding.addNewHabitTitle.getText().toString().trim().isEmpty()) {
                        binding.addNewHabitHint.setText(EMPTY_TITLE);
                    } else {
                        if (!binding.addNewHabitHint.getText().toString().trim().equals(MIN_SUBROUTINE)) {
                            binding.addNewHabitHint.setText("");
                        }
                    }
                }
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (binding != null) {
                            if (subroutinesList.isEmpty() || subroutinesList.size() < 2) {
                                if (binding.addNewHabitHint.getText().toString().trim().isEmpty() && !binding.addNewHabitHint.getText().toString().trim().equals(MIN_SUBROUTINE))
                                    binding.addNewHabitHint.setText(MIN_SUBROUTINE);
                            } else {
                                binding.addNewHabitHint.setText("");
                            }
                        } else {
                            timer.cancel();
                            timer.purge();
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    private void setDropDownHabits() {
        List<String> habitTitles = new ArrayList<>();
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(requireContext(), R.layout.adapter_home_parent_habit_drop_down_item, habitTitles);
        habitWithSubroutinesViewModel.getAllUserDefinedHabitListLiveData().observe(getViewLifecycleOwner(), habits -> {
            if (habits != null) {
                if (!habits.isEmpty()) {
                    List<Habits> onDropDownHabits = new ArrayList<>();
                    habitTitles.clear(); //clear contents
                    for (Habits habit : habits) {
                        if (!habit.isOnReform()) {
                            habitTitles.add(habit.getHabit());
                            onDropDownHabits.add(habit);
                        }
                    }
                    habitsList = onDropDownHabits;
                }
                if (habitTitles.isEmpty()) {
                    if (binding.addNewHabitTextInput.getVisibility() == View.VISIBLE)
                        binding.addNewHabitTextInput.setVisibility(View.GONE);
                } else {
                    if (binding.addNewHabitTextInput.getVisibility() == View.GONE)
                        binding.addNewHabitTextInput.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.addNewHabitDropItems.setAdapter(adapterItems);

        binding.addNewHabitDropItems.setOnItemClickListener((adapterView, view, pos, id) -> {
            habit = habitsList.get(pos);
            setDropDownContentView();
        });

        binding.addNewHabitDropItems.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    binding.addNewHabitTitle.setText("");
                    binding.addNewHabitDescription.setText("");
                    color = AppColor.CLOUDS.getColor();
                    setHabitColor();
                    setSelected_color();
                    subroutinesList.clear();
                    setRecyclerViewAdapter();
                } else {
                    for (String title : habitTitles) {
                        if (editable.toString().trim().equals(title.trim())) {
                            binding.fabAddDeleteHabit.setVisibility(View.VISIBLE);
                            break;
                        } else {
                            binding.fabAddDeleteHabit.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

    private void setDropDownContentView() {
        binding.addNewHabitTitle.setText(habit.getHabit());
        binding.addNewHabitDescription.setText(habit.getDescription());
        color = habit.getColor();
        clearSelected();
        setHabitColor();
        setSelected_color();
        setSubroutinesList(habit.getPk_habit_uid());
    }

    private void setSubroutinesList(long uid) {
        subroutinesList.clear();
        subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(uid);
        setRecyclerViewAdapter();
    }

    private void setCurrentTime() {
        binding.addNewHabitCurrentTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss a", Locale.getDefault()).format(new Date())
        );
    }

    private void colorSelect() {
//            No need to toggle hide because on edit mode, gotta disable

//        binding.addNewHabitColorSelector.setOnClickListener(v -> {
//            if (binding.addNewHabitLayoutMiscellaneous.getVisibility() == View.GONE) {
//                binding.addNewHabitLayoutMiscellaneous.setVisibility(View.VISIBLE);
//            } else {
//                binding.addNewHabitLayoutMiscellaneous.setVisibility(View.GONE);
//            }
//        });

        binding.cloudMisc.setOnClickListener(v -> updateSelectedColorIndex(0));

        binding.alzarinMisc.setOnClickListener(v -> updateSelectedColorIndex(1));

        binding.amethystMisc.setOnClickListener(v -> updateSelectedColorIndex(2));

        binding.brightskyBlueMisc.setOnClickListener(v -> updateSelectedColorIndex(3));

        binding.nephritisMisc.setOnClickListener(v -> updateSelectedColorIndex(4));

        binding.sunflowerMisc.setOnClickListener(v -> updateSelectedColorIndex(5));
    }

    private void setHabitColor() {
        if (habit != null) {
            if (habit.getColor().equals(AppColor.ALZARIN.getColor())) {
                current_selected_color = 1;
            } else if (habit.getColor().equals(AppColor.AMETHYST.getColor())) {
                current_selected_color = 2;
            } else if (habit.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
                current_selected_color = 3;
            } else if (habit.getColor().equals(AppColor.NEPHRITIS.getColor())) {
                current_selected_color = 4;
            } else if (habit.getColor().equals(AppColor.SUNFLOWER.getColor())) {
                current_selected_color = 5;
            } else {
                old_selected_color = 1;
            }
        } else {
            old_selected_color = 1;
        }
        setSelected_color();
    }

    private void setSelected_color() {

        if (old_selected_color != current_selected_color) {
            int ic_check = R.drawable.ic_check;

            switch (current_selected_color) {
                case 1:
                    binding.alzarinSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_alzarin));
                    color = AppColor.ALZARIN.getColor();
                    break;
                case 2:
                    binding.amethystSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_amethyst));
                    color = AppColor.AMETHYST.getColor();
                    break;
                case 3:
                    binding.brightskyBlueSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_brightsky_blue));
                    color = AppColor.BRIGHT_SKY_BLUE.getColor();
                    break;
                case 4:
                    binding.nephritisSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_nephritis));
                    color = AppColor.NEPHRITIS.getColor();
                    break;
                case 5:
                    binding.sunflowerSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_sunflower));
                    color = AppColor.SUNFLOWER.getColor();
                    break;
                default:
                    binding.cloudSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_clouds));
                    color = AppColor.CLOUDS.getColor();
                    break;
            }

            switch (old_selected_color) {
                case 1:
                    binding.alzarinSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 2:
                    binding.amethystSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 3:
                    binding.brightskyBlueSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 4:
                    binding.nephritisSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 5:
                    binding.sunflowerSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                default:
                    binding.cloudSelected.setImageResource(R.color.TRANSPARENT);
                    break;
            }
        }
    }

    private void updateSelectedColorIndex(int newSelected) {
        old_selected_color = current_selected_color;
        current_selected_color = newSelected;
        setSelected_color();
    }

    private void setBackgroundColorIndicator(Drawable backgroundNoteIndicator) {
        binding.addNewHabitColorSelector.setBackground(backgroundNoteIndicator);
    }

    private void insertNewHabit() {
        binding.fabAddNewHabit.setOnClickListener(v -> {
            if (binding.addNewHabitHint.getText().toString().trim().isEmpty()) {
                habit.setHabit(binding.addNewHabitTitle.getText().toString().trim());
                habit.setDescription(binding.addNewHabitDescription.getText().toString().trim());
                habit.setOnReform(true);
                habit.setDate_started(binding.addNewHabitCurrentTime.getText().toString().trim());
                habit.setColor(color);
                habit.setTotal_subroutine(subroutinesList.size());

                long uid = habitWithSubroutinesViewModel.insertHabit(habit);

                if (!subroutinesList.isEmpty()) {
                    for (Subroutines subroutine : subroutinesList) {
                        subroutine.setFk_habit_uid(uid);
                    }
                    habitWithSubroutinesViewModel.insertSubroutines(subroutinesList);
                }
                if (mOnAddNewHabitReturnHome != null)
                    mOnAddNewHabitReturnHome.onAddNewHabitClickReturnHome();

                if (savedInstanceState != null) savedInstanceState = null;
                isFragmentOnRemoved = true;
            }
        });
    }

    /**
     * Display Insert Subroutine Dialog for new Subroutine
     */
    private void insertSubroutine() {
        binding.addNewHabitSubroutineBtn.setOnClickListener(view -> {
            HomeAddNewInsertSubroutineDialogFragment dialog = new HomeAddNewInsertSubroutineDialogFragment();
            dialog.setTargetFragment(getChildFragmentManager()
                    .findFragmentById(AddNewHabitFragment.this.getId()), 1);
            dialog.show(getChildFragmentManager(), "HomeAddNewInsertSubroutineDialog");
        });
    }

    /**
     * Delete Habit and its subroutines and comments in room database
     */
    private void deleteHabit() {
        binding.fabAddDeleteHabit.setOnClickListener(view -> {
            habitWithSubroutinesViewModel.deleteHabit(habit);
            habitWithSubroutinesViewModel.deleteSubroutineList(subroutinesList);
            binding.fabAddDeleteHabit.setVisibility(View.GONE);
            habit = new Habits("", "", AppColor.CLOUDS.getColor(), true, true);
            subroutinesList = new ArrayList<>();
            setDropDownContentView();
            setDropDownHabits();
        });
    }

    private void setRecyclerViewAdapter() {
        if (subroutinesList != null) {
            if (subroutinesList.isEmpty()) {
                binding.addNewHabitSubroutineCardView.setVisibility(View.GONE);
            } else {
                binding.addNewHabitSubroutineCardView.setVisibility(View.VISIBLE);
            }

            if (homeAddNewHabitParentAdapter == null)
                homeAddNewHabitParentAdapter = new HomeAddNewHabitParentAdapter();

            homeAddNewHabitParentAdapter.setOnClickListener(new HomeAddNewHabitParentAdapter.OnClickListener() {
                @Override
                public void onDelete(Subroutines subroutine) {
                    HomeAddNewInsertSubroutineDialogFragment dialog = new HomeAddNewInsertSubroutineDialogFragment(subroutine, true);
                    dialog.setTargetFragment(getChildFragmentManager().findFragmentById(AddNewHabitFragment.this.getId()), 1);
                    dialog.show(getChildFragmentManager(), "AddNewHabitDialog.onDelete");
                }

                @Override
                public void onModify(Subroutines subroutine) {
                    HomeAddNewInsertSubroutineDialogFragment dialog = new HomeAddNewInsertSubroutineDialogFragment(subroutine);
                    dialog.setTargetFragment(getChildFragmentManager().findFragmentById(AddNewHabitFragment.this.getId()), 1);
                    dialog.show(getChildFragmentManager(), "AddNewHabitDialog.onModify");
                }
            });

            if (binding.addNewHabitSubroutineRecyclerView.getAdapter() == null) {
                binding.addNewHabitSubroutineRecyclerView.setAdapter(homeAddNewHabitParentAdapter);
            } else {
                homeAddNewHabitParentAdapter.setNewSubroutinesList(subroutinesList);
            }
        }
    }

    private void clearSelected() {
        binding.alzarinSelected.setImageResource(R.color.TRANSPARENT);
        binding.amethystSelected.setImageResource(R.color.TRANSPARENT);
        binding.brightskyBlueSelected.setImageResource(R.color.TRANSPARENT);
        binding.nephritisSelected.setImageResource(R.color.TRANSPARENT);
        binding.sunflowerSelected.setImageResource(R.color.TRANSPARENT);
        binding.cloudSelected.setImageResource(R.color.TRANSPARENT);
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (savedInstanceState != null) savedInstanceState = null;
                current_selected_color = 0;

                isFragmentOnRemoved = true;

                if (mOnAddNewHabitReturnHome != null)
                    mOnAddNewHabitReturnHome.onAddNewHabitClickReturnHome();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(HomeConfigurationKeys.CURRENT_SELECTED_COLOR.getValue(), current_selected_color);
        outState.putInt(HomeConfigurationKeys.OLD_SELECTED_COLOR.getValue(), old_selected_color);

        if (binding != null) {
            outState.putString(HomeConfigurationKeys.HINT_TEXT.getValue(), binding.addNewHabitHint.getText().toString().trim());
            outState.putString(HomeConfigurationKeys.TITLE.getValue(), binding.addNewHabitTitle.getText().toString().trim());
            outState.putString(HomeConfigurationKeys.DESCRIPTION.getValue(), binding.addNewHabitDescription.getText().toString().trim());
            outState.putInt(HomeConfigurationKeys.VIEW_VISIBILITY.getValue(), binding.fabAddDeleteHabit.getVisibility());
        }

        if (!subroutinesList.isEmpty()) {
            String gson_subroutine_list = new Gson().toJson(subroutinesList);
            outState.putString(HomeConfigurationKeys.SUBROUTINELISTGSON.getValue(), gson_subroutine_list);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(HomeConfigurationKeys.HOME_ADD_NEW_SHAREDPREF.getValue(), Context.MODE_PRIVATE);

        if (!isFragmentOnRemoved) {
            savedInstanceState = null;
            sharedPreferences.edit()
                    .putInt(HomeConfigurationKeys.CURRENT_SELECTED_COLOR.getValue(), current_selected_color)
                    .putInt(HomeConfigurationKeys.OLD_SELECTED_COLOR.getValue(), old_selected_color)
                    .apply();

            if (binding != null) {
                sharedPreferences.edit()
                        .putString(HomeConfigurationKeys.HINT_TEXT.getValue(), binding.addNewHabitHint.getText().toString().trim())
                        .putString(HomeConfigurationKeys.TITLE.getValue(), binding.addNewHabitTitle.getText().toString().trim())
                        .putString(HomeConfigurationKeys.DESCRIPTION.getValue(), binding.addNewHabitDescription.getText().toString().trim())
                        .putInt(HomeConfigurationKeys.VIEW_VISIBILITY.getValue(), binding.fabAddDeleteHabit.getVisibility())
                        .apply();
            }

            if (!subroutinesList.isEmpty()) {
                String gson_subroutine_list = new Gson().toJson(subroutinesList);
                sharedPreferences.edit().
                        putString(HomeConfigurationKeys.SUBROUTINELISTGSON.getValue(), gson_subroutine_list)
                        .apply();
            }
        } else {
            sharedPreferences.edit().clear().apply();
            isFragmentOnRemoved = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(HomeConfigurationKeys.HOME_ADD_NEW_SHAREDPREF.getValue(), Context.MODE_PRIVATE);

        Map<String, ?> entries = sharedPreferences.getAll();
        Set<String> keys = entries.keySet();

        if (!keys.isEmpty()) {
            current_selected_color = sharedPreferences.getInt(HomeConfigurationKeys.CURRENT_SELECTED_COLOR.getValue(), 0);
            old_selected_color = sharedPreferences.getInt(HomeConfigurationKeys.OLD_SELECTED_COLOR.getValue(), 0);

            clearSelected();
            setSelected_color();

            binding.addNewHabitHint.setText(
                    sharedPreferences.getString(HomeConfigurationKeys.HINT_TEXT.getValue(), null)
            );
            binding.addNewHabitTitle.setText(
                    sharedPreferences.getString(HomeConfigurationKeys.TITLE.getValue(), null)
            );
            binding.addNewHabitDescription.setText(
                    sharedPreferences.getString(HomeConfigurationKeys.DESCRIPTION.getValue(), null)
            );
            binding.fabAddDeleteHabit.setVisibility(
                    sharedPreferences.getInt(HomeConfigurationKeys.VIEW_VISIBILITY.getValue(), View.GONE)
            );

            if (sharedPreferences.contains(HomeConfigurationKeys.SUBROUTINELISTGSON.getValue())) {
                String gson_subroutine_list = sharedPreferences.getString(HomeConfigurationKeys.SUBROUTINELISTGSON.getValue(), null);

                Type listType = new TypeToken<ArrayList<Subroutines>>() {
                }.getType();
                subroutinesList = new Gson().fromJson(gson_subroutine_list, listType);

                setRecyclerViewAdapter();
            }
            setDropDownHabits();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mOnAddNewHabitReturnHome = null;
        habitWithSubroutinesViewModel.getAllUserDefinedHabitListLiveData().removeObservers(getViewLifecycleOwner());
        habitWithSubroutinesViewModel = null;
        binding = null;
    }
}