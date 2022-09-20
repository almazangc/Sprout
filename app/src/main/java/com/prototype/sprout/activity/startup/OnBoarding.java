package com.prototype.sprout.activity.startup;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.prototype.sprout.databinding.OnbordingBinding;

public class OnBoarding extends AppCompatActivity {

    //ViewBinding
    private OnbordingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OnbordingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
