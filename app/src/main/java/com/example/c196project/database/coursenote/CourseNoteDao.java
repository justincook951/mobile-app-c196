package com.example.c196project.database.coursenote;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CourseNoteDao
{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourseNote(CourseNoteEntity courseNoteEntity);

    @Delete
    void deleteCourseNote(CourseNoteEntity courseNoteEntity);

    @Query("SELECT * FROM courseNote WHERE id = :id")
    CourseNoteEntity getCourseNoteByid(int id);

    @Query("SELECT * FROM courseNote")
    LiveData<List<CourseNoteEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CourseNoteEntity> courseNotes);

    @Query("DELETE FROM courseNote")
    int deleteAll();
}
