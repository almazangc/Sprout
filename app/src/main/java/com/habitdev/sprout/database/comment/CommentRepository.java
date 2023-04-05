package com.habitdev.sprout.database.comment;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.habitdev.sprout.database.AppDatabase;
import com.habitdev.sprout.database.comment.model.Comment;

import java.util.List;

public class CommentRepository {
    private final CommentDao commentDao;

    public CommentRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(application);
        this.commentDao = appDatabase.commentDao();
    }

    public void insertComment(Comment comment) {
        new CommentRepository.InsertCommentAsyncTask(commentDao).execute(comment);
    }

    public void deleteComment(Comment comment) {
        new CommentRepository.DeleteCommentAsyncTask(commentDao).execute(comment);
    }

    private static class InsertCommentAsyncTask extends AsyncTask<Comment, Void, Void> {

        private final CommentDao commentDao;

        public InsertCommentAsyncTask(CommentDao commentDao) {
            this.commentDao = commentDao;
        }

        @Override
        protected Void doInBackground(Comment... comment) {
            commentDao.insertComment(comment[0]);
            return null;
        }
    }

    private static class DeleteCommentAsyncTask extends AsyncTask<Comment, Void, Void> {

        private final CommentDao commentDao;

        public DeleteCommentAsyncTask(CommentDao commentDao) {
            this.commentDao = commentDao;
        }

        @Override
        protected Void doInBackground(Comment... comment) {
            commentDao.deleteComment(comment[0]);
            return null;
        }
    }

    public LiveData<List<Comment>> getAllHabitCommentByUID(long uid) {
        return commentDao.getAllHabitCommentByUID(uid);
    }
}
