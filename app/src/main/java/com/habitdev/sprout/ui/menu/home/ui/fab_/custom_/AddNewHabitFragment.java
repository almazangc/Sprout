package com.habitdev.sprout.ui.menu.home.ui.fab_.custom_;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentAddNewHabitBinding;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.ui.menu.home.HomeFragment;
import com.habitdev.sprout.ui.menu.home.adapter.HomeAddNewHabitParentAdapter;
import com.habitdev.sprout.ui.menu.home.ui.dialog.HomeAddNewInsertSubroutineDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AddNewHabitFragment extends Fragment implements HomeAddNewInsertSubroutineDialogFragment.onDialoagChange {

    private final int ic_check;
    private FragmentAddNewHabitBinding binding;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private Habits habit;
    private Habits habit_snapshot;
    private List<Habits> habitsList;
    private List<Subroutines> subroutinesList;
    private List<Subroutines> subroutinesList_snapshot;
    private Subroutines subroutine;
    private int current_selected_color;
    private int old_selected_color;
    private String color;

    private HomeAddNewHabitParentAdapter homeAddNewHabitParentAdapter;

    public AddNewHabitFragment() {
        this.ic_check = R.drawable.ic_check;
        this.current_selected_color = 0;
        this.old_selected_color = 0;
        this.color = AppColor.CLOUDS.getColor();
        this.habit = new Habits(
                null,
                null,
                AppColor.CLOUDS.getColor(),
                false,
                true
        );
        this.subroutinesList = new ArrayList<>();
    }

    @Override
    public void addSubroutine(Subroutines subroutines) {
        this.subroutine = subroutines;
        subroutinesList.add(subroutine);
        setRecyclerViewAdapter();
    }

    @Override
    public void modifySubroutine(Subroutines subroutines) {
        this.subroutine = subroutines;
        setRecyclerViewAdapter();
    }

    @Override
    public void removeSubroutine(Subroutines subroutines) {
        this.subroutine = subroutines;
        subroutinesList.remove(subroutines);
        setRecyclerViewAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddNewHabitBinding.inflate(inflater, container, false);
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        setHint();
        userDefinedHabit();
        setCurrentTime();
        setHabitColor();
        colorSelect();
        insertSubroutine();
        setRecyclerViewAdapter();
        insertNewHabit();
        onBackPress();
        return binding.getRoot();
    }

    private void setHint() {
        binding.addNewHabitTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty())
                    binding.addNewHabitHint.setText("Required*");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    binding.addNewHabitHint.setText("Empty Title*");
                } else {
                    if (binding.addNewHabitDescription.getText().toString().trim().isEmpty()) {
                        binding.addNewHabitHint.setText("Empty Description*");
                    } else {
                        binding.addNewHabitHint.setText("");
                    }
                }
            }
        });
        binding.addNewHabitDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty())
                    binding.addNewHabitHint.setText("Required*");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    binding.addNewHabitHint.setText("Empty Description*");
                } else {
                    if (binding.addNewHabitTitle.getText().toString().trim().isEmpty()) {
                        binding.addNewHabitHint.setText("Empty Title*");
                    } else {
                        binding.addNewHabitHint.setText("");
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
                                if (binding.addNewHabitHint.getText().toString().trim().isEmpty())
                                    binding.addNewHabitHint.setText("Required Minimum of 2 Subroutine*");
                            } else {
                                binding.addNewHabitHint.setText("");
                            }
                        } else {
                            timer.cancel();
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    private void userDefinedHabit() {
        List<String> habitTitles = new ArrayList<>();
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(requireContext(), R.layout.adapter_home_parent_habit_drop_down_item, habitTitles);;
        habitWithSubroutinesViewModel.getAllUserDefinedHabitListLiveData().observe(getViewLifecycleOwner(), habits -> {
            if (habits != null) {
                if (!habits.isEmpty()) {
                    if (binding.addNewHabitTextInput.getVisibility() == View.GONE)
                        binding.addNewHabitTextInput.setVisibility(View.VISIBLE);
                    habitsList = habits;
                    habitTitles.clear();
                    for (Habits habit : habits) {
                        habitTitles.add(habit.getHabit());
                    }
                } else {
                    if (binding.addNewHabitTextInput.getVisibility() == View.VISIBLE)
                        binding.addNewHabitTextInput.setVisibility(View.GONE);
                }
//                adapterItems.clear();
//                adapterItems.addAll(habitTitles);
            }
        });

        binding.addNewHabitDropItems.setAdapter(adapterItems);

        binding.addNewHabitDropItems.setOnItemClickListener((adapterView, view, pos, id) -> {
            habit = habitsList.get(pos);
            updateView();
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

                            habit_snapshot = habit;
                            subroutinesList_snapshot = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit_snapshot.getPk_habit_uid());

                            deleteHabit();
                            break;
                        } else {
                            binding.fabAddDeleteHabit.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

    private void updateView() {
        binding.addNewHabitTitle.setText(habit.getHabit());
        binding.addNewHabitDescription.setText(habit.getDescription());
        color = habit.getColor();
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
        binding.addNewHabitColorSelector.setOnClickListener(v -> {
            if (binding.addNewHabitLayoutMiscellaneous.getVisibility() == View.GONE) {
                binding.addNewHabitLayoutMiscellaneous.setVisibility(View.VISIBLE);
            } else {
                binding.addNewHabitLayoutMiscellaneous.setVisibility(View.GONE);
            }
        });

        binding.cloudMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(0);
            setSelected_color();
        });

        binding.alzarinMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(1);
            setSelected_color();
        });

        binding.amethystMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(2);
            setSelected_color();
        });

        binding.brightskyBlueMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(3);
            setSelected_color();
        });

        binding.nephritisMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(4);
            setSelected_color();
        });

        binding.sunflowerMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(5);
            setSelected_color();
        });
    }

    private void setHabitColor() {
        if (habit.getColor().equals(AppColor.ALZARIN.getColor())) {
            current_selected_color = 1;
            setSelected_color();
        } else if (habit.getColor().equals(AppColor.AMETHYST.getColor())) {
            current_selected_color = 2;
            setSelected_color();
        } else if (habit.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
            current_selected_color = 3;
            setSelected_color();
        } else if (habit.getColor().equals(AppColor.NEPHRITIS.getColor())) {
            current_selected_color = 4;
            setSelected_color();
        } else if (habit.getColor().equals(AppColor.SUNFLOWER.getColor())) {
            current_selected_color = 5;
            setSelected_color();
        } else {
            old_selected_color = 1;
            setSelected_color();
        }
    }

    private void setSelected_color() {
        if (old_selected_color != current_selected_color) {
            switch (current_selected_color) {
                case 1:
                    //alzarin
                    binding.alzarinSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_alzarin));
                    color = AppColor.ALZARIN.getColor();
                    break;
                case 2:
                    //amethyst
                    binding.amethystSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_amethyst));
                    color = AppColor.AMETHYST.getColor();
                    break;
                case 3:
                    //bright_sky_blue
                    binding.brightskyBlueSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_brightsky_blue));
                    color = AppColor.BRIGHT_SKY_BLUE.getColor();
                    break;
                case 4:
                    //nephritis
                    binding.nephritisSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_nephritis));
                    color = AppColor.NEPHRITIS.getColor();
                    break;
                case 5:
                    //sunflower
                    binding.sunflowerSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_sunflower));
                    color = AppColor.SUNFLOWER.getColor();
                    break;
                default:
                    //clouds night
                    binding.cloudSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_clouds));
                    color = AppColor.CLOUDS.getColor();
                    break;
            }

            switch (old_selected_color) {
                case 1:
                    //alzarin
                    binding.alzarinSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 2:
                    //amethyst
                    binding.amethystSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 3:
                    //bright_sky_blue
                    binding.brightskyBlueSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 4:
                    //nephritis
                    binding.nephritisSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 5:
                    //sunflower
                    binding.sunflowerSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                default:
                    //clouds night
                    binding.cloudSelected.setImageResource(R.color.TRANSPARENT);
                    break;
            }
        }
    }

    private void updateSelectedColorIndex(int newSelected) {
        old_selected_color = current_selected_color;
        current_selected_color = newSelected;
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

                long uid = habitWithSubroutinesViewModel.insertHabit(habit);

                if (!subroutinesList.isEmpty()) {
                    for (Subroutines subroutine : subroutinesList) {
                        subroutine.setFk_habit_uid(uid);
                    }
                    habitWithSubroutinesViewModel.insertSubroutines(subroutinesList);
                }
                returnHomeFragment();
            }
        });
    }

    private void insertSubroutine() {
        binding.addNewHabitSubroutineBtn.setOnClickListener(view -> {
            HomeAddNewInsertSubroutineDialogFragment dialog = new HomeAddNewInsertSubroutineDialogFragment();
            dialog.setTargetFragment(getChildFragmentManager()
                    .findFragmentById(AddNewHabitFragment.this.getId()), 1);
            dialog.show(getChildFragmentManager(), "HomeAddNewInsertSubroutineDialog");
        });
    }

    private void deleteHabit() {
        if (binding.fabAddDeleteHabit.getVisibility() == View.VISIBLE) {
            binding.fabAddDeleteHabit.setOnClickListener(view -> {
                //Delete Habit and its subroutines and comments
                habitWithSubroutinesViewModel.deleteHabit(habit_snapshot);
                Toast.makeText(requireActivity(), habit_snapshot.getHabit() + "", Toast.LENGTH_SHORT).show();

                habitWithSubroutinesViewModel.deleteSubroutineList(subroutinesList_snapshot);
            });
        }
    }

    private void setRecyclerViewAdapter() {
        if (subroutinesList != null) {
            if (subroutinesList.isEmpty()) {
                binding.addNewHabitSubroutineCardView.setVisibility(View.GONE);
            } else {
                binding.addNewHabitSubroutineCardView.setVisibility(View.VISIBLE);
            }

            if (homeAddNewHabitParentAdapter == null)
                homeAddNewHabitParentAdapter = new HomeAddNewHabitParentAdapter(subroutinesList);

            homeAddNewHabitParentAdapter.setOnClickListener(new HomeAddNewHabitParentAdapter.OnClickListener() {
                @Override
                public void onDelete(Subroutines subroutine) {
                    HomeAddNewInsertSubroutineDialogFragment dialog = new HomeAddNewInsertSubroutineDialogFragment(subroutine, true);
                    dialog.setTargetFragment(getChildFragmentManager().findFragmentById(AddNewHabitFragment.this.getId()), 1);
                    dialog.show(getChildFragmentManager(), "TAG");
                }

                @Override
                public void onModify(Subroutines subroutine) {
                    HomeAddNewInsertSubroutineDialogFragment dialog = new HomeAddNewInsertSubroutineDialogFragment(subroutine);
                    dialog.setTargetFragment(getChildFragmentManager().findFragmentById(AddNewHabitFragment.this.getId()), 1);
                    dialog.show(getChildFragmentManager(), "TAG");
                }
            });

            if (binding.addNewHabitSubroutineRecyclerView.getAdapter() == null) {
                binding.addNewHabitSubroutineRecyclerView.setAdapter(homeAddNewHabitParentAdapter);
            } else {
                homeAddNewHabitParentAdapter.setSubroutinesList(subroutinesList);
            }
        }
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                returnHomeFragment();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void returnHomeFragment() {
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(binding.addNewHabitFrameLayout.getId(), new HomeFragment())
                .commit();
        binding.addNewHabitContainer.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        habitWithSubroutinesViewModel.getAllUserDefinedHabitListLiveData().removeObservers(getViewLifecycleOwner());
        habitWithSubroutinesViewModel = null;
        binding = null;
    }
}