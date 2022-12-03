package com.habitdev.sprout.ui.menu.subroutine.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        CardView ItemCardView;
        TextView Title, Description;
        Button Modify, Delete;
        ColorStateList cs_cloud, cs_amethyst, cs_sunflower, cs_nephritis, cs_bright_sky_blue, cs_alzarin;

        public SubroutineModifyViewHolder(@NonNull View itemView) {
            super(itemView);

            ItemCardView = itemView.findViewById(R.id.modify_subroutine_parent_item_card_view);
            Title = itemView.findViewById(R.id.modify_subroutine_parent_item_card_view_title);
            Description = itemView.findViewById(R.id.modify_subroutine_parent_item_description);
            Modify = itemView.findViewById(R.id.modify_subroutine_parent_item_modfiy_btn);
            Delete = itemView.findViewById(R.id.modify_subroutine_parent_item_delete_btn);

            cs_cloud = ContextCompat.getColorStateList(itemView.getContext(), R.color.CLOUDS);
            cs_amethyst = ContextCompat.getColorStateList(itemView.getContext(), R.color.AMETHYST);
            cs_sunflower = ContextCompat.getColorStateList(itemView.getContext(), R.color.SUNFLOWER);
            cs_nephritis = ContextCompat.getColorStateList(itemView.getContext(), R.color.NEPHRITIS);
            cs_bright_sky_blue = ContextCompat.getColorStateList(itemView.getContext(), R.color.BRIGHT_SKY_BLUE);
            cs_alzarin = ContextCompat.getColorStateList(itemView.getContext(), R.color.ALIZARIN);
        }

        void bindSubroutine(Subroutines subroutine, OnClickListener mOnClickListener) {

            if (subroutine.getColor().equals(AppColor.ALZARIN.getColor())) {
                ItemCardView.setBackgroundTintList(cs_alzarin);
            } else if (subroutine.getColor().equals(AppColor.AMETHYST.getColor())) {
                ItemCardView.setBackgroundTintList(cs_amethyst);
            } else if (subroutine.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
                ItemCardView.setBackgroundTintList(cs_bright_sky_blue);
            } else if (subroutine.getColor().equals(AppColor.NEPHRITIS.getColor())) {
                ItemCardView.setBackgroundTintList(cs_nephritis);
            } else if (subroutine.getColor().equals(AppColor.SUNFLOWER.getColor())) {
                ItemCardView.setBackgroundTintList(cs_sunflower);
            } else {
                ItemCardView.setBackgroundTintList(cs_cloud);
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
