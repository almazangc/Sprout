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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.qoutes.Qoutes;
import com.habitdev.sprout.database.qoutes.QoutesViewModel;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentSplashScreenBinding;
import com.habitdev.sprout.model.BundleKey;

import java.io.IOException;
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
    private List<Qoutes> qoutesList;

    public SplashScreenFragment() {
        splashDuration = 20000;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        qoutesViewModel = new ViewModelProvider(requireActivity()).get(QoutesViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false);

        if (isOnline()){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("quotes").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot documents) {
                            if (documents != null) changeQuotes(documents.toObjects(Qoutes.class), "onsucces");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                        Log.d("tag", "onFailure Exception: " + e);

                        }
                    });
        } else {
            List<Qoutes> qoutes = new ArrayList<>();
            qoutes.add(new Qoutes("Author1", "Quotes"));
            qoutes.add(new Qoutes("Author2", "Quotes"));
            qoutes.add(new Qoutes("Author3", "Quotes"));
            qoutes.add(new Qoutes("Author4", "Quotes"));
            changeQuotes(qoutes, "onfail");
        }

        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
//        qoutesList = qoutesViewModel.getQoutesList();
        onBackPress();
        checkStatus();
    }

    private void changeQuotes(List<Qoutes> qoutes, String str){
        Log.d("tag", "called from: " + str);

        Random random = new Random();

        new CountDownTimer(splashDuration, 3000) {

            public void onTick(long millisUntilFinished) {
                requireActivity().runOnUiThread(() -> {
                    int ran = random.nextInt(qoutes.size());
                    Qoutes qoute = qoutes.get(ran);
                    String content = qoute.getQuoted() + "---" + qoute.getAuthor();
                    binding.subLbl.setText(content);
                });
            }
            public void onFinish() {
                    // Do on finish timer
            }
        }.start();
    }

    // ICMP
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
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