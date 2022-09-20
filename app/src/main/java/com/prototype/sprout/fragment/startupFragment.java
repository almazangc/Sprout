package com.prototype.sprout.fragment;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.prototype.sprout.R;
import com.prototype.sprout.database.Assessment.Assessment;
import com.prototype.sprout.database.User.User;
import com.prototype.sprout.database.User.UserViewModel;
import com.prototype.sprout.databinding.FragmentStartupBinding;
import com.prototype.sprout.model.OnBackPressHandler;

import java.util.List;


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