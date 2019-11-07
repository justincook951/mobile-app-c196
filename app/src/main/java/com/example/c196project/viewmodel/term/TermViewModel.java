package com.example.c196project.viewmodel.term;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.term.TermEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TermViewModel extends AndroidViewModel
{

    public LiveData<List<TermEntity>> liveDataTerms;
    private AppRepository appRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public TermViewModel(@NonNull Application application)
    {
        super(application);
        appRepository = AppRepository.getInstance(application.getApplicationContext());
        liveDataTerms = appRepository.appTerms;
    }

}
