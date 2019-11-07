package com.example.c196project.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.term.TermEntity;

import java.util.List;

public class MainViewModel extends AndroidViewModel
{
    public LiveData<List<TermEntity>> termEntities;
    private AppRepository appRepository;

    public MainViewModel(@NonNull Application application)
    {
        super(application);

        appRepository = AppRepository.getInstance(application.getApplicationContext());
        termEntities = appRepository.appTerms;
    }

    public void deleteAllTerms()
    {
        appRepository.deleteAllTerms();
    }

    public void addSampleTerms()
    {
        appRepository.addSampleTerms();
    }

    public void addSampleCourses()
    {
        appRepository.addSampleCourses();
    }

    public void deleteAllCourses()
    {
        appRepository.deleteAllCourses();
    }

    public void addSampleAssessments()
    {
        appRepository.addSampleAssessments();
    }

    public void deleteAllAssessments()
    {
        appRepository.deleteAllAssessments();
    }

    public void deleteAllMentors()
    {
        appRepository.deleteAllMentors();
    }

    public void addSampleMentors()
    {
        appRepository.addSampleMentors();
    }
}
