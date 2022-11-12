package com.prototype.sprout.ui.menu.subroutine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.prototype.sprout.databinding.FragmentSubroutineBinding;

public class SubroutineFragment extends Fragment {

    FragmentSubroutineBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSubroutineBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}