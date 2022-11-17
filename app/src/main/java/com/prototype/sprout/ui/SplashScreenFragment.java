package com.prototype.sprout.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.prototype.sprout.R;
import com.prototype.sprout.database.user.UserViewModel;
import com.prototype.sprout.databinding.FragmentSplashScreenBinding;
import com.prototype.sprout.model.BundleKey;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("CustomSplashScreen")
public class SplashScreenFragment extends Fragment {

    /**
     * Startup Fragment View Binding
     */
    private FragmentSplashScreenBinding binding;
    private int splashDuration;

    /**
     * Auto Generated Empty Public Constructor
     */
    public SplashScreenFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false);
        splashDuration = 6000; //6 sec
        checkStatus();
        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] someFun = {
                "“Your beliefs become your thoughts,\n" +
                        "Your thoughts become your words,\n" +
                        "Your words become your actions,\n" +
                        "Your actions become your habits,\n" +
                        "Your habits become your values,\n" +
                        "Your values become your destiny.”\n" +
                        "― Gandhi",
                "“The truth is that everyone is bored, and devotes himself to cultivating habits.”\n" +
                        "― Albert Camus",
                "“The moment you become aware of the ego in you, it is strictly speaking no longer the ego, but just an old, conditioned mind-pattern. Ego implies unawareness. Awareness and ego cannot coexist.”\n" +
                        "― Eckhart Tolle",
                "“A man who can't bear to share his habits is a man who needs to quit them.”\n" +
                        "― Stephen King",
                "“Change might not be fast and it isn't always easy. But with time and effort, almost any habit can be reshaped.”\n" +
                        "― Charles Duhigg",
                "“The chains of habit are too weak to be felt until they are too strong to be broken.”\n" +
                        "― Samuel Johnson",
                "“We are what we repeatedly do. Excellence, then, is not an act, but a habit.”\n" +
                        "― Will Durant",
                "“We become what we repeatedly do.”\n" +
                        "― Sean Covey",
                "“Sow a thought, and you reap an act;\n" +
                        "Sow an act, and you reap a habit;\n" +
                        "Sow a habit, and you reap a character;\n" +
                        "Sow a character, and you reap a destiny.”\n" +
                        "― Samuel Smiles",
                "“A morning coffee is my favorite way of starting the day, settling the nerves so that they don't later fray.”\n" +
                        "― Marcia Carrington",
                "“I am dev not dev prolly.” \n -Almazan"
        };

        Random random = new Random();

        int delay = 0; // delay for 5 sec.
        int period = 3000; // repeat every 2 sec.

        new CountDownTimer(splashDuration, period) {

            public void onTick(long millisUntilFinished) {
                requireActivity().runOnUiThread(() -> {
                    int int_random = random.nextInt(someFun.length);
                    binding.subLbl.setText(someFun[int_random]);
                });
            }

            public void onFinish() {
                    // Do on finish timer
            }
        }.start();

//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask()
//        {
//            public void run()
//            {
//                requireActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//            }
//        }, delay, period);

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