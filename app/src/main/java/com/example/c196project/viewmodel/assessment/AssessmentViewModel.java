package com.example.c196project.viewmodel.assessment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.assessment.AssessmentEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AssessmentViewModel extends AndroidViewModel
{

    public LiveData<List<AssessmentEntity>> liveDataAssessments;
    private AppRepository appRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public AssessmentViewModel(@NonNull Application application)
    {
        super(application);
        appRepository = AppRepository.getInstance(application.getApplicationContext());
        liveDataAssessments = appRepository.appAssessments;
    }

}
