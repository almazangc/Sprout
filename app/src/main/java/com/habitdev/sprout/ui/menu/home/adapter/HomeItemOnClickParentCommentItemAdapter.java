package com.habitdev.sprout.ui.menu.home.adapter;

import android.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.achievement.AchievementViewModel;
import com.habitdev.sprout.database.achievement.model.Achievement;
import com.habitdev.sprout.database.comment.CommentViewModel;
import com.habitdev.sprout.database.comment.model.Comment;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.ui.dialog.CompletedAchievementDialogFragment;
import com.habitdev.sprout.ui.menu.home.ui.dialog.HomeParentItemAdapterModifyDialogFragment;
import com.habitdev.sprout.utill.diffutils.CommentDiffUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeItemOnClickParentCommentItemAdapter extends RecyclerView.Adapter<HomeItemOnClickParentCommentItemAdapter.CommentViewHolder> {

    private final CommentViewModel commentViewModel;
    private List<Comment> oldCommentList;

    public HomeItemOnClickParentCommentItemAdapter(CommentViewModel commentViewModel) {
        this.commentViewModel = commentViewModel;
    }

    private static final DiffUtil.ItemCallback<Comment> DIFF_CALLBACK = new DiffUtil.ItemCallback<Comment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
            return oldItem.getPk_comment_uid() == newItem.getPk_comment_uid();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
            return oldItem.getComment().equals(newItem.getComment()) &&
                    oldItem.getDate_commented().equals(newItem.getDate_commented());
        }
    };

    @NonNull
    @Override
    public HomeItemOnClickParentCommentItemAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_parent_habit_on_item_click_comment_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemOnClickParentCommentItemAdapter.CommentViewHolder holder, int position) {
        holder.bindComment(oldCommentList.get(position));

        holder.delete_comment.setOnClickListener(view -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setMessage("Would you like the remove the comment [" + oldCommentList.get(position).getComment() + "]?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        commentViewModel.deleteComment(oldCommentList.get(position));
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (oldCommentList == null ? 0 : oldCommentList.size());
    }

    public void setNewCommentList(List<Comment> newCommentList){
        DiffUtil.Callback DIFF_CALLBACK = new CommentDiffUtil(oldCommentList, newCommentList);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
        if (oldCommentList != null) {
            oldCommentList.clear();
            oldCommentList.addAll(newCommentList);
        } else {
            oldCommentList = new ArrayList<>(newCommentList);
        }

        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(this);
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        final TextView comment_item;
        final Button delete_comment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            comment_item = itemView.findViewById(R.id.comment_item);
            delete_comment = itemView.findViewById(R.id.habit_delete_comment);
        }

        void bindComment(Comment comment) {
            comment_item.setText(comment.getComment());
        }
    }
}
