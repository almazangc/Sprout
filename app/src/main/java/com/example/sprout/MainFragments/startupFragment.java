package com.example.sprout.MainFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.sprout.Database.AppDatabase;
import com.example.sprout.Database.User.User;
import com.example.sprout.R;
import com.example.sprout.databinding.FragmentStartupBinding;

import java.util.List;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStartupBinding.inflate(inflater, container, false);
        binding.btnMain.setOnClickListener(view -> {
            List<User> userList = AppDatabase.getDbInstance(requireActivity().getApplicationContext()).userDao().getAllUser();
            if (userList.isEmpty()) {
                Navigation.findNavController(view).navigate(R.id.action_navigate_from_startup_to_initial);
            } else {
////                startActivity((new Intent(this, Personalization.class)));
//                startActivity((new Intent(this, Analysis.class)));
                Log.d("TAG", "onCreateView: YEY");
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}