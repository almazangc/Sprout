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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.quotes.Quotes;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentSplashScreenBinding;
import com.habitdev.sprout.model.BundleKey;

import java.util.List;
import java.util.Random;

@SuppressLint("CustomSplashScreen")
public class SplashScreenFragment extends Fragment {

    /**
     * Startup Fragment View Binding
     */
    private FragmentSplashScreenBinding binding;
    private int splashDuration;

    public SplashScreenFragment() {
        this.splashDuration = 20000;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false);
        onBackPress();

        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        firestoreDB.collection("quotes")
                .get(Source.CACHE)
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        if (documents != null) {
                            changeQuotes(documents.toObjects(Quotes.class));
                        } else {
                            Log.d("tag", "SplashScreen: onSuccess() called: null documents " );
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("tag", "SplashScreen: onFailure() called: Data is not available on CACHE");
                    }
                });

//        final Observer<Boolean> activeNetworkStateObserver = new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean isConnected) {
//                if (isConnected) {
//
//                }
//                Log.d("tag", "onChanged() called: SplashScreen " + isConnected);
//            }
//        };
//
//        NetworkStateManager.getInstance().getNetworkConnectivityStatus().observe(requireActivity(), activeNetworkStateObserver);

        checkStatus();
        return binding.getRoot();
    }

    private void changeQuotes(List<Quotes> qoutes) {

        if (!qoutes.isEmpty()){

            Random random = new Random();

            new CountDownTimer(splashDuration, 3000) {
                public void onTick(long millisUntilFinished) {
                    requireActivity().runOnUiThread(() -> {
                        int ran = random.nextInt(qoutes.size());
                        Quotes qoute = qoutes.get(ran);
                        String content = qoute.getQuoted() + "---" + qoute.getAuthor();
                        binding.subLbl.setText(content);
                    });
                }

                public void onFinish() {
                    // Do on finish timer
                }
            }.start();
        } else {
            binding.subLbl.setText("Yokoso");
        }
    }

    /**
     * Check Status of User (Handles Display what part to display)
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
                //Prevent Onback Press
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