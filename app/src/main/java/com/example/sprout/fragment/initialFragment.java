package com.example.sprout.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sprout.R;
import com.example.sprout.databinding.FragmentInitialBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link initialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class initialFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //View Binding
    private FragmentInitialBinding binding;

    public initialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment initialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static initialFragment newInstance(String param1, String param2) {
        initialFragment fragment = new initialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInitialBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.btnLetsDoIt.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_initial_to_eula);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}