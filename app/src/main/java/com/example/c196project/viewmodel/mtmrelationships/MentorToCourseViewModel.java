package com.example.c196project.viewmodel.mtmrelationships;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.course.CourseEntity;
import com.example.c196project.database.mtmrelationships.MentorCourseJoinEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MentorToCourseViewModel extends AndroidViewModel
{

    public LiveData<List<CourseEntity>> liveDataCourses;
    private AppRepository appRepository;
    private Executor executor = Executors.newSingleThreadExecutor();
    private List<CourseEntity> coursesByMentor;

    public MentorToCourseViewModel(@NonNull Application application)
    {
        super(application);
        appRepository = AppRepository.getInstance(getApplication());
        liveDataCourses = appRepository.appCourses;
    }

    public void saveRelationship(int mentorId, int courseId)
    {
        MentorCourseJoinEntity joiner = new MentorCourseJoinEntity(mentorId, courseId);
        appRepository.addMentorToCourse(joiner);
    }

    public void deleteRelationship(int mentorId, int courseId)
    {
        appRepository.deleteMentorCourseRelationship(mentorId, courseId);
    }

    public List<CourseEntity> getMatchedEntries(int mentorId)
    {
        if (coursesByMentor == null) {
            executor.execute(() -> coursesByMentor = appRepository.getCoursesByMentor(mentorId));
        }
        return coursesByMentor;
    }
}
