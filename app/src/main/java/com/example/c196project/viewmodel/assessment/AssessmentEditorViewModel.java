package com.example.c196project.viewmodel.assessment;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.assessment.AssessmentEntity;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AssessmentEditorViewModel extends AndroidViewModel
{

    public MutableLiveData<AssessmentEntity> mutableAssessment = new MutableLiveData<>();
    private AppRepository appRepository;
    private Executor executor = Executors.newSingleThreadExecutor();
    private static boolean isValidInput = true;

    public AssessmentEditorViewModel(@NonNull Application application)
    {
        super(application);
        appRepository = AppRepository.getInstance(getApplication());
    }

    public void loadById(int assessmentId)
    {
        // Not a Livedata object; wrap in executor to handle thread
        executor.execute(() -> {
            AssessmentEntity assessment = appRepository.getAssessmentById(assessmentId);
            // Triggers observer's onChange method
            mutableAssessment.postValue(assessment);
        });
    }

    public void saveAssessment(String assessmentTitle, Date endDate)
    {
        isValidInput = true;
        AssessmentEntity selectedAssessment = mutableAssessment.getValue();
        if (selectedAssessment == null) {
            // Saving a new assessment
            if (endDate == null) {
                return;
            }
            selectedAssessment = new AssessmentEntity(assessmentTitle, endDate);
        }
        else {
            // Editing an existing assessment
            selectedAssessment.setTitle(assessmentTitle);
            selectedAssessment.setEndDate(endDate);
        }
        selectedAssessment = validateAssessment(selectedAssessment);
        if (isValidInput) {
            appRepository.insertAssessment(selectedAssessment);
        }
    }

    private AssessmentEntity validateAssessment(AssessmentEntity selectedAssessment)
    {
        String strippedString = (selectedAssessment.getTitle()).trim();
        if (TextUtils.isEmpty(strippedString)) {
            isValidInput = false;
        }
        Date endDate = selectedAssessment.getEndDate();
        if (endDate == null) {
            isValidInput = false;
        }
        selectedAssessment.setTitle(strippedString);
        return selectedAssessment;
    }

    public void deleteAssessment()
    {
        appRepository.deleteAssessment(mutableAssessment.getValue());
    }
}