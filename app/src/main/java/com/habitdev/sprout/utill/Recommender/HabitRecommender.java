package com.habitdev.sprout.utill.Recommender;

import android.util.Log;

import com.habitdev.sprout.database.assessment.model.Assessment;
import com.habitdev.sprout.database.assessment.AssessmentViewModel;
import com.habitdev.sprout.database.assessment.model.Answer;
import com.habitdev.sprout.database.assessment.model.Choices;
import com.habitdev.sprout.database.assessment.model.Question;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.ui.onBoarding.personalizationAssessment.adapter.Model.Result;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that is used to recommend a habit based on the questions provided.
 * A knowledge-based recommender which follower a rule-base algorithm using assessment tool for determining what habit will likely be recommended.
 * The class uses the 'habitScores' map to keep track of the scores for each habit.
 */
public class HabitRecommender {

    private final List<HashMap<Long, Result>> habitScore = new ArrayList<>();
    private AssessmentViewModel assessmentViewModel;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;

    public HabitRecommender() {
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

            //Check for not flag of 1 and -1
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

                //positive flag
                if (question.getClassification() == 1) {
                    //The value is already defined on Frequency enum, so no need for different calculation for diferent
                    //Never: 100%
                    //Seldom: 70%
                    //Rarely: 60%
                    //Occasionally: 50%
                    //Sometimes: 40%
                    //Often: 30%
                    //Usually: 20%
                    //Regularly: 10%
                    //Always: 0%

                    //for positive question
                }

                //negative flag
                if (question.getClassification() == -1) {
                    //The value is already defined on Frequency enum, so no need for different calculation for defirent flag
                    //Never: 0%
                    //Seldom: 10%
                    //Rarely: 20%
                    //Occasionally: 30%
                    //Sometimes: 40%
                    //Often: 50%
                    //Usually: 60%
                    //Regularly: 70%
                    //Always: 100%

                    //for negative Question
                }

                boolean doesContainHabitUID = false;
                if (!habitScore.isEmpty()) {
                    for (HashMap<Long, Result> habit_score : habitScore) {
                        //check if a habit uid of same uid is add
                        if (habit_score.containsKey(habitUid)) {
                            //if added just adds up the new habit_score
                            Result item = habit_score.get(habitUid);
                            item.setScore(item.getScore() + value);
                            item.setTotal_count((int) (item.getTotal_count() + 1));
                            habit_score.put(habitUid, item);
                            doesContainHabitUID = true;
                        }
                    }
                }

                //Identifies which of assessment question is not a filler
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
     * Returns the recommended habit based on the highest score calculated.
     */
    public void getRecommendedHabitsScore() {
        for (HashMap<Long, Result> map : habitScore) {
            for (Map.Entry<Long, Result> entry : map.entrySet()) {
                Result result = entry.getValue();
                String habit_title = habitWithSubroutinesViewModel.getHabitByUID(result.getHabit_uid()).getHabit();
                Log.d("tag", "Habit, " + entry.getKey() + "-> " + habit_title + " , Result: " + new DecimalFormat("##%").format(result.getScore()) + ", Total Item: " + result.getTotal_count());
            }
        }
    }

    public List<HashMap<Long, Result>> getHabitScore() {
        return habitScore;
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