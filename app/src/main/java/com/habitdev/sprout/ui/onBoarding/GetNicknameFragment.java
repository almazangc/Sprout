package com.habitdev.sprout.ui.onBoarding;

import android.os.Bundle;
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

    public GetNicknameFragment() {
        // Required empty public constructor
    }

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
        binding.btnContinue.setOnClickListener(view -> {
            nickname = Objects.requireNonNull(binding.editNickname.getText()).toString();
            binding.editNicknameContainer.setHelperText(validate_nickname(nickname));
            if (binding.editNicknameContainer.getHelperText() == null){
                Bundle bundle = getArguments();
                assert bundle != null;
                bundle.putString(BundleKeys.NICKNAME.getKEY(), nickname);
                Navigation.findNavController(view).navigate(R.id.action_navigate_from_getNickname_to_getIdentity, bundle);
            }
        });
        onBackPress();
    }

    private String validate_nickname(String nickname) {
        if (nickname.trim().isEmpty()){
            return "Required*";
        } else if (nickname.length() > 15 || nickname.length() < 3 ) {
            return "Minimum of 3, Maximum of 15 Characters*";
        } else if (!Pattern.compile("^[a-zA-Z ]*$").matcher(nickname).matches()){
//            Allowed Input a-zA-Z space
            return "Invalid nickname*";
        }
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        onBackPress();
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