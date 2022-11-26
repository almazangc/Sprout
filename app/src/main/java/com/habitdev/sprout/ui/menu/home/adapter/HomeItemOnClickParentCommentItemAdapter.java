package com.habitdev.sprout.ui.menu.home.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.comment.model.Comment;

import java.util.List;

public class HomeItemOnClickParentCommentItemAdapter extends RecyclerView.Adapter<HomeItemOnClickParentCommentItemAdapter.CommentViewHolder> {

    private List<Comment> comments;

    public HomeItemOnClickParentCommentItemAdapter(List<Comment> comments) {
        this.comments = comments;
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
        holder.bindComment(comments.get(position));
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

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView comment_item;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            comment_item = itemView.findViewById(R.id.comment_item);
        }

        void bindComment(Comment comment) {
            comment_item.setText(comment.getComment());
        }
    }
}
