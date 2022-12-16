package com.habitdev.sprout.activity.startup;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.ActivityMainBinding;
import com.habitdev.sprout.ui.menu.home.enums.ConfigurationKeys;
import com.habitdev.sprout.utill.DateTimeElapsedUtil;
import com.habitdev.sprout.utill.NetworkMonitoringUtil;
import com.habitdev.sprout.utill.NetworkStateManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Sprout:  HABIT BUSTER REFORM WITH BITE-SIZE SUBROUTINES
 * <p>
 *     The study was conducted to design and develop an android mobile-based habit bite-size routine recommender to assist in monitoring, breaking, or reforming bad habits.
 * </p>
 * <p>
 *     The proposed system is expected to solve and experiment with a fresh   concept by integrating bite-size subroutines as support for breaking and habilitating bad habits.
 * </p>
 * <p>
 *     The researchers used the general Agile software process model to develop the proposed Sprout.
 * </p>
 * <p>
 *  The acceptability of the developed system will be determined using the ISO 25010 software quality standard tool along with:
 *  a. functional suitability
 *  b. performance efficiency
 *  c. compatibility
 *  d. usability
 *  e. reliability
 *  f. security
 *  g. portability.
 * </p>
 * @author Almazan, Gilbert C.
 * @version 1.0
 * @since 07-27-2022
 */
public class Main extends AppCompatActivity {

    /**
     * Main Activity View Binding
     */
    private ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;
    private NetworkStateManager networkStateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        if (savedInstanceState == null) {

            final String SharedPreferences_KEY = "SP_DB";
            sharedPreferences = getSharedPreferences(SharedPreferences_KEY, Main.MODE_PRIVATE);

            NetworkMonitoringUtil mNetworkMonitoringUtil = new NetworkMonitoringUtil(getApplicationContext());
            // Check the network state before registering for the 'networkCallbackEvents'
            mNetworkMonitoringUtil.checkNetworkState();
            mNetworkMonitoringUtil.registerNetworkCallbackEvents();

            //need to fetch quotes once only (need to fetch once every week for updates)
            networkStateManager = NetworkStateManager.getInstance();
            networkStateManager.getNetworkConnectivityStatus().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isConnected) {
                    if(isConnected){
                        if(!sharedPreferences.contains("isDB_loaded") || (sharedPreferences.contains("isDB_loaded") && sharedPreferences.getBoolean("isDB_loaded", false))){
                            sharedPreferences.edit().putBoolean("isDB_loaded", FirebaseFirestore.getInstance().collection("quotes").get(Source.SERVER).isComplete()).apply();
                            Log.d("tag", "Main isConnected() called: data is being fetch from server");
                        } else {
                            Log.d("tag", "Main isConnected() called: data already available on cache");
                            networkStateManager.getNetworkConnectivityStatus().removeObserver(this);
                            Log.d("tag", "Main isConnected() called: removed observer");
                        }
                    } else {
                        Log.d("tag", "onChanged() called: Main no network connection");
                    }
                }
            });
            setDailyDateTracker();
        }
        setContentView(binding.getRoot());
    }


    private void setDailyDateTracker(){
        final String DATE_KEY = "DATE";

        if (sharedPreferences.contains(DATE_KEY)){

            String date = sharedPreferences.getString(DATE_KEY, "");
            DateTimeElapsedUtil dateTimeElapsedUtil = new DateTimeElapsedUtil(date, 1);
            dateTimeElapsedUtil.calculateElapsedDateTime();

            long num = dateTimeElapsedUtil.getElapsed_day();

            if (dateTimeElapsedUtil.getElapsed_day() >= 1){
                date = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
                sharedPreferences.edit().putString(DATE_KEY, date).apply();

                HabitWithSubroutinesViewModel habitWithSubroutinesViewModel = new ViewModelProvider(this).get(HabitWithSubroutinesViewModel.class);
                List<Habits> habitsList = habitWithSubroutinesViewModel.getAllHabitOnReform();

                for (Habits habit : habitsList){

                    List<Subroutines> subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid());

                    for (Subroutines subroutine : subroutinesList){

                        if (subroutine.isMarkDone()){
                            subroutine.setMarkDone(false);
                            subroutine.setMax_streak(subroutine.getMax_streak()+1);
                        } else {
                            subroutine.setMax_streak(0);
                            subroutine.setTotal_skips(subroutine.getTotal_skips()+1);
                        }

                        if (subroutine.getLongest_streak() < subroutine.getMax_streak()){
                            subroutine.setLongest_streak(subroutine.getMax_streak());
                        }

                        habitWithSubroutinesViewModel.updateSubroutine(subroutine);
                    }
                }
                Toast.makeText(this, dateTimeElapsedUtil.getElapsed_day() + " Day's, resets daily subroutines", Toast.LENGTH_SHORT).show();
            }
        } else {
            String date = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
            sharedPreferences.edit().putString(DATE_KEY, date).apply();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkStateManager = null;
        binding = null;

        SharedPreferences sharedPreferences = getSharedPreferences(ConfigurationKeys.HOME_SHAREDPREF.getValue(), Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        sharedPreferences = getSharedPreferences(ConfigurationKeys.HOME_ADD_DEFAULT_SHAREDPREF.getValue(), MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

}
