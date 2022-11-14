package com.prototype.sprout.ui.onBoarding.assessment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.prototype.sprout.R;
import com.prototype.sprout.database.habits_with_subroutines.HabitWithSubroutinesViewModel;
import com.prototype.sprout.database.user.UserViewModel;
import com.prototype.sprout.databinding.FragmentAnalysisBinding;
import com.prototype.sprout.model.BundleKey;

import java.util.List;

public class AnalysisFragment extends Fragment {

    private FragmentAnalysisBinding binding;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAnalysisBinding.inflate(inflater, container, false);

        //Recommender Algorithm Here to display result according to the analysis

        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        List<String> habits = habitWithSubroutinesViewModel.getAllHabitTitle();

        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(requireContext(), R.layout.adapter_analysis_habit_item, habits);
        binding.dropItems.setAdapter(adapterItems);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.btnContinue.setOnClickListener(view -> {
            setOnBoarding();
            Bundle bundle = new Bundle();
            bundle.putBoolean(new BundleKey().getKEY_ANALYSIS(), true);
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_analysis_to_Home, bundle);
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Toast.makeText(requireContext(), "You shall not amend", Toast.LENGTH_SHORT).show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setOnBoarding(){
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setOnBoarding();
    }
}