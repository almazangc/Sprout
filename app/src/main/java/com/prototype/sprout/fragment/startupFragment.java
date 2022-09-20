package com.prototype.sprout.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.prototype.sprout.R;
import com.prototype.sprout.database.user.UserViewModel;
import com.prototype.sprout.databinding.FragmentStartupBinding;


public class startupFragment extends Fragment {

    //Vew Binding
    private FragmentStartupBinding binding;

    public startupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStartupBinding.inflate(inflater, container, false);

        boolean isOnBoardingDone;

        Bundle bundle = getArguments();
        if (bundle != null) {
            isOnBoardingDone = bundle.getBoolean("x");
        } else {
            UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
            isOnBoardingDone = userViewModel.getOnBoarding();
        }

        if (!isOnBoardingDone) NavHostFragment.findNavController(startupFragment.this).navigate(R.id.action_startup_to_onboarding);

        onBackPress();

        binding.btnMain.setText("BANANA!!!");
        return binding.getRoot();
    }


    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Do Something
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("TAG", "onDestroyView: ");
        binding = null;
    }
}