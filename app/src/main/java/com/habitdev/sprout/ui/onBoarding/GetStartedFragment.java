package com.habitdev.sprout.ui.onBoarding;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.habitdev.sprout.R;
import com.habitdev.sprout.databinding.FragmentGetStartedBinding;
import com.habitdev.sprout.enums.BundleKeys;
import com.habitdev.sprout.enums.OnBoardingConfigurationKeys;

public class GetStartedFragment extends Fragment {

    // View Binding
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
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_getStarted_to_personalization);
        });

        onBackPressed();
    }

    private void onBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
//              showToast();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }


    /**
     * Example of custom toast
     */
    private void showToast() {
//        TODO: REMOVE THIS REPLACE WTITH SNACKBAR
        final CharSequence MESSAGE = "DATA IS ALREADY SAVED";

        final int duration = Toast.LENGTH_LONG;

        View layout = getLayoutInflater().inflate(R.layout.get_started_custom_toast, binding.getRoot().findViewById(R.id.custom_toast_layout));
        TextView text = layout.findViewById(R.id.custom_toast_lbl);
        text.setText(MESSAGE);

        Toast toast = new Toast(requireContext());
//        E/Toast: setGravity() shouldn't be called on text toasts, the values won't be used
//        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layout);
        toast.setDuration(duration);
        toast.show();
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