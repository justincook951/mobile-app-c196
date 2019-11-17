package com.example.c196project.database.mtmrelationships;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.c196project.database.course.CourseEntity;

import java.util.List;

@Dao
public interface TermCourseJoinDao
{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTermCourseJoin(TermCourseJoinEntity termCourseJoinEntity);

    @Delete
    void deleteTermCourseJoin(TermCourseJoinEntity termCourseJoinEntity);

    @Query("SELECT * FROM course " +
            "INNER JOIN termToCourse " +
            "ON course.id = termToCourse.courseId " +
            "WHERE termToCourse.termId = :termId")
    List<CourseEntity> getAllCoursesInTerm(final int termId);

    @Query("SELECT count(*) AS count FROM course " +
            "INNER JOIN termToCourse " +
            "ON course.id = termToCourse.courseId " +
            "WHERE termToCourse.termId = :termId")
    int getCountCoursesInTerms(final int termId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TermCourseJoinEntity> termCourseJoins);

    @Query("DELETE FROM termToCourse")
    int deleteAll();

    @Query("SELECT * FROM termToCourse WHERE termId = :termId AND courseId = :courseId")
    TermCourseJoinEntity getEntityByValues(int termId, int courseId);
}
