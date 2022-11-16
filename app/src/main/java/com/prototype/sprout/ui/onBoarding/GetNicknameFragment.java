package com.prototype.sprout.ui.onBoarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.prototype.sprout.R;
import com.prototype.sprout.databinding.FragmentGetNicknameBinding;
import com.prototype.sprout.model.BundleKey;

import java.util.Objects;

public class GetNicknameFragment extends Fragment {

    //View Binding
    private FragmentGetNicknameBinding binding;

    public GetNicknameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetNicknameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.btnContinue.setOnClickListener(view -> {
            String nickname = Objects.requireNonNull(binding.editTextTextPersonName.getText()).toString();
            if (nickname.trim().isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a nickname", Toast.LENGTH_SHORT).show();
            } else {
                if (nickname.length() <= 15){
                    Bundle bundle = getArguments();
                    assert bundle != null;
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