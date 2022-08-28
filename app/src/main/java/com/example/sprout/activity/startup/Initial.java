package com.example.sprout.activity.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sprout.R;

public class Initial extends AppCompatActivity {

    Button btn_loadEULA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_initial);

        btn_loadEULA = (Button) findViewById(R.id.btn_letsDoIt);

        btn_loadEULA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Switching layouts
                Intent intent = new Intent(getApplicationContext(), EULA.class);
                startActivity(intent);
            }
        });
    }
}
