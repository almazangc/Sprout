package com.habitdev.sprout.utill;

import androidx.recyclerview.widget.DiffUtil;

import com.habitdev.sprout.database.comment.model.Comment;

import java.util.List;

/**
 * CommentDiffUtil is a class that extends DiffUtil.Callback and is used to compare and calculate the difference between two lists of comments.
 * It is used to updateAnswer a RecyclerView's adapter with the new list of comments in an efficient manner by identifying the minimum number of changes
 * required to transform the old list into the new list.
 */
 public class CommentDiffUtil extends DiffUtil.Callback {

    private final List<Comment> oldCommentList;
    private final List<Comment> newCommentList;

    /**
     * Constructor for CommentDiffUtil
     * @param oldCommentList The old list of comments
     * @param newCommentList The new list of comments to compare to the old list
     */
    public CommentDiffUtil(List<Comment> oldCommentList, List<Comment> newCommentList) {
        this.oldCommentList = oldCommentList;
        this.newCommentList = newCommentList;
    }

    /**
     * Returns the size of the old list of comments
     * @return size of oldCommentList
     */
    @Override
    public int getOldListSize() {
        return (oldCommentList == null ? 0 : oldCommentList.size());
    }

    /**
     * Returns the size of the new list of comments
     * @return size of newCommentList
     */
    @Override
    public int getNewListSize() {
        return newCommentList.size();
    }

    /**
     * Compares the primary keys of the comments at the given positions in the old and new lists
     * @param oldItemPosition position of comment in oldCommentList
     * @param newItemPosition position of comment in newCommentList
     * @return true if the primary keys are the same, false otherwise
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldCommentList.get(oldItemPosition).getPk_comment_uid() == newCommentList.get(newItemPosition).getPk_comment_uid();
    }

    /**
     * Compares the comment, date_commented and comment_type attributes of the comments at the given positions in the old and new lists
     * @param oldItemPosition position of comment in oldCommentList
     * @param newItemPosition position of comment in newCommentList
     * @return true if the attributes are the same, false otherwise
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Comment oldComment = oldCommentList.get(oldItemPosition);
        Comment newComment = newCommentList.get(newItemPosition);
        return oldComment.getComment().equals(newComment.getComment()) &&
                oldComment.getDate_commented().equals(newComment.getDate_commented()) &&
                oldComment.getComment_type().equals(newComment.getComment_type());
    }
}
