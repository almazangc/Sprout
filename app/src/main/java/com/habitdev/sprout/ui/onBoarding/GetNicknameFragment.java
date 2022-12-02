package com.habitdev.sprout.ui.onBoarding;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.habitdev.sprout.R;
import com.habitdev.sprout.databinding.FragmentGetNicknameBinding;
import com.habitdev.sprout.enums.BundleKeys;

import java.util.Objects;
import java.util.regex.Pattern;

public class GetNicknameFragment extends Fragment {

    //View Binding
    private FragmentGetNicknameBinding binding;
    private String nickname;

    public GetNicknameFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetNicknameBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(BundleKeys.NICKNAME.getKEY())) nickname = bundle.getString(BundleKeys.NICKNAME.getKEY());
        if (nickname != null) binding.editNickname.setText(nickname);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        validate_nickname();
        binding.btnContinue.setOnClickListener(view -> {
            nickname = Objects.requireNonNull(binding.editNickname.getText()).toString();
            if (binding.editNicknameContainer.getHelperText() == null){
                Bundle bundle = getArguments();
                assert bundle != null;
                bundle.putString(BundleKeys.NICKNAME.getKEY(), nickname);
                Navigation.findNavController(view).navigate(R.id.action_navigate_from_getNickname_to_getIdentity, bundle);
            }
        });
        onBackPress();
    }

    private void validate_nickname() {
        binding.editNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty()){
                    binding.editNicknameContainer.setHelperText("Required*");
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()){
                    binding.editNicknameContainer.setHelperText("Required*");
                } else if (editable.toString().trim().length() > 15 || editable.toString().trim().length() < 3 ) {
                    binding.editNicknameContainer.setHelperText("Minimum of 3, Maximum of 15 Characters*");
                } else if (!Pattern.compile("^[a-zA-Z ]*$").matcher(editable.toString().trim()).matches()){
//            Allowed Input a-zA-Z space
                    binding.editNicknameContainer.setHelperText("Invalid nickname*");
                } else {
                    binding.editNicknameContainer.setHelperText("");
                }
            }
        });
    }

    /**
     * Handles onBackPress Key
     */
    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                nickname = Objects.requireNonNull(binding.editNickname.getText()).toString();
                Bundle bundle = getArguments();
                if (!Objects.requireNonNull(bundle).containsKey(BundleKeys.NICKNAME.getKEY())) bundle.putString(BundleKeys.NICKNAME.getKEY(), nickname);
                Navigation.findNavController(requireView()).navigate(R.id.action_navigate_from_getNickname_to_greetings, bundle);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}