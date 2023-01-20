package com.habitdev.sprout.ui.onBoarding;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.habitdev.sprout.R;
import com.habitdev.sprout.databinding.FragmentInitialBinding;

public class InitialFragment extends Fragment {

    //View Binding
    private FragmentInitialBinding binding;

    public InitialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInitialBinding.inflate(inflater, container, false);
        openVideo(); //ehe
        return binding.getRoot();
    }

    public void openVideo() {
        binding.agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String videoId = "dQw4w9WgXcQ";
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("youtube:" + videoId));
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.btnLetsDoIt.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_navigate_from_initial_to_eula));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}