package com.habitdev.sprout.database.comment;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.habitdev.sprout.database.comment.model.Comment;

import java.util.List;

public class CommentViewModel extends AndroidViewModel {

    private final CommentRepository repository;

    public CommentViewModel(@NonNull Application application) {
        super(application);
        repository = new CommentRepository(application);
    }

    public LiveData<List<Comment>> getAllHabitCommentByUID(long uid) {
        return repository.getAllHabitCommentByUID(uid);
    }

    public void insertComment(Comment comment){
        repository.insertComment(comment);
    }

    public void deleteComment(Comment comment){
        repository.deleteComment(comment);
    }
}
