package com.habitdev.sprout.ui.onBoarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.user.User;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentGetIdentityBinding;
import com.habitdev.sprout.enums.BundleKeys;

public class GetIdentityFragment extends Fragment {

    // View Binding
    private FragmentGetIdentityBinding binding;
    private String nickname;
    private String identity;
    private int wakeHour;
    private int wakeMinute;
    private int sleepHour;
    private int sleepMinute;
    private boolean eula;

    public GetIdentityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetIdentityBinding.inflate(inflater, container, false);
        getBundleArgs();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.btnContinue.setOnClickListener(view -> {
            setIdentity();
            new AlertDialog.Builder(requireContext())
                    .setMessage("Please Confirm!\n" +
                            String.format("\n%-14s%d:%d", "Wake Time:", wakeHour, wakeMinute) +
                            String.format("\n%-15s%d:%d", "Sleep Time:", sleepHour, sleepMinute) +
                            String.format("\n%-15s%s", "Nickname:", nickname) +
                            String.format("\n%-20s%s", "Identity:", identity))
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        addUser();
                        Navigation.findNavController(view).navigate(R.id.action_navigate_from_getIdentity_to_getStarted, getArguments());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    // Unpack Bundle
    private void getBundleArgs() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            eula = bundle.getBoolean(BundleKeys.EULA.getKEY(), true);
            wakeHour = bundle.getInt(BundleKeys.WAKE_HOUR.getKEY());
            wakeMinute = bundle.getInt(BundleKeys.WAKE_MINUTE.getKEY());
            sleepHour = bundle.getInt(BundleKeys.SLEEP_HOUR.getKEY());
            sleepMinute = bundle.getInt(BundleKeys.SLEEP_MINUTE.getKEY());
            nickname = bundle.getString(BundleKeys.NICKNAME.getKEY());
        } else {
            Toast.makeText(requireContext(), "ERROR: NO BUNDLE IS PASSED", Toast.LENGTH_LONG).show();
        }
    }

    private void setIdentity() {
        int selectedId = binding.identitySelection.getCheckedRadioButtonId();
        RadioButton radioButton = (binding.getRoot().findViewById(selectedId));
        identity = radioButton.getText().toString();
    }

    private void addUser() {
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.insert(new User(nickname, identity, 0 , wakeHour, wakeMinute, sleepHour, sleepMinute, eula, false, false));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}