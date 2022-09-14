package com.example.sprout.activity.startup.get;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprout.activity.startup.Greetingsv2;
import com.example.sprout.databinding.ActivityStartupGetCommonSleepTimeBinding;
import com.example.sprout.model.BundleKey;

public class CommonSleepTime extends AppCompatActivity {

    ActivityStartupGetCommonSleepTimeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartupGetCommonSleepTimeBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        setInitialTime();

        binding.btnContinue.setOnClickListener(view -> {
            Bundle bundle = getIntent().getBundleExtra(new BundleKey().getKEY_BUNDLE()); // get the nested bundle, or it'll be getIntent().getExtras().getBundle("bundle")
            bundle.putInt(new BundleKey().getKEY_SLEEPHOUR(), binding.SleepTimePicker.getHour());
            bundle.putInt(new BundleKey().getKEY_SLEEPMINUTE(), binding.SleepTimePicker.getMinute());
            startActivity((new Intent(this, Greetingsv2.class).putExtra(new BundleKey().getKEY_BUNDLE(), bundle)));
        });
    }

    private void setInitialTime() {
        binding.SleepTimePicker.setHour(20);
        binding.SleepTimePicker.setMinute(15);
    }
}