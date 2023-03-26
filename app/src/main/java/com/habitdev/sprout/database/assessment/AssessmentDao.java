package com.habitdev.sprout.database.assessment;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.habitdev.sprout.database.assessment.model.Answer;
import com.habitdev.sprout.database.assessment.model.Assessment;
import com.habitdev.sprout.database.assessment.model.Choices;
import com.habitdev.sprout.database.assessment.model.Question;

import java.util.List;

@Dao
public interface AssessmentDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertQuestion(Question question);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChoices(List<Choices> choices);

    @Transaction
    @Query("SELECT * FROM question INNER JOIN choices ON pk_questions_uid = fk_question_uid")
    LiveData<List<Assessment>> getAssessmentsListLiveData();

    @Transaction
    @Query("SELECT * FROM question INNER JOIN choices ON pk_questions_uid = fk_question_uid")
    List<Assessment> getAllAssessmentsList();

    @Query("SELECT * FROM question")
    List<Question> getAllQuestion();

    @Query("SELECT * FROM question ORDER BY RANDOM()")
    List<Question> getShuffledQuestions();

    @Query("SELECT * FROM Choices WHERE fk_question_uid=:pk_uid_question")
    List<Choices> getAllChoicesByUID(long pk_uid_question);

    @Query("SELECT * FROM Answer")
    LiveData<List<Answer>> getAllAnswerListLiveData();

    @Query("SELECT * FROM Answer")
    List<Answer> getAllAnswerList();

    @Query("SELECT COUNT(fk_question_uid) FROM Answer WHERE fk_question_uid=:uid")
    long doesAnswerExist(long uid);

    @Query("SELECT * FROM Answer WHERE fk_question_uid=:uid")
    Answer getAnswerByFkQuestionUID(long uid);

    @Update
    void updateQuestion(Question question);

    @Update
    void updateChoice(Choices choices);

    @Update
    void updateAnswer(Answer... answers);

    @Insert
    void insertAnswer(Answer... answers);

    @Delete
    void deleteQuestion(Question question);

    @Delete
    void deleteChoice(Choices choices);
}
