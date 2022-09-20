package com.prototype.sprout.fragment.onBoarding;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prototype.sprout.R;
import com.prototype.sprout.database.User.UserViewModel;
import com.prototype.sprout.databinding.FragmentOnBoardingBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link onBoardingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class onBoardingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentOnBoardingBinding binding;

    public onBoardingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment onBoardingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static onBoardingFragment newInstance(String param1, String param2) {
        onBoardingFragment fragment = new onBoardingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false);

        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        if (userViewModel.getUserCount() == 1 & !userViewModel.getAssessment()) {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigate_from_onboarding_to_personalization);
        } else if (userViewModel.getAssessment()) {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigate_from_onboarding_to_analysis);
        } else {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigate_from_onboarding_to_initial);
        }

        return binding.getRoot();
    }
}