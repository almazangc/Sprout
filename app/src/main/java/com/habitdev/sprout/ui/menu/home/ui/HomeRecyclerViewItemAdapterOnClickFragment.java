package com.habitdev.sprout.ui.menu.home.ui;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.habitdev.sprout.R;
import com.habitdev.sprout.databinding.FragmentHomeRecyclerViewItemAdapterOnClickViewBinding;
import com.habitdev.sprout.ui.menu.home.HomeFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeRecyclerViewItemAdapterOnClickFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeRecyclerViewItemAdapterOnClickFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentHomeRecyclerViewItemAdapterOnClickViewBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeRecyclerViewItemAdapterOnClickFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeRecyclerViewItemAdapterOnClickView.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeRecyclerViewItemAdapterOnClickFragment newInstance(String param1, String param2) {
        HomeRecyclerViewItemAdapterOnClickFragment fragment = new HomeRecyclerViewItemAdapterOnClickFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeRecyclerViewItemAdapterOnClickViewBinding.inflate(inflater, container,false);
        onBackPress();
        return binding.getRoot();
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Returns to Home
                HomeFragment homeFragment = new HomeFragment();
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(binding.homeItemAdapterOnClickFrameLayout.getId(), homeFragment)
                        .commit();
                binding.homeItemAdapterOnClickContainer.setVisibility(View.GONE);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
}