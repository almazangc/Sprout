package com.habitdev.sprout.ui.menu.setting.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.database.user.model.User;
import com.habitdev.sprout.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public interface OnReturnSetting {
        void returnFromProfileToSetting();
    }

    private OnReturnSetting mOnReturnSetting;

    public void setmOnReturnSetting(OnReturnSetting mOnReturnSetting) {
        this.mOnReturnSetting = mOnReturnSetting;
    }

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        final UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        User user = userViewModel.getUserByUID(1);

        userViewModel.getUserNickname().observe(getViewLifecycleOwner(), nickname -> {
            binding.settingProfileChangeNickname.setText(nickname);
        });

        binding.settingProfileSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update nickname
                if (!binding.settingProfileChangeNickname.getText().toString().trim().isEmpty()) {
                    user.setNickname(binding.settingProfileChangeNickname.getText().toString().trim());
                    userViewModel.update(user);
                } else {
                    Toast.makeText(requireActivity(), "Empty Nickname", Toast.LENGTH_SHORT).show();
                }
            }
        });

        onBackPress();
        return binding.getRoot();
    }



    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOnReturnSetting != null) mOnReturnSetting.returnFromProfileToSetting();
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