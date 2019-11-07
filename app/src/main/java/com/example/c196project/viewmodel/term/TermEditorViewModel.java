package com.example.c196project.viewmodel.term;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.term.TermEntity;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TermEditorViewModel extends AndroidViewModel
{

    public MutableLiveData<TermEntity> mutableTerm = new MutableLiveData<>();
    private AppRepository appRepository;
    private Executor executor = Executors.newSingleThreadExecutor();
    private static boolean isValidInput = true;

    public TermEditorViewModel(@NonNull Application application)
    {
        super(application);
        appRepository = AppRepository.getInstance(getApplication());
    }

    public void loadById(int termId)
    {
        // Not a Livedata object; wrap in executor to handle thread
        executor.execute(() -> {
            TermEntity term = appRepository.getTermById(termId);
            // Triggers observer's onChange method
            mutableTerm.postValue(term);
        });
    }

    public void saveTerm(String termTitle, Date startDate, Date endDate)
    {
        isValidInput = true;
        TermEntity selectedTerm = mutableTerm.getValue();
        if (selectedTerm == null) {
            // Saving a new term
            if (startDate == null || endDate == null) {
                return;
            }
            selectedTerm = new TermEntity(termTitle, startDate, endDate);
        }
        else {
            // Editing an existing term
            selectedTerm.setTitle(termTitle);
            selectedTerm.setStartDate(startDate);
            selectedTerm.setEndDate(endDate);
        }
        selectedTerm = validateTerm(selectedTerm);
        if (isValidInput) {
            appRepository.insertTerm(selectedTerm);
        }
    }

    private TermEntity validateTerm(TermEntity selectedTerm)
    {
        String strippedString = (selectedTerm.getTitle()).trim();
        if (TextUtils.isEmpty(strippedString)) {
            isValidInput = false;
        }
        Date startDate = selectedTerm.getStartDate();
        Date endDate = selectedTerm.getEndDate();
        if (startDate == null || endDate == null) {
            isValidInput = false;
        }
        if (startDate.after(endDate)) {
            isValidInput = false;
        }
        selectedTerm.setTitle(strippedString);
        return selectedTerm;
    }

    public void deleteTerm()
    {
        appRepository.deleteTerm(mutableTerm.getValue());
    }
}
