package com.prototype.sprout.model;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.prototype.sprout.activity.startup.Main;

public class OnBackPressHandler implements onBackPressInterface{

    private Context context;
    private FragmentActivity fragmentActivity;
    private LifecycleOwner lifecycleOwner;

    public OnBackPressHandler(FragmentActivity fragmentActivity, LifecycleOwner lifecycleOwner) {
        this.context = context.getApplicationContext();
        this.fragmentActivity = fragmentActivity;
        this.lifecycleOwner = lifecycleOwner;
    }

    @Override
    public void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Toast.makeText(context, "You shall not amend", Toast.LENGTH_SHORT).show();
            }
        };
        fragmentActivity.getOnBackPressedDispatcher().addCallback(lifecycleOwner, callback);
    }
}
