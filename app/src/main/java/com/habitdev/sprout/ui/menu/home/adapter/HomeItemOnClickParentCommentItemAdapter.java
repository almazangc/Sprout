package com.habitdev.sprout.ui.menu.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.comment.CommentViewModel;
import com.habitdev.sprout.database.comment.model.Comment;

public class HomeItemOnClickParentCommentItemAdapter extends ListAdapter<Comment, HomeItemOnClickParentCommentItemAdapter.CommentViewHolder> {

    private final CommentViewModel commentViewModel;

    public HomeItemOnClickParentCommentItemAdapter(CommentViewModel commentViewModel) {
        super(DIFF_CALLBACK);
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
                    oldItem.getDate_commented().equals(newItem.getDate_commented()) &&
                    oldItem.getComment_type().equals(newItem.getComment_type());
        }
    };

    @NonNull
    @Override
    public HomeItemOnClickParentCommentItemAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeItemOnClickParentCommentItemAdapter.CommentViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_parent_habit_on_item_click_comment_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemOnClickParentCommentItemAdapter.CommentViewHolder holder, int position) {
        holder.bindComment(getComment(position), commentViewModel);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public Comment getComment(int position) {
        return getItem(position);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView comment_item;
        Button delete_comment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            comment_item = itemView.findViewById(R.id.comment_item);
            delete_comment = itemView.findViewById(R.id.habit_delete_comment);
        }

        void bindComment(Comment comment, CommentViewModel commentViewModel) {
            comment_item.setText(comment.getComment());
            delete_comment.setOnClickListener(view -> {
                commentViewModel.deleteComment(comment);
            });
        }
    }
}
