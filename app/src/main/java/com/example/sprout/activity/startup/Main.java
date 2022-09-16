package com.example.sprout.activity.startup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviderGetKt;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.sprout.database.Assestment.Assessment;
import com.example.sprout.database.Assestment.AssessmentViewModel;
import com.example.sprout.database.User.User;
import com.example.sprout.database.User.UserViewModel;
import com.example.sprout.databinding.ActivityMainBinding;

import java.util.List;

public class Main extends AppCompatActivity {

    //ViewBinding
    private ActivityMainBinding binding;

    //User Live Data
    private UserViewModel userViewModel;
    private AssessmentViewModel assessmentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);

        userViewModel.getAllUserListLiveData().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                // Update Recycler View
                Toast.makeText(Main.this, "Update Recycler View: Onchange", Toast.LENGTH_LONG).show();
                Log.d("TAG", "onChanged: ");
            }
        });

        assessmentViewModel.getAllAssessmentListLiveData().observe(this, new Observer<List<Assessment>>() {
            @Override
            public void onChanged(List<Assessment> assessments) {
                // Update Recycler View
                Toast.makeText(Main.this, "Update Recycler View: Onchange", Toast.LENGTH_LONG).show();
                Log.d("TAG", "onChanged: ");
            }
        });
    }
}
