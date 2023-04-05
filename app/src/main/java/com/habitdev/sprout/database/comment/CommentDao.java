package com.habitdev.sprout.database.comment;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.habitdev.sprout.database.comment.model.Comment;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM Comment where fk_habit_uid=:uid ORDER BY pk_comment_uid DESC")
    LiveData<List<Comment>> getAllHabitCommentByUID(long uid);

    @Insert
    void insertComment(Comment comments);

    @Delete
    void deleteComment(Comment comment);
}
