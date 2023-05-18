package com.habitdev.sprout.ui.onBoarding;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.habitdev.sprout.database.user.model.User;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentGetIdentityBinding;
import com.habitdev.sprout.enums.BundleKeys;
import com.habitdev.sprout.utill.alarm.AlarmScheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GetIdentityFragment extends Fragment {

    private FragmentGetIdentityBinding binding;
    private String nickname;
    private String identity;
    private int wakeHour;
    private int wakeMinute;
    private int sleepHour;
    private int sleepMinute;
    private boolean eula;

    public GetIdentityFragment() {}

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
                            String.format("\n%-14s%d:%s", "Wake Time:", wakeHour, wakeMinute == 0 ? "00": wakeMinute) +
                            String.format("\n%-15s%d:%s", "Sleep Time:", sleepHour, sleepMinute == 0 ? "00" : sleepMinute) +
                            String.format("\n%-15s%s", "Nickname:", nickname) +
                            String.format("\n%-20s%s", "Identity:", identity))
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        setAlarm();
                        addUser();
                        Navigation.findNavController(view).navigate(R.id.action_navigate_from_getIdentity_to_getStarted, getArguments());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void setAlarm() {
        AlarmScheduler alarmScheduler = new AlarmScheduler(requireContext());
        alarmScheduler.scheduleMorningAlarm(alarmScheduler.setCalendar(wakeHour, wakeMinute), "Keep going, You can do it");
        alarmScheduler.scheduleEveningAlarm(alarmScheduler.setCalendar(sleepHour, sleepMinute), "Don't forget to update your progress");
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("DAILY_NOTIFY_SHARED_PREF", Context.MODE_PRIVATE);
        final String isToggledONKEY = "IS_DAILY_TOGGLED_ON";
        sharedPreferences.edit().putBoolean(isToggledONKEY, true).apply();
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
        String  date_installed = new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss a", Locale.getDefault()).format(new Date());
        userViewModel.insert(new User(nickname, identity, 0 , wakeHour, wakeMinute, sleepHour, sleepMinute, eula, false, false, date_installed));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}