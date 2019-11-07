package com.example.c196project.viewmodel.coursenote;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.coursenote.CourseNoteEntity;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseNoteEditorViewModel extends AndroidViewModel
{

    public MutableLiveData<CourseNoteEntity> mutableCourseNote = new MutableLiveData<>();
    private AppRepository appRepository;
    private Executor executor = Executors.newSingleThreadExecutor();
    private static boolean isValidInput = true;

    public CourseNoteEditorViewModel(@NonNull Application application)
    {
        super(application);
        appRepository = AppRepository.getInstance(getApplication());
    }

    public void loadById(int courseNoteId)
    {
        // Not a Livedata object; wrap in executor to handle thread
        executor.execute(() -> {
            CourseNoteEntity courseNote = appRepository.getCourseNoteByid(courseNoteId);
            // Triggers observer's onChange method
            mutableCourseNote.postValue(courseNote);
        });
    }

    public void saveCourseNote(String courseNote, int courseId)
    {
        Log.i("MethodCalled", "Called SaveCourseNote");
        isValidInput = true;
        CourseNoteEntity selectedCourseNote = mutableCourseNote.getValue();
        if (selectedCourseNote == null) {
            // Saving a new courseNote
            Log.i("MethodCalled", "Saving new note");
            selectedCourseNote = new CourseNoteEntity(courseId, courseNote);
        }
        else {
            // Editing an existing courseNote
            selectedCourseNote.setCourseNoteText(courseNote);
        }
        selectedCourseNote = validateCourseNote(selectedCourseNote);
        if (isValidInput) {
            appRepository.insertCourseNote(selectedCourseNote);
        }
    }

    private CourseNoteEntity validateCourseNote(CourseNoteEntity selectedCourseNote)
    {
        String strippedString = (selectedCourseNote.getCourseNoteText()).trim();
        if (TextUtils.isEmpty(strippedString)) {
            isValidInput = false;
        }
        selectedCourseNote.setCourseNoteText(strippedString);
        return selectedCourseNote;
    }

    public void deleteCourseNote()
    {
        appRepository.deleteCourseNote(mutableCourseNote.getValue());
    }
}