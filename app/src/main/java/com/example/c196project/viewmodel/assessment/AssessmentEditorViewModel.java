package com.example.c196project.viewmodel.assessment;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.assessment.AssessmentEntity;
import com.example.c196project.database.course.CourseEntity;
import com.example.c196project.utilities.WGUNotificationMgr;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AssessmentEditorViewModel extends AndroidViewModel
{

    public MutableLiveData<AssessmentEntity> mutableAssessment = new MutableLiveData<>();
    public int totalCoursesCount;
    private AppRepository appRepository;
    private Executor executor = Executors.newSingleThreadExecutor();
    private static boolean isValidInput = true;
    public LiveData<List<CourseEntity>> liveDataCourses;
    public int courseId;
    public static WGUNotificationMgr notifymgr;

    public AssessmentEditorViewModel(@NonNull Application application)
    {
        super(application);
        appRepository = AppRepository.getInstance(getApplication());
        liveDataCourses = appRepository.appCourses;
        totalCoursesCount = appRepository.getCoursesCount();
    }

    public void loadById(int assessmentId)
    {
        // Not a Livedata object; wrap in executor to handle thread
        executor.execute(() -> {
            AssessmentEntity assessment = appRepository.getAssessmentById(assessmentId);
            // Triggers observer's onChange method
            mutableAssessment.postValue(assessment);
            courseId = assessment.getCourseId();
            appRepository.getAssessmentsByCourseId(courseId);
        });
    }

    public void saveAssessment(String assessmentTitle, Date endDate, int courseId, String assessmentType, boolean setAlarm)
    {
        isValidInput = true;
        AssessmentEntity selectedAssessment = mutableAssessment.getValue();
        if (selectedAssessment == null) {
            // Saving a new assessment
            if (endDate == null) {
                return;
            }
            selectedAssessment = new AssessmentEntity(assessmentTitle, endDate, courseId, assessmentType);
        }
        else {
            // Editing an existing assessment
            selectedAssessment.setTitle(assessmentTitle);
            selectedAssessment.setEndDate(endDate);
            selectedAssessment.setCourseId(courseId);
            selectedAssessment.setAssessmentType(assessmentType);
        }
        selectedAssessment = validateAssessment(selectedAssessment);
        if (isValidInput) {
            appRepository.insertAssessment(selectedAssessment);
            mutableAssessment.postValue(selectedAssessment);
            if (setAlarm) {
                if (notifymgr == null) {
                    notifymgr = new WGUNotificationMgr();
                }
                notifymgr.setAlarm(endDate, "Your assessment is today: " + assessmentTitle + "!", this.getApplication().getApplicationContext());
            }
        }
    }

    private AssessmentEntity validateAssessment(AssessmentEntity selectedAssessment)
    {
        String strippedString = (selectedAssessment.getTitle()).trim();
        if (TextUtils.isEmpty(strippedString)) {
            isValidInput = false;
        }
        Date endDate = selectedAssessment.getEndDate();
        if (endDate == null) {
            isValidInput = false;
        }
        selectedAssessment.setTitle(strippedString);
        return selectedAssessment;
    }

    public void deleteAssessment()
    {
        appRepository.deleteAssessment(mutableAssessment.getValue());
    }

}
