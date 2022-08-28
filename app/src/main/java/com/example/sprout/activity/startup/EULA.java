package com.example.sprout.activity.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sprout.R;
import com.example.sprout.activity.Main;

public class EULA extends AppCompatActivity {

    Button btn_agree, btn_disagree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_eula);

        btn_agree = (Button) findViewById(R.id.btn_Agree);


        btn_agree.setOnClickListener(view -> {
            Intent intent_agree = new Intent(getApplicationContext(), Introduction.class);
            startActivity(intent_agree);
        });

        btn_disagree = (Button) findViewById(R.id.btn_Disagree);
        btn_disagree.setOnClickListener(view -> {
            Intent intent_disagree = new Intent(getApplicationContext(), Main.class);
            startActivity(intent_disagree);
        });
    }
}