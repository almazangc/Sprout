package com.prototype.sprout.ui.onBoarding;

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

import com.prototype.sprout.R;
import com.prototype.sprout.database.user.User;
import com.prototype.sprout.database.user.UserViewModel;
import com.prototype.sprout.databinding.FragmentGetIdentityBinding;
import com.prototype.sprout.model.BundleKey;

public class GetIdentityFragment extends Fragment {

    private final BundleKey bundleKey = new BundleKey();
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
            eula = bundle.getBoolean(bundleKey.getKEY_EULA(), true);
            wakeHour = bundle.getInt(bundleKey.getKEY_WAKE_HOUR());
            wakeMinute = bundle.getInt(bundleKey.getKEY_WAKE_MINUTE());
            sleepHour = bundle.getInt(bundleKey.getKEY_SLEEP_HOUR());
            sleepMinute = bundle.getInt(bundleKey.getKEY_SLEEP_MINUTE());
            nickname = bundle.getString(bundleKey.getKEY_NICKNAME());
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