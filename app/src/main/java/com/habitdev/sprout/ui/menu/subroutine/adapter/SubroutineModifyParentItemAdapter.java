package com.habitdev.sprout.ui.menu.subroutine.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apachat.swipereveallayout.core.SwipeLayout;
import com.apachat.swipereveallayout.core.ViewBinder;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.utill.SubroutineDiffUtil;

import java.util.List;

public class SubroutineModifyParentItemAdapter extends RecyclerView.Adapter<SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder> {

    private List<Subroutines> oldSubroutineList;
    private SubroutineModifyParentOnclickListener mSubroutineModifyParentOnclickListener;

    private final ViewBinder viewBinder = new ViewBinder();

    public SubroutineModifyParentItemAdapter(List<Subroutines> oldSubroutineList) {
        this.oldSubroutineList = oldSubroutineList;
    }

    public void setmSubroutineModifyOnclickListener(SubroutineModifyParentOnclickListener mSubroutineModifyParentOnclickListener) {
        this.mSubroutineModifyParentOnclickListener = mSubroutineModifyParentOnclickListener;
    }

    @NonNull
    @Override
    public SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubroutineModifyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_subroutine_modify_parent_item, parent, false), mSubroutineModifyParentOnclickListener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SubroutineModifyParentItemAdapter.SubroutineModifyViewHolder holder, int position) {
        viewBinder.setOpenOnlyOne(true);
        viewBinder.bind(holder.swipeLayout, String.valueOf(oldSubroutineList.get(position).getPk_subroutine_uid()));
        holder.bindSubroutine(oldSubroutineList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return oldSubroutineList.size();
    }

    public void setNewSubroutineList(List<Subroutines> newSubroutineList) {
        DiffUtil.Callback DIFF_CALLBACK = new SubroutineDiffUtil(oldSubroutineList, newSubroutineList);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
        oldSubroutineList = newSubroutineList;
        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(this);
    }

    public static class SubroutineModifyViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        RelativeLayout itemLayout, deleteLayout;
        TextView Title, Description;
        Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin;

        public SubroutineModifyViewHolder(@NonNull View itemView, SubroutineModifyParentOnclickListener mSubroutineModifyParentOnclickListener) {
            super(itemView);

            itemLayout = itemView.findViewById(R.id.modify_subroutine_parent_item_layout_subroutine);
            swipeLayout = itemView.findViewById(R.id.modify_subroutine_parent_item_layout);
            deleteLayout = itemView.findViewById(R.id.modify_subroutine_parent_item_layout_control);
            Title = itemView.findViewById(R.id.modify_subroutine_parent_item_card_view_title);
            Description = itemView.findViewById(R.id.modify_subroutine_parent_item_description);

            cloud = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_cloud_selector);
            amethyst = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_amethyst_selector);
            sunflower = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_sunflower_selector);
            nephritis = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_nephritis_selector);
            bright_sky_blue = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_brightsky_blue_selector);
            alzarin = ContextCompat.getDrawable(itemView.getContext(), R.drawable.background_btn_alzarin_selector);

            itemLayout.setOnClickListener(view -> mSubroutineModifyParentOnclickListener.onItemClick(getBindingAdapterPosition()));
            deleteLayout.setOnClickListener(view -> mSubroutineModifyParentOnclickListener.onItemDelete(getBindingAdapterPosition()));
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

//            itemLayout.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                        itemLayout.setPadding(padding_inPx(10), padding_inPx(10), padding_inPx(5), padding_inPx(0));
//                    } else {
//                        itemLayout.setPadding(padding_inPx(5), padding_inPx(5), padding_inPx(5), padding_inPx(5));
//                    }
//                    return false;
//                }
//            });
//
//            itemLayout.setOnDragListener(new View.OnDragListener() {
//                @Override
//                public boolean onDrag(View view, DragEvent dragEvent) {
//                    Log.d("tag", "onDrag: " + dragEvent.getAction());
//                    return false;
//                }
//            });

            Title.setText(subroutine.getSubroutine());
            Description.setText(subroutine.getDescription());


        }

        int padding_inPx(int dp) {
            final float scale = itemLayout.getResources().getDisplayMetrics().density;
            return (int) (dp * scale + 0.5f);
        }
    }
}
