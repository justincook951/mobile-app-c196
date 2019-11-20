package com.example.c196project.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.c196project.database.assessment.AssessmentDao;
import com.example.c196project.database.assessment.AssessmentEntity;
import com.example.c196project.database.course.CourseDao;
import com.example.c196project.database.course.CourseEntity;
import com.example.c196project.database.coursenote.CourseNoteDao;
import com.example.c196project.database.coursenote.CourseNoteEntity;
import com.example.c196project.database.mentor.MentorDao;
import com.example.c196project.database.mentor.MentorEntity;
import com.example.c196project.database.mtmrelationships.MentorCourseJoinDao;
import com.example.c196project.database.mtmrelationships.MentorCourseJoinEntity;
import com.example.c196project.database.mtmrelationships.TermCourseJoinDao;
import com.example.c196project.database.mtmrelationships.TermCourseJoinEntity;
import com.example.c196project.database.term.TermDao;
import com.example.c196project.database.term.TermEntity;

@Database(entities =
        {
            TermEntity.class,
            CourseEntity.class,
            AssessmentEntity.class,
            MentorEntity.class,
            CourseNoteEntity.class,
            TermCourseJoinEntity.class,
            MentorCourseJoinEntity.class
        },
        version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)

public abstract class AppDatabase extends RoomDatabase
{

    public static final String DATABASE_NAME = "AppDatabase.db";
    private static volatile AppDatabase instance;
    private static final Object LOCK = new Object();

    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();
    public abstract MentorDao mentorDao();
    public abstract CourseNoteDao courseNoteDao();
    public abstract TermCourseJoinDao termCourseJoinDao();
    public abstract MentorCourseJoinDao mentorCourseJoinDao();

    public static AppDatabase getInstance(Context context)
    {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, DATABASE_NAME).build();
                }
            }
        }

        return instance;
    }

}
