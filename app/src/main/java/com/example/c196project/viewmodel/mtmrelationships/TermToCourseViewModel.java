package com.example.c196project.viewmodel.mtmrelationships;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.course.CourseEntity;
import com.example.c196project.database.mtmrelationships.TermCourseJoinEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TermToCourseViewModel extends AndroidViewModel
{

    public MutableLiveData<TermCourseJoinEntity> mutableRelationship = new MutableLiveData<>();
    public LiveData<List<CourseEntity>> liveDataCourses;
    private AppRepository appRepository;
    private Executor executor = Executors.newSingleThreadExecutor();
    private static boolean isValidInput = true;

    public TermToCourseViewModel(@NonNull Application application)
    {
        super(application);
        appRepository = AppRepository.getInstance(getApplication());
        liveDataCourses = appRepository.appCourses;
    }

    public void saveRelationship(int termId, int courseId)
    {
        TermCourseJoinEntity joiner = new TermCourseJoinEntity(termId, courseId);
        appRepository.addTermToCourse(joiner);
    }

    public void deleteRelationship()
    {
        appRepository.deleteCourseNote(mutableRelationship.getValue());
    }
}