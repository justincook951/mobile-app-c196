package com.example.c196project.viewmodel.coursenote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.coursenote.CourseNoteEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseNoteViewModel extends AndroidViewModel
{

    public LiveData<List<CourseNoteEntity>> liveDataCourseNotes;
    private AppRepository appRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public CourseNoteViewModel(@NonNull Application application)
    {
        super(application);
        appRepository = AppRepository.getInstance(application.getApplicationContext());
        liveDataCourseNotes = appRepository.appNotes;
    }

}
