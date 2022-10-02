package com.prototype.sprout.ui;

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
import com.prototype.sprout.model.BundleKey;


public class StartupFragment extends Fragment {

    /**
     * Startup Fragment View Binding
     */
    private FragmentStartupBinding binding;

    /**
     * Auto Generated Empty Public Constructor
     */
    public StartupFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStartupBinding.inflate(inflater, container, false);

        boolean isOnBoardingDone = false;

        Bundle bundle = getArguments();
        if (bundle != null) {
            isOnBoardingDone = bundle.getBoolean(new BundleKey().getKEY_ANALYSIS());
        } else {
            UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
            isOnBoardingDone = userViewModel.getOnBoarding();
        }

        if (!isOnBoardingDone) NavHostFragment.findNavController(StartupFragment.this).navigate(R.id.action_startup_to_onboarding);

        if (isOnBoardingDone) NavHostFragment.findNavController(StartupFragment.this).navigate(R.id.action_startupFragment_to_main);

            onBackPress();

        binding.btnMain.setText("BANANA!!!");
        return binding.getRoot();
    }


    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Do Something
                Log.d("TAG", "handleOnBackPressed: Disabled");
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