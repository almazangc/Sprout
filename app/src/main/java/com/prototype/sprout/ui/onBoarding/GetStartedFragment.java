package com.prototype.sprout.ui.onBoarding;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.prototype.sprout.R;
import com.prototype.sprout.databinding.FragmentGetStartedBinding;
import com.prototype.sprout.model.BundleKey;

public class GetStartedFragment extends Fragment {

    // View Binding
    private FragmentGetStartedBinding binding;

    public GetStartedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetStartedBinding.inflate(inflater, container, false);
        binding.lblName.setText(getArguments().getString(new BundleKey().getKEY_NICKNAME()));
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.btnContinue.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_getStarted_to_personalization);
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
//                Toast.makeText(requireContext(), "DATA IS WRITTEN", Toast.LENGTH_SHORT).show();
                showToast("DATA IS ALREADY SAVED");
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showToast(String string) {
        CharSequence message = string;
        int duration = Toast.LENGTH_LONG;

//        View layout = binding.getRoot().findViewById(R.id.custom_toast_layout);
        View layout = getLayoutInflater().inflate(R.layout.get_started_custom_toast, (ViewGroup) binding.getRoot().findViewById(R.id.custom_toast_layout));
        TextView text = layout.findViewById(R.id.custom_toast_lbl);
        text.setText(message);

        Toast toast = new Toast(requireContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layout);
        toast.setDuration(duration);
        toast.show();
    }
}