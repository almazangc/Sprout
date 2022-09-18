package com.example.sprout.activity.startup;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprout.database.Assestment.Assessment;
import com.example.sprout.database.Assestment.AssessmentViewModel;
import com.example.sprout.database.User.User;
import com.example.sprout.database.User.UserViewModel;
import com.example.sprout.databinding.ActivityMainBinding;

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
