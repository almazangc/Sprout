package com.habitdev.sprout.ui.onBoarding.personalizationAssessment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentAnalysisBinding;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.enums.BundleKeys;
import com.habitdev.sprout.ui.onBoarding.personalizationAssessment.adapter.AnalysisParentItemAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AnalysisFragment extends Fragment {

    private FragmentAnalysisBinding binding;
    private static HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private static List<Habits> habitsList;
    private static Habits habit;
    private static AnalysisParentItemAdapter analysisParentItemAdapter;
    private static List<Subroutines> subroutinesList;

    public AnalysisFragment() {
        habit = new Habits(
                null,
                null,
                AppColor.CLOUDS.getColor(),
                false,
                false
        );
        subroutinesList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAnalysisBinding.inflate(inflater, container, false);
        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        toggleHabitDescriptionVisibility(false);
        setDropDownItems();
        setRecyclerViewAdapter();
        setDropDownItemListener();
        setContinueListener();
    }

    private void setDropDownItems(){
        //Recommender Algorithm Here to display result according to the analysis
        // the habits suggested should adjust depending on what user score or answers on assessment
        List<String> habitTitles = new ArrayList<>();
        habitWithSubroutinesViewModel.getAllHabitListLiveData().observe(getViewLifecycleOwner(), habits -> {
            habitsList = habits;
            for (Habits habit : habits) {
                habitTitles.add(habit.getHabit());
            }
        });

        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(requireContext(), R.layout.adapter_analysis_parent_habit_item, habitTitles);
        binding.analysisDropItem.setAdapter(adapterItems);
    }

    private void toggleHabitDescriptionVisibility(boolean visibility){
        if (!visibility) {
            binding.analysisHabitDescrition.setVisibility(View.GONE);
            binding.analysisHabitDescritionLbl.setVisibility(View.GONE);
        } else {
            binding.analysisHabitDescrition.setVisibility(View.VISIBLE);
            binding.analysisHabitDescritionLbl.setVisibility(View.VISIBLE);
        }
    }

    private void setRecyclerViewAdapter(){
        if (subroutinesList != null){
            if (analysisParentItemAdapter == null) {
                analysisParentItemAdapter = new AnalysisParentItemAdapter();
                analysisParentItemAdapter.setOldSubroutinesList(subroutinesList);
            }

            if (binding.analysisRecyclerView.getAdapter() == null) {
                binding.analysisRecyclerView.setAdapter(analysisParentItemAdapter);
            } else {
                binding.analysisHabitDescrition.setText(habit.getDescription());
                analysisParentItemAdapter.setNewSubroutineList(subroutinesList);
            }
        }
    }

    private void setDropDownItemListener() {
        binding.analysisDropItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                habit = habitsList.get(position);
                subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid());
                toggleHabitDescriptionVisibility(true);
                binding.analysisHabitDescrition.setText(habit.getDescription());
                setRecyclerViewAdapter();
            }
        });

        binding.analysisDropItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    subroutinesList.clear();
                    toggleHabitDescriptionVisibility(false);
                    setRecyclerViewAdapter();
                }
            }
        });
    }

    private void setContinueListener() {
        binding.analysisContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                habit.setOnReform(true);
                habit.setDate_started(new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss a", Locale.getDefault()).format(new Date()));
                habitWithSubroutinesViewModel.updateHabit(habit);

                setOnBoarding();
                Bundle bundle = new Bundle();
                bundle.putBoolean(BundleKeys.ANALYSIS.getKEY(), true);
                Navigation.findNavController(view).navigate(R.id.action_navigate_from_analysis_to_Home, bundle);
            }
        });
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Block on back press
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void setOnBoarding() {
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setOnBoarding();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        habitWithSubroutinesViewModel.getAllHabitListLiveData().removeObservers(getViewLifecycleOwner());
        habitWithSubroutinesViewModel = null;
        binding = null;
    }
}