package com.prototype.sprout.ui.onBoarding;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.prototype.sprout.R;
import com.prototype.sprout.database.user.UserViewModel;
import com.prototype.sprout.databinding.FragmentOnBoardingBinding;

public class OnBoardingFragment extends Fragment {

    private FragmentOnBoardingBinding binding;
    private UserViewModel userViewModel;
    private boolean isAssessmentDone = false;
    private int userCount = 0;

    public OnBoardingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false);

        this.userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUserListLiveData().observe(getViewLifecycleOwner(), assessmentList -> {
            Log.d("TAG", "onCreateView: Onboard" );
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
        return binding.getRoot();
    }
}