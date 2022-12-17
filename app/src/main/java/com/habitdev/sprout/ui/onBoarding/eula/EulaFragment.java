package com.habitdev.sprout.ui.onBoarding.eula;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.habitdev.sprout.R;
import com.habitdev.sprout.databinding.FragmentEulaBinding;
import com.habitdev.sprout.enums.BundleKeys;

public class EulaFragment extends Fragment {

    private FragmentEulaBinding binding;

    public EulaFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEulaBinding.inflate(inflater, container, false);
        binding.lblEULA.setText(getString(R.string.sampleText));
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.btnAgree.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(BundleKeys.EULA.getKEY(), true);
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_eula_to_getCommonWakeup, bundle);
        });

        binding.btnDisagree.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_navigate_from_eula_to_initial));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}