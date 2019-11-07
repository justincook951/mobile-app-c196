package com.example.c196project.database.mentor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.c196project.database.mentor.MentorEntity;

import java.util.List;

@Dao
public interface MentorDao
{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMentor(MentorEntity mentorEntity);

    @Delete
    void deleteMentor(MentorEntity mentorEntity);

    @Query("SELECT * FROM mentor WHERE id = :id")
    MentorEntity getMentorByid(int id);

    @Query("SELECT * FROM mentor")
    LiveData<List<MentorEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MentorEntity> mentors);

    @Query("DELETE FROM mentor")
    int deleteAll();
}
