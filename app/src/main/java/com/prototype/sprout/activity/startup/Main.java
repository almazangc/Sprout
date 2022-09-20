package com.prototype.sprout.activity.startup;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.prototype.sprout.database.User.User;
import com.prototype.sprout.database.User.UserViewModel;
import com.prototype.sprout.databinding.ActivityMainBinding;
import com.prototype.sprout.model.OnBackPressHandler;

import java.util.List;

public class Main extends AppCompatActivity {

    //ViewBinding
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
