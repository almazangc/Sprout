package com.example.sprout.fragment.onBoarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.sprout.R;
import com.example.sprout.databinding.FragmentEulaBinding;
import com.example.sprout.model.BundleKey;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link eulaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class eulaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //View Binding
    private FragmentEulaBinding binding;

    public eulaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment eula.
     */
    // TODO: Rename and change types and number of parameters
    public static eulaFragment newInstance(String param1, String param2) {
        eulaFragment fragment = new eulaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEulaBinding.inflate(inflater, container, false);
        binding.lblEULA.setText(getString(R.string.sampleText));
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.btnAgree.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(new BundleKey().getKEY_EULA(), true);
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_eula_to_getCommonWakeup, bundle);
        });
        binding.btnDisagree.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_eula_to_startup);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}