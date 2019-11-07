package com.example.c196project.viewmodel.mentor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.mentor.MentorEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MentorViewModel extends AndroidViewModel
{

    public LiveData<List<MentorEntity>> liveDataMentors;
    private AppRepository appRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public MentorViewModel(@NonNull Application application)
    {
        super(application);
        appRepository = AppRepository.getInstance(application.getApplicationContext());
        liveDataMentors = appRepository.appMentors;
    }

}
