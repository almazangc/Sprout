package com.habitdev.sprout.utill.recommender;

import android.util.Log;

import com.habitdev.sprout.database.assessment.model.Assessment;
import com.habitdev.sprout.database.assessment.AssessmentViewModel;
import com.habitdev.sprout.database.assessment.model.Answer;
import com.habitdev.sprout.database.assessment.model.Choices;
import com.habitdev.sprout.database.assessment.model.Question;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.ui.habit_assessment.adapter.Model.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that is used to recommend a habit based on the questions provided.
 * A knowledge-based recommender following a rule-base approach using assessment tool for determining what habit will likely be recommended.
 * The class uses the 'habitScores' map to keep track of the scores for each habit.
 */
public class KnowledgeBased {

    private final List<HashMap<Long, Result>> habitScore = new ArrayList<>();
    private AssessmentViewModel assessmentViewModel;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;

    public KnowledgeBased() {
    }

    public void setAssessmentViewModel(AssessmentViewModel assessmentViewModel) {
        this.assessmentViewModel = assessmentViewModel;
    }

    public void setHabitWithSubroutinesViewModel(HabitWithSubroutinesViewModel habitWithSubroutinesViewModel) {
        this.habitWithSubroutinesViewModel = habitWithSubroutinesViewModel;
    }

    /**
     * Calculates the scores for each habit based on the answers provided.
     * The higher the score, the more likely the habit is recommended.
     */
    public void calculateHabitScores() {
        List<Question> questionList = assessmentViewModel.getAllQuestionList();
        List<Answer> answerList = assessmentViewModel.getAllAnswerList();

        List<Assessment> assessmentList = new ArrayList<>();

        for (Question question : questionList) {
            long uid = question.getPk_question_uid();
            List<Choices> choices = assessmentViewModel.getAllChoicesByUID(uid);
            assessmentList.add(new Assessment(question, choices));
        }

        for (Assessment assessment : assessmentList) {
            Question question = assessment.getQuestion();
            List<Choices> choices = assessment.getChoices();

            //Check for flag of 1 and -1
            if (question.getClassification() != 0) {
                long habitUid = question.getFk_habit_uid();

                double value = 0;
                for (Answer answer : answerList) {
                    if (answer.getFk_question_uid() == question.getPk_question_uid()) {
                        for (Choices choice : choices) {
                            if (choice.getChoices().equals(answer.getSelected_answer())) {
                                value = choice.getValue();
                            }
                        }
                    }
                }

                boolean doesContainHabitUID = false;

                if (!habitScore.isEmpty()) {
                    for (HashMap<Long, Result> habit_score : habitScore) {
                        if (habit_score.containsKey(habitUid)) {
                            Result item = habit_score.get(habitUid);
                            item.setScore(item.getScore() + value);
                            item.setTotal_count((int) (item.getTotal_count() + 1));
                            habit_score.put(habitUid, item);
                            doesContainHabitUID = true;
                        }
                    }
                }

                if (!doesContainHabitUID) {
                    HashMap<Long, Result> hashMap = new HashMap<>();
                    Result item = new Result(habitUid, value, 1);
                    hashMap.put(habitUid, item);
                    habitScore.add(hashMap);
                }
            }
        }

        for (HashMap<Long, Result> map : habitScore) {
            for (Map.Entry<Long, Result> entry : map.entrySet()) {
                Result result = entry.getValue();
                result.setScore(((double) Math.round((result.getScore() / result.getTotal_count()) * 100)) / 100);
                map.put(entry.getKey(), result);
            }
        }
    }

    /**
     * Returns the recommended habit based on the score calculated.
     */
    public void getRecommendedHabitsScore() {
        for (HashMap<Long, Result> map : habitScore) {
            for (Map.Entry<Long, Result> entry : map.entrySet()) {
                Result result = entry.getValue();
                String habit_title = habitWithSubroutinesViewModel.getHabitByUID(result.getHabit_uid()).getHabit();
                Log.d("tag", "Habit, " + entry.getKey() + "-> " + habit_title + " , Result: " + result.getFormattedConfidenceScore() + ", Total Item: " + result.getTotal_count());
            }
        }
    }

    public List<Result> getConvertedToResultList() {
        List<Result> resultList = new ArrayList<>();
        for (HashMap<Long, Result> map : habitScore) {
            for (Map.Entry<Long, Result> entry : map.entrySet()) {
                resultList.add(entry.getValue());
            }
        }
        return resultList;
    }
}