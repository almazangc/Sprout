package com.example.sprout.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sprout.R;
import com.example.sprout.databinding.FragmentGetNicknameBinding;
import com.example.sprout.model.BundleKey;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link getNicknameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class getNicknameFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //View Binding
    private FragmentGetNicknameBinding binding;

    public getNicknameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment nicknameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static getNicknameFragment newInstance(String param1, String param2) {
        getNicknameFragment fragment = new getNicknameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetNicknameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.btnContinue.setOnClickListener(view -> {
            String nickname = binding.editTextTextPersonName.getText().toString();
            if (nickname.equals("")) {
                Toast.makeText(requireContext(), "Please enter a nickname", Toast.LENGTH_SHORT).show();
            } else {
                Bundle bundle = getArguments();
                bundle.putString(new BundleKey().getKEY_NICKNAME(), nickname);
                Navigation.findNavController(view).navigate(R.id.action_navigate_from_getNickname_to_getIdentity, bundle);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}