package com.example.sprout.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sprout.activity.startup.EULA;
import com.example.sprout.activity.startup.Hello;
import com.example.sprout.R;

public class Main extends AppCompatActivity {

    Button loadInitialStartup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      Initializing Variables
        loadInitialStartup = (Button) findViewById(R.id.btn_main);

        loadInitialStartup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Switching layouts
                Intent intent = new Intent(getApplicationContext(), EULA.class);
                startActivity(intent);
            }
        });
    }
}