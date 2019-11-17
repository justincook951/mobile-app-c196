package com.example.c196project.viewmodel.mentor;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.mentor.MentorEntity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MentorEditorViewModel extends AndroidViewModel
{

    public MutableLiveData<MentorEntity> mutableMentor = new MutableLiveData<>();
    private AppRepository appRepository;
    private Executor executor = Executors.newSingleThreadExecutor();
    private static boolean isValidInput = true;

    public MentorEditorViewModel(@NonNull Application application)
    {
        super(application);
        appRepository = AppRepository.getInstance(getApplication());
    }

    public void loadById(int mentorId)
    {
        // Not a Livedata object; wrap in executor to handle thread
        executor.execute(() -> {
            MentorEntity mentor = appRepository.getMentorById(mentorId);
            // Triggers observer's onChange method
            mutableMentor.postValue(mentor);
        });
    }

    public void saveMentor(String mentorName, String mentorPhone, String mentorEmail)
    {
        isValidInput = true;
        MentorEntity selectedMentor = mutableMentor.getValue();
        if (selectedMentor == null) {
            // Saving a new mentor
            selectedMentor = new MentorEntity(mentorName, mentorPhone, mentorEmail);
        }
        else {
            // Editing an existing mentor
            selectedMentor.setName(mentorName);
            selectedMentor.setPhone(mentorPhone);
            selectedMentor.setEmail(mentorEmail);
        }
        selectedMentor = validateMentor(selectedMentor);
        if (isValidInput) {
            appRepository.insertMentor(selectedMentor);
        }
    }

    private MentorEntity validateMentor(MentorEntity selectedMentor)
    {
        String strippedString = (selectedMentor.getName()).trim();
        if (TextUtils.isEmpty(strippedString)) {
            isValidInput = false;
        }
        selectedMentor.setName(strippedString);
        strippedString = (selectedMentor.getPhone()).trim();
        if (TextUtils.isEmpty(strippedString)) {
            isValidInput = false;
        }
        selectedMentor.setPhone(strippedString);
        strippedString = (selectedMentor.getEmail()).trim();
        if (TextUtils.isEmpty(strippedString)) {
            isValidInput = false;
        }
        selectedMentor.setEmail(strippedString);
        return selectedMentor;
    }

    public void deleteMentor()
    {
        appRepository.deleteMentor(mutableMentor.getValue());
    }
}
