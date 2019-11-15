package com.example.c196project.viewmodel.course;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.course.CourseEntity;
import com.example.c196project.utilities.WGUNotificationMgr;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseEditorViewModel extends AndroidViewModel
{

    public MutableLiveData<CourseEntity> mutableCourse = new MutableLiveData<>();
    private AppRepository appRepository;
    private Executor executor = Executors.newSingleThreadExecutor();
    private static boolean isValidInput = true;
    private static WGUNotificationMgr notifymgr;

    public CourseEditorViewModel(@NonNull Application application)
    {
        super(application);
        appRepository = AppRepository.getInstance(getApplication());
    }

    public void loadById(int courseId)
    {
        // Not a Livedata object; wrap in executor to handle thread
        executor.execute(() -> {
            CourseEntity course = appRepository.getCourseById(courseId);
            // Triggers observer's onChange method
            mutableCourse.postValue(course);
        });
    }

    public void saveCourse(String courseTitle, String status, Date startDate, Date endDate, boolean setAlarm)
    {
        isValidInput = true;
        CourseEntity selectedCourse = mutableCourse.getValue();
        if (selectedCourse == null) {
            // Saving a new course
            if (startDate == null || endDate == null) {
                return;
            }
            selectedCourse = new CourseEntity(courseTitle, status, startDate, endDate);
        }
        else {
            // Editing an existing course
            selectedCourse.setTitle(courseTitle);
            selectedCourse.setStartDate(startDate);
            selectedCourse.setEndDate(endDate);
            selectedCourse.setStatus(status);
        }
        selectedCourse = validateCourse(selectedCourse);
        if (isValidInput) {
            appRepository.insertCourse(selectedCourse);
            if (setAlarm) {
                if (notifymgr == null) {
                    notifymgr = new WGUNotificationMgr();
                }
                notifymgr.setAlarm(startDate, "Your course is set to start today: " + courseTitle + "!", this.getApplication().getApplicationContext());
                notifymgr.setAlarm(endDate, "Your course is set to end today: " + courseTitle + "!", this.getApplication().getApplicationContext());
            }
        }
    }

    private CourseEntity validateCourse(CourseEntity selectedCourse)
    {
        String strippedString = (selectedCourse.getTitle()).trim();
        if (TextUtils.isEmpty(strippedString)) {
            isValidInput = false;
        }
        Date startDate = selectedCourse.getStartDate();
        Date endDate = selectedCourse.getEndDate();
        if (startDate == null || endDate == null) {
            isValidInput = false;
        }
        if (startDate.after(endDate)) {
            isValidInput = false;
        }
        selectedCourse.setTitle(strippedString);
        return selectedCourse;
    }

    public void deleteCourse()
    {
        appRepository.deleteCourse(mutableCourse.getValue());
    }
}
