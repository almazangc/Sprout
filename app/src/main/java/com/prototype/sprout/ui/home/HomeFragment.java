package com.prototype.sprout.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.prototype.sprout.R;
import com.prototype.sprout.database.user.User;
import com.prototype.sprout.database.user.UserViewModel;
import com.prototype.sprout.databinding.FragmentHomeBinding;

import java.util.List;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUserListLiveData().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                String text = "Welcome again now now " + users.get(0).getNickname();

                binding.lblWelcomeMsg.setText(text);
            }
        });
        return binding.getRoot();
    }
}