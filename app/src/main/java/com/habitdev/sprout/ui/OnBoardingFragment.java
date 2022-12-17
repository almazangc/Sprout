package com.habitdev.sprout.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.database.user.model.User;
import com.habitdev.sprout.databinding.FragmentOnBoardingBinding;

import java.util.List;

public class OnBoardingFragment extends Fragment {

    private FragmentOnBoardingBinding binding;
    private static boolean isAssessmentDone = false;
    private static int userCount = 0;

    public OnBoardingFragment() {}

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

        List<User> userList = userViewModel.getUserList();
        if (userList != null && !userList.isEmpty()) {
            userCount = userViewModel.getUserCount();
            isAssessmentDone = userList.get(0).isAssessmentDone();
        }

        if (userCount == 1 && !isAssessmentDone) {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigate_from_onboarding_to_personalization);
        } else if (!isAssessmentDone) {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigate_from_onboarding_to_initial);
        } else {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigate_from_onboarding_to_analysis);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}