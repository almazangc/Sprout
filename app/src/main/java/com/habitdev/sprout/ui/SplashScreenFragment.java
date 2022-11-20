package com.habitdev.sprout.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.qoutes.Qoutes;
import com.habitdev.sprout.database.qoutes.QoutesViewModel;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentSplashScreenBinding;
import com.habitdev.sprout.model.BundleKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressLint("CustomSplashScreen")
public class SplashScreenFragment extends Fragment {

    /**
     * Startup Fragment View Binding
     */
    private FragmentSplashScreenBinding binding;
    private int splashDuration;
    private QoutesViewModel qoutesViewModel;

    public SplashScreenFragment() {
        splashDuration = 20000;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false);
        qoutesViewModel = new ViewModelProvider(requireActivity()).get(QoutesViewModel.class);
        List<Qoutes> qoutesList;
        qoutesList = qoutesViewModel.getQoutesList();
        Log.d("tag", "Fragment: QuoteList: " + qoutesList.toString());

//        Random random = new Random();
//
//        int delay = 0; // delay for 5 sec.
//        int period = 3000; // repeat every 2 sec.

//        new CountDownTimer(splashDuration, period) {
//
//            public void onTick(long millisUntilFinished) {
//                requireActivity().runOnUiThread(() -> {
//                    int ran = random.nextInt(qoutesList.size());
//                    Qoutes qoutes = qoutesList.get(ran);
//                    String content = qoutes.getQuoted() + "---" + qoutes.getAuthor();
//                    binding.subLbl.setText(content);
//                });
//            }
//
//            public void onFinish() {
//                    // Do on finish timer
//            }
//        }.start();

        checkStatus();
        onBackPress();
        return binding.getRoot();
    }

    /**
     * Check Status of User (Handles Display at app_start)
     */
    private void checkStatus() {
        new Handler().postDelayed(() -> {
            //Loading intents of fragments
            boolean isOnBoardingDone;

            Bundle bundle = SplashScreenFragment.this.getArguments();
            if (bundle != null) {
                isOnBoardingDone = bundle.getBoolean(new BundleKey().getKEY_ANALYSIS());
            } else {
                UserViewModel userViewModel = new ViewModelProvider(SplashScreenFragment.this.requireActivity()).get(UserViewModel.class);
                isOnBoardingDone = userViewModel.getOnBoarding();
            }

            if (!isOnBoardingDone)
                NavHostFragment.findNavController(SplashScreenFragment.this).navigate(R.id.action_splashscreen_to_onboarding);
            if (isOnBoardingDone)
                NavHostFragment.findNavController(SplashScreenFragment.this).navigate(R.id.action_splashscreen_to_main);
            onDestroyView();
        }, splashDuration);
    }


    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Do Nothing
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