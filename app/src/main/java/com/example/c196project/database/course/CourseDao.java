package com.example.c196project.database.course;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CourseDao
{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourse(CourseEntity courseEntity);

    @Delete
    void deleteCourse(CourseEntity courseEntity);

    @Query("SELECT * FROM course WHERE id = :id")
    CourseEntity getCourseByid(int id);

    @Query("SELECT * FROM course ORDER BY startDate")
    LiveData<List<CourseEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CourseEntity> courses);

    @Query("DELETE FROM course")
    int deleteAll();
}
