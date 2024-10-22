package com.habitdev.sprout.ui.menu.setting.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.habitdev.sprout.R;
import com.habitdev.sprout.activity.startup.Main;
import com.habitdev.sprout.databinding.FragmentTechStackInfoBinding;

import java.util.List;

public class TechStackInfoFragment extends Fragment {

    private FragmentTechStackInfoBinding binding;

    public interface OnReturnSetting {
        void returnFromTechStackInfoToSetting();
    }

    private OnReturnSetting mOnReturnSetting;

    public void setmOnReturnSetting(OnReturnSetting mOnReturnSetting) {
        this.mOnReturnSetting = mOnReturnSetting;
    }

    public TechStackInfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTechStackInfoBinding.inflate(inflater, container, false);
        setBackground();
        openRepository();
        sendMail();
        readEULA();
        onBackPress();
        return binding.getRoot();
    }

    private void setBackground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // The device's SDK version is greater than or equal to 28, so continue executing the app
            final String SharedPreferences_KEY = "SP_DB";
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SharedPreferences_KEY, Main.MODE_PRIVATE);

            final String SHARED_PREF_KEY = "THEME_SHARED.PREF";
            int theme = sharedPreferences.getInt(SHARED_PREF_KEY, -1);
            if (theme == 1) {
                binding.techStackInfoFrameLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.background_image_light));
            } else if (theme == 2) {
                binding.techStackInfoFrameLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.background_image_night));
            } else {
                binding.techStackInfoFrameLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.background_image_light));
            }
        }
    }

    public void openRepository() {
        binding.githubRepository.setOnClickListener(view -> openGitHubURL("https://github.com/DOMO-Dom/Sprout"));
    }

    public void sendMail() {
        binding.sendMail.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:sproutdev.technology@gmail.com"));
            startActivity(intent);
        });
    }

    private void readEULA() {
        binding.readEula.setOnClickListener(view -> openGitHubURL("https://github.com/DOMO-Dom/Sprout/blob/main/app/src/main/assets/sprout_generated_eula.md"));
    }

    /**
     * Open the link provided on Github Application if installed,
     * Otherwise opens in default browser
     *
     * @param url link of the site
     */
    private void openGitHubURL(String url) {
        Intent githubIntent = requireActivity().getPackageManager().getLaunchIntentForPackage("com.github.android");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        List<ResolveInfo> resolveInfoList = requireActivity().getPackageManager().queryIntentActivities(browserIntent, 0);
        if (githubIntent != null) {
            // Create a chooser dialog to allow user to select between GitHub app and browser
            Intent chooserIntent = Intent.createChooser(githubIntent, "Open with");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, resolveInfoList.toArray(new Parcelable[0]));
            startActivity(chooserIntent);
        } else {
            startActivity(browserIntent);
        }
    }


    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOnReturnSetting != null) mOnReturnSetting.returnFromTechStackInfoToSetting();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}