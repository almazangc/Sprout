package com.example.sprout.fragment.onBoarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.sprout.R;
import com.example.sprout.database.User.User;
import com.example.sprout.database.User.UserViewModel;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Unpack Bundle
    private void getBundleArgs() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            eula = bundle.getBoolean(bundleKey.getKEY_EULA(), true);
            wakeHour = bundle.getInt(bundleKey.getKEY_WAKEHOUR());
            wakeMinute = bundle.getInt(bundleKey.getKEY_WAKEMINUTE());
            sleepHour = bundle.getInt(bundleKey.getKEY_SLEEPHOUR());
            sleepMinute = bundle.getInt(bundleKey.getKEY_SLEEPMINUTE());
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
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.insert(new User(nickname, identity, wakeHour, wakeMinute, sleepHour, sleepMinute, eula));
    }
}