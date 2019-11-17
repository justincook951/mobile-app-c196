package com.example.c196project.viewmodel.course;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.course.CourseEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseViewModel extends AndroidViewModel
{

    public LiveData<List<CourseEntity>> liveDataCourses;
    private AppRepository appRepository;
    private Executor executor = Executors.newSingleThreadExecutor();
    private List<CourseEntity> coursesByTerm;

    public CourseViewModel(@NonNull Application application)
    {
        super(application);
        appRepository = AppRepository.getInstance(application.getApplicationContext());
        liveDataCourses = appRepository.appCourses;
    }

    public List<? extends CourseEntity> getCoursesByTerm(int termId)
    {
        if (coursesByTerm == null) {
            executor.execute(() -> coursesByTerm = appRepository.getCoursesByTerm(termId));
        }
        return coursesByTerm;

    }
}
