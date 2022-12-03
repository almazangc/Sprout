package com.habitdev.sprout.enums;

import android.content.Context;
import android.content.res.ColorStateList;

import androidx.core.content.ContextCompat;

import com.habitdev.sprout.R;

public class AppColorStateList {
    private final ColorStateList CS_CLOUD, CS_AMETHYST, CS_SUNFLOWER, CS_NEPHRITIS, CS_BRIGHT_BLUE_SKY, CS_ALZARIN;

    public AppColorStateList(Context context) {
        CS_CLOUD = ContextCompat.getColorStateList(context, R.color.CLOUDS);
        CS_AMETHYST = ContextCompat.getColorStateList(context, R.color.AMETHYST);
        CS_SUNFLOWER = ContextCompat.getColorStateList(context, R.color.SUNFLOWER);
        CS_NEPHRITIS = ContextCompat.getColorStateList(context, R.color.NEPHRITIS);;
        CS_BRIGHT_BLUE_SKY = ContextCompat.getColorStateList(context, R.color.BRIGHT_SKY_BLUE);
        CS_ALZARIN = ContextCompat.getColorStateList(context, R.color.ALIZARIN);
    }

    public ColorStateList getCS_CLOUD() {
        return CS_CLOUD;
    }

    public ColorStateList getCS_AMETHYST() {
        return CS_AMETHYST;
    }

    public ColorStateList getCS_SUNFLOWER() {
        return CS_SUNFLOWER;
    }

    public ColorStateList getCS_NEPHRITIS() {
        return CS_NEPHRITIS;
    }

    public ColorStateList getCS_BRIGHT_BLUE_SKY() {
        return CS_BRIGHT_BLUE_SKY;
    }

    public ColorStateList getCS_ALZARIN() {
        return CS_ALZARIN;
    }
}
