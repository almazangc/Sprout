package com.habitdev.sprout.ui.menu.setting.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.habitdev.sprout.databinding.FragmentAboutUsBinding;
import com.habitdev.sprout.utill.network.NetworkMonitoringUtil;
import com.habitdev.sprout.utill.network.NetworkStateManager;

public class AboutUsFragment extends Fragment {

    private FragmentAboutUsBinding binding;

    public interface OnReturnSetting {
        void returnFromAboutUsToSetting();
    }

    private OnReturnSetting mOnReturnSetting;

    public void setmOnReturnSetting(OnReturnSetting mOnReturnSetting) {
        this.mOnReturnSetting = mOnReturnSetting;
    }

    public AboutUsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false);

        binding.sample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFacebookProfile("https://www.facebook.com/gcalmazan/");
            }
        });
        onBackPress();
        return binding.getRoot();
    }


    /**
     * Opens a Facebook profile link using the Facebook app if it is installed, or the device's default browser if it is not.
     * @param profileUrl The Facebook profile link to be opened.
     */
    public void openFacebookProfile(String profileUrl) {
        ConnectivityManager connectivityManager;
        NetworkMonitoringUtil networkMonitoringUtil;

        connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkMonitoringUtil = new NetworkMonitoringUtil(getContext());

        Intent facebookIntent;
        NetworkStateManager networkStateManager = NetworkStateManager.getInstance();
        LiveData<Boolean> networkConnectivityStatus = networkStateManager.getNetworkConnectivityStatus();
        boolean isNetworkConnected = networkConnectivityStatus.getValue() == null ? false : networkConnectivityStatus.getValue();
        if (isNetworkConnected) {
            try {
                // Try to open the link in the Facebook app
                facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + profileUrl));
                startActivity(facebookIntent);
            } catch (Exception e) {
                if (e instanceof ActivityNotFoundException) {
                    // If the Facebook app is not installed, open the link in the device's default browser
                    facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(profileUrl));
                    startActivity(facebookIntent);
                } else {
                    // Handle any other exceptions
                    Toast.makeText(getContext(), "An error occurred while trying to open the Facebook profile.", Toast.LENGTH_SHORT).show();
                }
            } finally {
                // unregister the network callback
                if (connectivityManager != null && networkMonitoringUtil != null) {
                    connectivityManager.unregisterNetworkCallback(networkMonitoringUtil);
                }
            }
        } else {
            Toast.makeText(getContext(), "No internet connection available", Toast.LENGTH_SHORT).show();
            connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            networkMonitoringUtil = new NetworkMonitoringUtil(getContext());
            networkMonitoringUtil.registerNetworkCallbackEvents();
        }
    }


            private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOnReturnSetting != null) mOnReturnSetting.returnFromAboutUsToSetting();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}