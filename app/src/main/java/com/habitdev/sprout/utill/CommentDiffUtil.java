package com.habitdev.sprout.utill;

import androidx.recyclerview.widget.DiffUtil;

import com.habitdev.sprout.database.comment.model.Comment;
import com.habitdev.sprout.database.habit.model.Subroutines;

import java.util.List;

public class CommentDiffUtil extends DiffUtil.Callback {

    private List<Comment> oldCommentList, newCommentList;

    public CommentDiffUtil(List<Comment> oldCommentList, List<Comment> newCommentList) {
        this.oldCommentList = oldCommentList;
        this.newCommentList = newCommentList;
    }

    @Override
    public int getOldListSize() {
        return oldCommentList.size();
    }

    @Override
    public int getNewListSize() {
        return newCommentList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldCommentList.get(oldItemPosition).getPk_comment_uid() == newCommentList.get(newItemPosition).getPk_comment_uid();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Comment oldComment = oldCommentList.get(oldItemPosition);
        Comment newComment = newCommentList.get(newItemPosition);
        return oldComment.getComment().equals(newComment.getComment()) &&
                oldComment.getDate_commented().equals(newComment.getDate_commented()) &&
                oldComment.getComment_type().equals(newComment.getComment_type());
    }
}
