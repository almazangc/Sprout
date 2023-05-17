package com.habitdev.sprout.utill.diffutils;

import androidx.recyclerview.widget.DiffUtil;

import com.habitdev.sprout.database.assessment.model.AssessmentRecord;
import com.habitdev.sprout.database.quotes.model.Quotes;

import java.util.List;
import java.util.Objects;

public class AssessmentRecordDiffUtil extends DiffUtil.Callback {

    private final List<AssessmentRecord> oldAssessmentRecordList;
    private final List<AssessmentRecord> newAssessmentRecordList;

    public AssessmentRecordDiffUtil(List<AssessmentRecord> oldAssessmentRecordList, List<AssessmentRecord> newAssessmentRecordList) {
        this.oldAssessmentRecordList = oldAssessmentRecordList;
        this.newAssessmentRecordList = newAssessmentRecordList;
    }

    @Override
    public int getOldListSize() {
        return oldAssessmentRecordList == null ? 0 : oldAssessmentRecordList.size();
    }

    @Override
    public int getNewListSize() {
        return newAssessmentRecordList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(oldAssessmentRecordList.get(oldItemPosition).getPk_assessment_record_uid(), newAssessmentRecordList.get(newItemPosition).getPk_assessment_record_uid());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        AssessmentRecord oldQuotes = oldAssessmentRecordList.get(oldItemPosition);
        AssessmentRecord newQuotes = newAssessmentRecordList.get(newItemPosition);
        return oldQuotes.getPk_assessment_record_uid() == (newQuotes.getPk_assessment_record_uid()) &&
                oldQuotes.isCompleted() == (newQuotes.isCompleted());
    }
}
