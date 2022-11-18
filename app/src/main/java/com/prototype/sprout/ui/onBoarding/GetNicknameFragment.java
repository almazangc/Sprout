package com.prototype.sprout.ui.onBoarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.prototype.sprout.R;
import com.prototype.sprout.databinding.FragmentGetNicknameBinding;
import com.prototype.sprout.model.BundleKey;

import java.util.Objects;
import java.util.regex.Pattern;

public class GetNicknameFragment extends Fragment {

    //View Binding
    private FragmentGetNicknameBinding binding;

    public GetNicknameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            String nickname = Objects.requireNonNull(binding.editNickname.getText()).toString();

            binding.editNicknameContainer.setHelperText(validate_nickname(nickname));

            if (binding.editNicknameContainer.getHelperText() == null){
                Bundle bundle = getArguments();
                assert bundle != null;
                bundle.putString(new BundleKey().getKEY_NICKNAME(), nickname);
                Navigation.findNavController(view).navigate(R.id.action_navigate_from_getNickname_to_getIdentity, bundle);
            }
        });
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}