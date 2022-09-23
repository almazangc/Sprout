package com.prototype.sprout.ui.onBoarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.prototype.sprout.R;
import com.prototype.sprout.databinding.FragmentGetNicknameBinding;
import com.prototype.sprout.model.BundleKey;

public class GetNicknameFragment extends Fragment {

    //View Binding
    private FragmentGetNicknameBinding binding;

    public GetNicknameFragment() {
        // Required empty public constructor
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
                if (nickname.length() <= 15){
                    Bundle bundle = getArguments();
                    bundle.putString(new BundleKey().getKEY_NICKNAME(), nickname);
                    Navigation.findNavController(view).navigate(R.id.action_navigate_from_getNickname_to_getIdentity, bundle);
                } else {
                    Toast.makeText(requireContext(), "Please enter within the limit", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}