package com.example.sprout.fragment.onboarding;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.sprout.R;
import com.example.sprout.database.AppDatabase;
import com.example.sprout.database.User.User;
import com.example.sprout.databinding.FragmentGetIdentityBinding;
import com.example.sprout.model.BundleKey;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link getIdentityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class getIdentityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // View Binding
    private FragmentGetIdentityBinding binding;

    private boolean eula;
    private int wakeHour;
    private int wakeMinute;
    private int sleepHour;
    private int sleepMinute;
    private String nickname;

    private final BundleKey bundleKey = new BundleKey();

    public getIdentityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment getIdentityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static getIdentityFragment newInstance(String param1, String param2) {
        getIdentityFragment fragment = new getIdentityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetIdentityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        getBundleArgs();

        binding.btnContinue.setOnClickListener(view -> {

            String identity = addListenerOnButton();

            new AlertDialog.Builder(requireContext())
                    .setMessage("Please Confirm!\n" +
                            String.format("\n%-15s%d:%d", "Wake Time: ", wakeHour, wakeMinute) +
                            String.format("\n%-15s%d:%d", "Sleep Time:", sleepHour, sleepMinute) +
                            String.format("\n%-15s%s", "Nickname:", nickname) +
                            String.format("\n%-20s%s", "Identity:", identity))
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        saveUserData(nickname, identity, wakeHour, wakeMinute, sleepHour, sleepMinute, eula);
                        Navigation.findNavController(view).navigate(R.id.action_navigate_from_getIdentity_to_getStarted , getArguments());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Unpack Bundle
    private void getBundleArgs(){
        Bundle bundle = getArguments();
        if (bundle != null) {
            eula = bundle.getBoolean(bundleKey.getKEY_EULA(), true);
            wakeHour = bundle.getInt(bundleKey.getKEY_WAKEHOUR());
            wakeMinute = bundle.getInt(bundleKey.getKEY_WAKEMINUTE());
            sleepHour = bundle.getInt(bundleKey.getKEY_SLEEPHOUR());
            sleepMinute = bundle.getInt(bundleKey.getKEY_SLEEPMINUTE());
            nickname = bundle.getString(bundleKey.getKEY_NICKNAME());
        } else {
            Log.d("TAG", "getBundleArgs: BUNDLE IS NULL");
        }
    }

    // Radio Selection Button
    private String addListenerOnButton() {
        int selectedId = binding.identitySelection.getCheckedRadioButtonId();
        RadioButton radioButton = (binding.getRoot().findViewById(selectedId));
        return radioButton.getText().toString();
    }

    // Save userdata on room
    private void saveUserData(String nickname, String identity, int wakeHour, int wakeMinute, int sleepHour, int sleepMinute, boolean eula){
        AppDatabase appDatabase = AppDatabase.getDbInstance(this.requireContext());
        User user = new User();
        user.nickname = nickname;
        user.identity = identity;
        user.wake_hour = wakeHour;
        user.wake_minute = wakeMinute;
        user.sleep_hour = sleepHour;
        user.sleep_minute = sleepMinute;
        user.agreed = eula;
        appDatabase.userDao().insert(user);
    }
}