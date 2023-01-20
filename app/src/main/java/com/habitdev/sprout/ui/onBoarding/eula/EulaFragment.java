package com.habitdev.sprout.ui.onBoarding.eula;

import android.content.Intent;
import android.net.Uri;
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
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.btnAccept.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(BundleKeys.EULA.getKEY(), true);
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_eula_to_getCommonWakeup, bundle);
        });

        binding.btnDecline.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_navigate_from_eula_to_initial));
        sendEmail();
    }

    public void sendEmail() {
        binding.contactEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:sproutdev.technology@gmail.com"));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}