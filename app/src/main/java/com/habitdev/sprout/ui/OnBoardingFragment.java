package com.habitdev.sprout.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentOnBoardingBinding;

public class OnBoardingFragment extends Fragment {

    private FragmentOnBoardingBinding binding;
    private boolean isAssessmentDone = false;
    private int userCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false);
        checkProgress();
        return binding.getRoot();
    }

    /**
     * Check Status of User (Handles Display Onboarding Progress)
     */
    private void checkProgress(){
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUserListLiveData().observe(getViewLifecycleOwner(), assessmentList -> {
            if (assessmentList == null){
                userCount = 0;
                isAssessmentDone = false;
            } else {
                userCount = assessmentList.size();
                if (assessmentList.size() > 0 )isAssessmentDone = assessmentList.get(0).isAssessmentDone();
            }
        });

        if (userCount == 1 & !isAssessmentDone) {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigate_from_onboarding_to_personalization);
        } else if (isAssessmentDone) {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigate_from_onboarding_to_analysis);
        } else {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigate_from_onboarding_to_initial);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}