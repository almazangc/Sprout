package com.habitdev.sprout.ui.menu.setting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
            R.drawable.ic_spout,
            R.drawable.ic_home,
            R.drawable.ic_subroutine,
            R.drawable.ic_analytics,
            R.drawable.ic_journal,
            R.drawable.ic_setting
    };

    //Contains array list of heaading lbls
    public String[] slideHeader = {
            "SPROUT",
            "HOME",
            "SUBROUTINE",
            "ANALYTIC",
            "JOURNAL",
            "SETTING"
    };

    //Contains array list of heaading lbls
    public String[] slideContent = {
            " The Sprout was chosen as its app name, because just like a sprout plant, the app aims to help users reshape and change their negative habits, similar to how a sprout grows from a seed into a mature plant.\n The app guides, support, and help reforms users' habits, just as a sprout needs proper care and guidance to grow into a healthy plant.\n The app also emphasizes the importance of starting small and breaking down a habit into manageable tasks, similar to how a sprout starts as a small seed before growing into a mature plant.\n Overall, the app and the sprout plant share the common goal of growth and development.",
            " The Home menu display the current habit on reform and information about the habit status.\n Add Button: Which allows user to add a habit whether its predefined or custom.\n More Habit info: The user can tap on the habit added and view more information and also leave a comment about the habit.",
            " The Subroutine menu display list subroutine of habits on reform, which can be marked as done daily after completing the subroutine for the day.\n The system will record the collected data locally as progress.\n Modify subroutine Button: will be displayed if the habit added by user is custom, it gives freedom to update thier subroutines.",
            " The Analytic menu displays a detailed infromations of habits and its subroutines. Use a pie chart for graphing completed, skipped, and total subroutines",
            " The Journal menu allows user to record and read thier own journal notes, and can also be used for self-reflection.",
            " The Setting menu user to modify thier profile, nickname, daily notification, identity(fixed not allowed to change), how long since the app was installed, theme change, about the app, about the people involve, the technology used in development, and a terminal(which is not yet fixed)."
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
