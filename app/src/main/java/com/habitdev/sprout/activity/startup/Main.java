package com.habitdev.sprout.activity.startup;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.habitdev.sprout.databinding.ActivityMainBinding;

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
 * @url
 */
public class Main extends AppCompatActivity {

    /**
     * Main Activity View Binding
     */
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
