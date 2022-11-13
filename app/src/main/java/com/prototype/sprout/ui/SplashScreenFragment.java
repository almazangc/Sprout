package com.prototype.sprout.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.prototype.sprout.R;
import com.prototype.sprout.database.user.UserViewModel;
import com.prototype.sprout.databinding.FragmentSplashScreenBinding;
import com.prototype.sprout.model.BundleKey;


@SuppressLint("CustomSplashScreen")
public class SplashScreenFragment extends Fragment {

    /**
     * Startup Fragment View Binding
     */
    private FragmentSplashScreenBinding binding;

    /**
     * Auto Generated Empty Public Constructor
     */
    public SplashScreenFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false);
        checkStatus();
        onBackPress();
        return binding.getRoot();
    }


    /**
     * Check Status of User (Handles Display at app_start)
     */
    private void checkStatus() {
        new Handler().postDelayed(() -> {
            //Loading intents of fragments
            boolean isOnBoardingDone;

            Bundle bundle = SplashScreenFragment.this.getArguments();
            if (bundle != null) {
                isOnBoardingDone = bundle.getBoolean(new BundleKey().getKEY_ANALYSIS());
            } else {
                UserViewModel userViewModel = new ViewModelProvider(SplashScreenFragment.this.requireActivity()).get(UserViewModel.class);
                isOnBoardingDone = userViewModel.getOnBoarding();
            }

            if (!isOnBoardingDone)
                NavHostFragment.findNavController(SplashScreenFragment.this).navigate(R.id.action_splashscreen_to_onboarding);
            if (isOnBoardingDone)
                NavHostFragment.findNavController(SplashScreenFragment.this).navigate(R.id.action_splashscreen_to_main);
                onDestroyView();
        }, 2000);
    }


    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Do Nothing
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}