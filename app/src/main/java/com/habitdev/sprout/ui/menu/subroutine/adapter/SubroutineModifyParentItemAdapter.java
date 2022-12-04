package com.habitdev.sprout.ui.menu.subroutine.adapter;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

    public interface OnClickListener {
        void onClickModify(Subroutines subroutine);
        void onClickDelete(Subroutines subroutine);
    }

    private OnClickListener mOnClickListener;

    public void setmOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_subroutine_modify_parent_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder holder, int position) {
        holder.bindSubroutine(getSubroutine(position), mOnClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public Subroutines getSubroutine(int position) {
        return getItem(position);
    }

    public static class SubroutineModifyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemLayout;
        TextView Title, Description;
        Button Modify, Delete;
        Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin;

        public SubroutineModifyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemLayout = itemView.findViewById(R.id.modify_subroutine_parent_item_layout_subroutine);
            Title = itemView.findViewById(R.id.modify_subroutine_parent_item_card_view_title);
            Description = itemView.findViewById(R.id.modify_subroutine_parent_item_description);
            Modify = itemView.findViewById(R.id.modify_subroutine_parent_item_modfiy_btn);
            Delete = itemView.findViewById(R.id.modify_subroutine_parent_item_delete_btn);

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_child_item_view_cloud);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_child_item_view_amethyst);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_child_item_view_sunflower);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_child_item_view_nephritis);
            bright_sky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_child_item_view_brightsky_blue);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_child_item_view_alzarin);
        }

        void bindSubroutine(Subroutines subroutine, OnClickListener mOnClickListener) {

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

            Title.setText(subroutine.getSubroutine());
            Description.setText(subroutine.getDescription());

            Modify.setOnClickListener(view -> {
                mOnClickListener.onClickModify(subroutine);
            });

            Delete.setOnClickListener(view -> {
                mOnClickListener.onClickDelete(subroutine);
            });
        }
    }
}
