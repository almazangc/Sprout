package com.habitdev.sprout.utill.recommender;

import android.util.Log;

import com.habitdev.sprout.database.habit.firestore.HabitFireStoreRepository;
import com.habitdev.sprout.database.habit.firestore.HabitFireStoreViewModel;
import com.habitdev.sprout.database.habit.firestore.SubroutineFireStoreRepository;
import com.habitdev.sprout.database.habit.model.firestore.HabitFireStore;
import com.habitdev.sprout.database.habit.model.firestore.SubroutineFireStore;

import java.util.ArrayList;
import java.util.List;

public class PopularityBased {

    private static List<HabitFireStore> habits;
    private static List<SubroutineFireStore> subroutines;

    /**
     * m The constant prior value for minimum vote
     */
    private static final int M = 5;

    /**
     * 95% confidence interval with a margin of error 5%
     */
    private static final double z = 1.96;

    private static HabitFireStoreViewModel habitFireStoreViewModel;

    public PopularityBased() {
        PopularityBased.habits = getHabitFireStoreList();
        PopularityBased.subroutines = getSubroutineFireStoreList();
    }

    public static void setHabits(List<HabitFireStore> habits) {
        PopularityBased.habits = habits;
    }

    public static void setSubroutines(List<SubroutineFireStore> subroutines) {
        PopularityBased.subroutines = subroutines;
    }

    public void setHabitFireStoreViewModel(HabitFireStoreViewModel habitFireStoreViewModel) {
        PopularityBased.habitFireStoreViewModel = habitFireStoreViewModel;
    }

    public static void calculateRating() {
        if (checkItems()) {
            Log.d("tag", "calculateRating: failed to calculate rating");
            return;
        }

        for (HabitFireStore habit : habits) {
            if (habit.getUpvote() + habit.getDownvote() < M){
                habit.setRating(0);
            } else {
                // Calculate Wilson score lower bound for the habit
                double pHat = (double) habit.getUpvote() / (habit.getUpvote() + habit.getDownvote());
                int n = habit.getUpvote() + habit.getDownvote();
                double habitLowerBound = (pHat + z * z / (2 * n) - z * Math.sqrt((pHat * (1 - pHat) + z * z / (4 * n)) / n))
                        / (1 + z * z / n);

                // Calculate Wilson score lower bound for each of the habit's associated subroutines
                double subroutinesLowerBoundSum = 0.0;
                for (SubroutineFireStore subroutine : subroutines) {
                    if (habit.getPk_uid() == subroutine.getFk_habit_uid()) {

                        double pHatSub = (double) subroutine.getUpvote() / (subroutine.getUpvote() + subroutine.getDownvote());
                        int nSub = subroutine.getUpvote() + subroutine.getDownvote();

                        double subroutineLowerBound = (pHatSub + z * z / (2 * nSub) - z * Math.sqrt((pHatSub * (1 - pHatSub) + z * z / (4 * nSub)) / nSub))
                                / (1 + z * z / nSub);

                        subroutinesLowerBoundSum += subroutineLowerBound;
                    }
                }

                // Calculate the overall rating for the habit, taking into account its upvote, downvote, and subroutines' lower bounds
                double totalLowerBound = habitLowerBound + subroutinesLowerBoundSum;
                double totalVotes = habit.getUpvote() + habit.getDownvote();
                double rating = totalLowerBound / totalVotes;
                habit.setRating(rating);
            }
            habitFireStoreViewModel.updateHabit(habit);
        }
    }

    public static List<HabitFireStore> getHabitFireStoreList() {
        HabitFireStoreRepository habitFireStoreRepository = new HabitFireStoreRepository();
        final List<HabitFireStore>[] habits = new List[]{new ArrayList<>()};
        habitFireStoreRepository.fetchData(new HabitFireStoreRepository.FetchCallback() {
            @Override
            public void onFetchHabitSuccess(List<HabitFireStore> habitFireStoreList) {
                habits[0] = habitFireStoreList;
                Log.d("tag", "onFetchHabitSuccess: ");
            }

            @Override
            public void onFetchHabitFailure(Exception e) {
                Log.e("tag", "onFetchHabitFailure: " + e.getMessage());
            }
        });
        return habits[0];
    }

    public static List<SubroutineFireStore> getSubroutineFireStoreList() {
        SubroutineFireStoreRepository subroutineFireStoreRepository = new SubroutineFireStoreRepository();
        final List<SubroutineFireStore>[] subroutines = new List[]{new ArrayList<>()};
        subroutineFireStoreRepository.fetchData(new SubroutineFireStoreRepository.FetchCallback() {
            @Override
            public void onFetchSubroutineSuccess(List<SubroutineFireStore> subroutineFireStores) {
                subroutines[0] = subroutineFireStores;
                Log.d("tag", "onFetchHabitSuccess: ");
            }

            @Override
            public void onFetchSubroutineFailure(Exception e) {
                Log.e("tag", "onFetchHabitFailure: " + e.getMessage());
            }
        });
        return subroutines[0];
    }

    private static boolean checkItems() {
        if (habits == null) {
            Log.d("tag", "habits: item is null");
            return true;
        }

        if (habits.isEmpty()) {
            Log.d("tag", "habits: item is empty");
            return true;
        }

        if (subroutines == null) {
            Log.d("tag", "subroutines: item is null");
            return true;
        }

        if (subroutines.isEmpty()) {
            Log.d("tag", "subroutines: item is empty");
            return true;
        }
        return false;
    }
}