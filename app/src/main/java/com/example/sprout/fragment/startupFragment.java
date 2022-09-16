package com.example.sprout.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sprout.database.AppDatabase;
import com.example.sprout.R;
import com.example.sprout.databinding.FragmentStartupBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link startupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class startupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Vew Binding
    private FragmentStartupBinding binding;

    public startupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment startupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static startupFragment newInstance(String param1, String param2) {
        startupFragment fragment = new startupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStartupBinding.inflate(inflater, container, false);

        boolean isUserRegistered = AppDatabase.getDbInstance(requireContext())
                .userDao()
                .getAllUser()
                .isEmpty();
        boolean isAssestmentDone = (AppDatabase.getDbInstance(requireContext())
                .assestmentDao()
                .getALLAssestment()
                .isEmpty());

        if (!isUserRegistered) {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigate_from_startup_to_personalization);
        } else if(isAssestmentDone) {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigate_from_startup_to_initial);
        } else {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigate_from_startup_to_analysis);
        }
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}