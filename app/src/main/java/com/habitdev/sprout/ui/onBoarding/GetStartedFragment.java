package com.habitdev.sprout.ui.onBoarding;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.habitdev.sprout.R;
import com.habitdev.sprout.databinding.FragmentGetStartedBinding;
import com.habitdev.sprout.enums.BundleKeys;
import com.habitdev.sprout.enums.OnBoardingConfigurationKeys;
import com.habitdev.sprout.ui.menu.OnBackPressDialogFragment;

public class GetStartedFragment extends Fragment {

    private FragmentGetStartedBinding binding;

    public GetStartedFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetStartedBinding.inflate(inflater, container, false);
        assert getArguments() != null;
        binding.lblName.setText(getArguments().getString(BundleKeys.NICKNAME.getKEY()));
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.btnContinue.setOnClickListener(view -> {
            clearWakeAndSleepSharedPref();
            //TODO: //change to assessment briefing layout
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_getStarted_to_personalizationBriefing);
        });

        onBackPress();
    }

    private void onBackPress() {
        final int[] keypress_count = {0};
        final boolean[] isOnBackPressDialogShowing = {false};

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                keypress_count[0]++;

                new CountDownTimer(200, 200) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        if (keypress_count[0] > 1) {
                            //Dialog is displayed twice
                            OnBackPressDialogFragment dialog = new OnBackPressDialogFragment();
                            if (!isOnBackPressDialogShowing[0]) {
                                dialog.setTargetFragment(getChildFragmentManager().findFragmentById(GetStartedFragment.this.getId()), 1);
                                dialog.show(getChildFragmentManager(), "Menu.onBackPress");
                                dialog.setmOnCancelDialog(() -> {
                                    keypress_count[0] = 0;
                                    isOnBackPressDialogShowing[0] = false;
                                });
                                isOnBackPressDialogShowing[0] = true;
                            }
                        } else {
                            requireActivity().moveTaskToBack(true);
                            keypress_count[0] = 0;
                        }
                        this.cancel();
                    }
                }.start();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void clearWakeAndSleepSharedPref(){
        requireActivity().getSharedPreferences(OnBoardingConfigurationKeys.WAKEUP_SHAREDPREF.getKey(), Context.MODE_PRIVATE).edit().clear().apply();
        requireActivity().getSharedPreferences(OnBoardingConfigurationKeys.SLEEP_SHAREDPREF.getKey(), Context.MODE_PRIVATE).edit().clear().apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}