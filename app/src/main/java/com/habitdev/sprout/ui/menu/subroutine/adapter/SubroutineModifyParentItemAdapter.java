package com.habitdev.sprout.ui.menu.subroutine.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.enums.AppColor;

public class SubroutineModifyParentItemAdapter extends ListAdapter<Subroutines, SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder> {

    public SubroutineModifyParentItemAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Subroutines> DIFF_CALLBACK = new DiffUtil.ItemCallback<Subroutines>() {
        @Override
        public boolean areItemsTheSame(@NonNull Subroutines oldItem, @NonNull Subroutines newItem) {
            return oldItem.getPk_subroutine_uid() == newItem.getPk_subroutine_uid();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Subroutines oldItem, @NonNull Subroutines newItem) {
            return oldItem.getSubroutine().equals(newItem.getSubroutine()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getColor().equals(newItem.getColor());
        }
    };

    private SubroutineModifyParentOnclickListener mSubroutineModifyParentOnclickListener;

    public void setmSubroutineModifyOnclickListener(SubroutineModifyParentOnclickListener mSubroutineModifyParentOnclickListener) {
        this.mSubroutineModifyParentOnclickListener = mSubroutineModifyParentOnclickListener;
    }

    @NonNull
    @Override
    public SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_subroutine_modify_parent_item, parent, false), mSubroutineModifyParentOnclickListener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder holder, int position) {
        holder.bindSubroutine(getSubroutine(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public Subroutines getSubroutine(int position) {
        return getItem(position);
    }

    public static class SubroutineModifyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemLayout, subroutineLayout, deleteLayout;
        TextView Title, Description;
        Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin;

        public SubroutineModifyViewHolder(@NonNull View itemView, SubroutineModifyParentOnclickListener mSubroutineModifyParentOnclickListener) {
            super(itemView);

            itemLayout = itemView.findViewById(R.id.modify_subroutine_parent_item_layout_subroutine);
            subroutineLayout = itemLayout.findViewById(R.id.modify_subroutine_parent_item_layout_subroutine);
            deleteLayout = itemLayout.findViewById(R.id.modify_subroutine_parent_item_layout_control);
            Title = itemView.findViewById(R.id.modify_subroutine_parent_item_card_view_title);
            Description = itemView.findViewById(R.id.modify_subroutine_parent_item_description);

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_cloud_selector);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_amethyst_selector);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_sunflower_selector);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_nephritis_selector);
            bright_sky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_brigthsky_blue_selector);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_alzarin_selector);

            itemLayout.setOnClickListener(view -> {
                    mSubroutineModifyParentOnclickListener.onItemClick(getItemViewType());
            });
        }

        @SuppressLint("ClickableViewAccessibility")
        void bindSubroutine(Subroutines subroutine) {

            if (subroutine.getColor().equals(AppColor.ALZARIN.getColor())) {
                itemLayout.setBackground(alzarin);
            } else if (subroutine.getColor().equals(AppColor.AMETHYST.getColor())) {
                itemLayout.setBackground(amethyst);
            } else if (subroutine.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
                itemLayout.setBackground(bright_sky_blue);
            } else if (subroutine.getColor().equals(AppColor.NEPHRITIS.getColor())) {
                itemLayout.setBackground(nephritis);
            } else if (subroutine.getColor().equals(AppColor.SUNFLOWER.getColor())) {
                itemLayout.setBackground(sunflower);
            } else {
                itemLayout.setBackground(cloud);
            }

            //changing component attributes with motion events
            itemLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                        subroutineLayout.setPadding(padding_inPx(10), padding_inPx(10), padding_inPx(5), padding_inPx(0));
                    } else {
                        subroutineLayout.setPadding(padding_inPx(5), padding_inPx(5), padding_inPx(5), padding_inPx(5));
                    }
                    return false;
                }
            });

            Title.setText(subroutine.getSubroutine());
            Description.setText(subroutine.getDescription());
        }

        int padding_inPx (int dp){
            int padding_in_dp = dp;
            final float scale = itemLayout.getResources().getDisplayMetrics().density;
            return (int) (padding_in_dp * scale + 0.5f);
        }
    }
}
