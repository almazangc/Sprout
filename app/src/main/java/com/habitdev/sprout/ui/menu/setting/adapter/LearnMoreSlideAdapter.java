package com.habitdev.sprout.ui.menu.setting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.habitdev.sprout.R;

public class LearnMoreSlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public LearnMoreSlideAdapter(Context context) {
        this.context = context;
    }

    //Contains array list of drawables
    public int[] slideImgRes = {
            R.drawable.ic_home,
            R.drawable.ic_subroutine,
            R.drawable.ic_analytics,
            R.drawable.ic_journal,
            R.drawable.ic_setting
    };

    //Contains array list of heading labels
    public String[] slideHeader = {
            "HOME",
            "SUBROUTINE",
            "ANALYTIC",
            "JOURNAL",
            "SETTING"
    };

    //Contains array list of heading labels
    public String[] slideContent = {
            " The Home menu display the current habit on reform and information about the habit status.\n Add Button: Which allows user to add a habit whether its predefined or custom.\n More Habit info: The user can tap on the habit added and view more information and also leave a comment about the habit.",
            " The Subroutine menu display list subroutine of habits on reform, which can be marked as done daily after completing the subroutine for the day.\n The system will record the collected data locally as progress.\n Modify subroutine Button: will be displayed if the habit added by user is custom, it gives freedom to update their subroutines.",
            " The Analytic menu displays a detailed information of habits and its subroutines. Use a pie chart for graphing completed, skipped, and total subroutines",
            " The Journal menu enables end-user to record and read their own journal notes, and can also be used for self-reflection.",
            " The Setting menu consist of 6 tabs. Profile Tab, Theme Tab, Stack Info Tab, Learn More Tab, About Us Tab, and lastly Achievement Tab."
    };

    @Override
    public int getCount() {
        return slideHeader.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.adapter_setting_learn_more_viewpager_slide, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.adapter_setting_learn_more_viewpager_slide_logo);
        TextView slideTextViewHeader = (TextView) view.findViewById(R.id.adapter_setting_learn_more_viewpager_slide_logo_header);
        TextView slideTextViewContent = (TextView) view.findViewById(R.id.adapter_setting_learn_more_viewpager_slide_logo_content);

        slideImageView.setImageResource(slideImgRes[position]);
        slideTextViewHeader.setText(slideHeader[position]);
        slideTextViewContent.setText(slideContent[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object); // destroy view to prevent leaking
    }
}
