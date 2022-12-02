package com.habitdev.sprout.ui.menu.subroutine.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.enums.AppColor;

import java.util.List;

public class SubroutineChildItemAdapter extends RecyclerView.Adapter<SubroutineChildItemAdapter.ChildItemViewHolder> {

    List<Subroutines> subroutines;

    public SubroutineChildItemAdapter(List<Subroutines> subroutines) {
        this.subroutines = subroutines;
    }

    @NonNull
    @Override
    public SubroutineChildItemAdapter.ChildItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChildItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_subroutine_child_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SubroutineChildItemAdapter.ChildItemViewHolder holder, int position) {
        holder.bindDate(subroutines.get(position));
    }

    @Override
    public int getItemCount() {
        return subroutines.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setHabitWithSubroutines(List<Subroutines> subroutines) {
        this.subroutines = subroutines;
    }

    public static class ChildItemViewHolder extends RecyclerView.ViewHolder {

        CardView ItemCardView;
        TextView Title, Description;
        Button Upvote, Downvote, MarkAsDone;
        ColorStateList cs_cloud, cs_amethyst, cs_sunflower, cs_nephritis, cs_bright_sky_blue, cs_alzarin;

        public ChildItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemCardView = itemView.findViewById(R.id.subroutine_child_item_card_view);
            Title = itemView.findViewById(R.id.subroutine);
            Description = itemView.findViewById(R.id.home_item_on_click_habit_description);

            Upvote = itemView.findViewById(R.id.btn_upvote_subroutine);
            Downvote = itemView.findViewById(R.id.btn_downvote_subroutine);
            MarkAsDone = itemView.findViewById(R.id.mark_as_done);

            cs_cloud = ContextCompat.getColorStateList(itemView.getContext(), R.color.CLOUDS);
            cs_amethyst = ContextCompat.getColorStateList(itemView.getContext(), R.color.AMETHYST);
            cs_sunflower = ContextCompat.getColorStateList(itemView.getContext(), R.color.SUNFLOWER);
            cs_nephritis = ContextCompat.getColorStateList(itemView.getContext(), R.color.NEPHRITIS);
            cs_bright_sky_blue = ContextCompat.getColorStateList(itemView.getContext(), R.color.BRIGHT_SKY_BLUE);
            cs_alzarin = ContextCompat.getColorStateList(itemView.getContext(), R.color.ALIZARIN);
        }

        void bindDate(Subroutines subroutine) {
            if (subroutine.getColor().equals(AppColor.ALZARIN.getColor())){
                ItemCardView.setBackgroundTintList(cs_alzarin);
            } else if (subroutine.getColor().equals(AppColor.AMETHYST.getColor())){
                ItemCardView.setBackgroundTintList(cs_amethyst);
            } else if (subroutine.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())){
                ItemCardView.setBackgroundTintList(cs_bright_sky_blue);
            } else if (subroutine.getColor().equals(AppColor.NEPHRITIS.getColor())){
                ItemCardView.setBackgroundTintList(cs_nephritis);
            } else if (subroutine.getColor().equals(AppColor.SUNFLOWER.getColor())){
                ItemCardView.setBackgroundTintList(cs_sunflower);
            } else {
                ItemCardView.setBackgroundTintList(cs_cloud);
            }

            Title.setText(subroutine.getSubroutine());
            Description.setText(subroutine.getDescription());

            Upvote.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "UpVote", Toast.LENGTH_SHORT).show();
            });

            Downvote.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "DownVote", Toast.LENGTH_SHORT).show();
            });

            MarkAsDone.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "Marked as Done", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
