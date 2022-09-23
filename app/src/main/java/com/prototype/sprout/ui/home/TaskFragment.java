package com.prototype.sprout.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prototype.sprout.R;
import com.prototype.sprout.databinding.FragmentHomeBinding;
import com.prototype.sprout.databinding.FragmentTaskBinding;

public class TaskFragment extends Fragment {

    FragmentTaskBinding binding;

    public TaskFragment() {
        // Empty Constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}