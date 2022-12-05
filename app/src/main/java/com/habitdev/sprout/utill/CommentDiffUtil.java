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
        Comment oldSubroutine = oldCommentList.get(oldItemPosition);
        Comment newSubroutine = newCommentList.get(newItemPosition);
        return oldSubroutine.getComment().equals(newSubroutine.getComment());
    }
}
