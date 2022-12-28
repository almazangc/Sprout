package com.habitdev.sprout.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.habitdev.sprout.R;
import com.habitdev.sprout.activity.startup.Main;
import com.habitdev.sprout.database.quotes.model.Quotes;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentSplashScreenBinding;
import com.habitdev.sprout.enums.BundleKeys;

import java.util.List;
import java.util.Random;

@SuppressLint("CustomSplashScreen")
public class SplashScreenFragment extends Fragment {

    private static final String TAG = "tag";
    /**
     * Startup Fragment View Binding
     */
    private FragmentSplashScreenBinding binding;
    private final int splashDuration;
    private FragmentActivity fragment;

    public SplashScreenFragment() {
        this.splashDuration = 3000;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragment = requireActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false);
        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        setTheme();
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        firestoreDB.collection("quotes")
                .get(Source.CACHE)
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        if (documents != null) {
                            changeQuotes(documents.toObjects(Quotes.class));
                        } else {
                            Log.d("tag", "SplashScreen: onSuccess() called: null documents ");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("tag", "SplashScreen: onFailure() called: Data is not available on CACHE");
                    }
                });
        checkStatus();
    }

    private void setTheme() {
        final String SharedPreferences_KEY = "SP_DB";
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SharedPreferences_KEY, Main.MODE_PRIVATE);
        Drawable light, dark;
        int _light, _dark;

        light = ContextCompat.getDrawable(requireContext(), R.color.LIGHT);
        dark = ContextCompat.getDrawable(requireContext(), R.color.DARK);

        _light = ContextCompat.getColor(requireContext(), R.color.LIGHT);
        _dark = ContextCompat.getColor(requireContext(), R.color.DARK);

        final String SHARED_PREF_KEY = "THEME";
        int theme = sharedPreferences.getInt(SHARED_PREF_KEY, -1);
        if (theme == 1) {
            binding.splashScreenBackground.setBackground(light);
            binding.appName.setTextColor(_dark);
            binding.qouteLbl.setTextColor(_dark);
        } else if (theme == 2) {
            binding.splashScreenBackground.setBackground(dark);
            binding.appName.setTextColor(_light);
            binding.qouteLbl.setTextColor(_light);
        }
    }

    private void changeQuotes(List<Quotes> quotes) {

        if (!quotes.isEmpty()) {

            Random random = new Random();

            new CountDownTimer(splashDuration, 1500) {
                public void onTick(long millisUntilFinished) {
                    fragment.runOnUiThread(() -> {
                        int ran = random.nextInt(quotes.size());
                        Quotes quote = quotes.get(ran);
                        String content = quote.getQuoted() + " - " + quote.getAuthor();
                        if (binding != null) binding.qouteLbl.setText(content);
                    });
                }
                public void onFinish() {
                    this.cancel();
                }
            }.start();
        } else {
            final String MESSAGE = "First Time Message for new installation";
            binding.qouteLbl.setText(MESSAGE);
        }
    }

    /**
     * Check Status of User (Handles Display what part to display)
     */
    private void checkStatus() {
        if (binding != null) {
            new Handler().postDelayed(() -> {
                //Loading intents of fragments
                boolean isOnBoardingDone = false;

                Bundle bundle = SplashScreenFragment.this.getArguments();
                if (bundle != null) {
                    isOnBoardingDone = bundle.getBoolean(BundleKeys.ANALYSIS.getKEY());
                } else {
                    try {
                        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
                        isOnBoardingDone = userViewModel.getOnBoarding();
                    } catch (Exception e) {
//                        Log.d("tag", "checkStatus: " + e.getMessage());
//                    Fragment SplashScreenFragment not attached to an activity.
                    }
                }

                if (!isOnBoardingDone) {
                    try {
                        NavHostFragment.findNavController(SplashScreenFragment.this).navigate(R.id.action_splashscreen_to_onboarding);
                    } catch (Exception e) {
//                        Log.d("tag", "checkStatus: " + e.getMessage());
//                    Fragment SplashScreenFragment not associated with a fragment manager
                    }
                } else {
                    try {
                        NavHostFragment.findNavController(SplashScreenFragment.this).navigate(R.id.action_splashscreen_to_main);
                    } catch (Exception e) {
//                        Log.d("tag", "checkStatus: " + e.getMessage());
//                    Fragment SplashScreenFragment not associated with a fragment manager
                    }
                }

                onDestroyView();
            }, splashDuration);
        }
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //cannot back press
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