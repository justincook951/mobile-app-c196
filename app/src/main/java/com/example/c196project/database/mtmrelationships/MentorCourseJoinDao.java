package com.example.c196project.database.mtmrelationships;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.c196project.database.assessment.AssessmentEntity;
import com.example.c196project.database.course.CourseEntity;
import com.example.c196project.database.mentor.MentorEntity;

import java.util.List;

@Dao
public interface MentorCourseJoinDao
{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMentorCourseJoin(MentorCourseJoinEntity mentorCourseJoinEntity);

    @Delete
    void deleteMentorCourseJoin(MentorCourseJoinEntity mentorCourseJoinEntity);

    @Query("SELECT * FROM course " +
            "INNER JOIN mentorToCourse " +
            "ON course.id = mentorToCourse.courseId " +
            "WHERE mentorToCourse.mentorId = :mentorId")
    List<CourseEntity> getAllCoursesInMentor(final int mentorId);

    @Query("SELECT count(*) AS count FROM course " +
            "INNER JOIN mentorToCourse " +
            "ON course.id = mentorToCourse.courseId " +
            "WHERE mentorToCourse.mentorId = :mentorId")
    int getCountCoursesInMentors(final int mentorId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MentorCourseJoinEntity> mentorCourseJoins);

    @Query("DELETE FROM mentorToCourse")
    int deleteAll();

    @Query("SELECT * FROM mentorToCourse WHERE mentorId = :mentorId AND courseId = :courseId")
    MentorCourseJoinEntity getEntityByValues(int mentorId, int courseId);

    @Query("SELECT * FROM mentor " +
            "INNER JOIN mentorToCourse " +
            "ON mentor.id = mentorToCourse.mentorId " +
            "WHERE mentorToCourse.courseId = :courseId")
    List<MentorEntity> getMentorsByCourseId(int courseId);
}
