package com.habitdev.sprout.activity.startup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.habitdev.sprout.databinding.ActivityMainBinding;
import com.habitdev.sprout.utill.NetworkMonitoringUtil;
import com.habitdev.sprout.utill.NetworkStateManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        NetworkMonitoringUtil mNetworkMonitoringUtil = new NetworkMonitoringUtil(getApplicationContext());
        // Check the network state before registering for the 'networkCallbackEvents'
        mNetworkMonitoringUtil.checkNetworkState();
        mNetworkMonitoringUtil.registerNetworkCallbackEvents();

        final String SharedPreferences_KEY = "SP_DB";
        sharedPreferences = getSharedPreferences(SharedPreferences_KEY, Main.MODE_PRIVATE);

        //need to fetch quotes once only (need to fetch once every week for updates)
            NetworkStateManager netStateMgr = NetworkStateManager.getInstance();
            netStateMgr.getNetworkConnectivityStatus().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isConnected) {
                    if(isConnected){
                        if(!sharedPreferences.contains("isDB_loaded") || (sharedPreferences.contains("isDB_loaded") && sharedPreferences.getBoolean("isDB_loaded", false))){
                            loadFireStoreQuotesCollection();
                            Log.d("tag", "Main isConnected() called: data is fetch from server");
                        } else {
                            Log.d("tag", "Main isConnected() called: data already available on cache");
                            netStateMgr.getNetworkConnectivityStatus().removeObserver(this);
                        }
                    }
                    Log.d("tag", "onChanged() called: Main " + isConnected);
                }
            });
        setContentView(binding.getRoot());
    }

    void loadFireStoreQuotesCollection(){
        sharedPreferences.edit().putBoolean("isDB_loaded", FirebaseFirestore.getInstance().collection("quotes").get(Source.SERVER).isComplete()).apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
