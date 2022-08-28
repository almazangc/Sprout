package com.example.sprout.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sprout.R;
import com.example.sprout.activity.startup.Initial;

public class Main extends AppCompatActivity {

    Button btn_loadInitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      Initializing Variables
        btn_loadInitial = (Button) findViewById(R.id.btn_main);

        btn_loadInitial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Switching layouts
                Intent intent = new Intent(getApplicationContext(), Initial.class);
                startActivity(intent);
            }
        });
    }
}