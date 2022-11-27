package com.habitdev.sprout.ui.menu.home.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.comment.CommentViewModel;
import com.habitdev.sprout.database.comment.model.Comment;

import java.util.List;

public class HomeItemOnClickParentCommentItemAdapter extends RecyclerView.Adapter<HomeItemOnClickParentCommentItemAdapter.CommentViewHolder> {

    private List<Comment> comments;
    private final CommentViewModel commentViewModel;

    public HomeItemOnClickParentCommentItemAdapter(List<Comment> comments, CommentViewModel commentViewModel) {
        this.comments = comments;
        this.commentViewModel = commentViewModel;
    }

    @NonNull
    @Override
    public HomeItemOnClickParentCommentItemAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeItemOnClickParentCommentItemAdapter.CommentViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_parent_habit_on_item_click_comment_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemOnClickParentCommentItemAdapter.CommentViewHolder holder, int position) {
        holder.bindComment(comments.get(position), commentViewModel);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView comment_item;
        Button delete_comment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            comment_item = itemView.findViewById(R.id.comment_item);
            delete_comment = itemView.findViewById(R.id.habit_delete_comment);
        }

        @SuppressLint("NotifyDataSetChanged")
        void bindComment(Comment comment, CommentViewModel commentViewModel) {
            comment_item.setText(comment.getComment());
            delete_comment.setOnClickListener(view -> {
                comments.removeIf(listComment -> comment == listComment);
                commentViewModel.deleteComment(comment);
                notifyDataSetChanged();
            });
        }
    }
}
