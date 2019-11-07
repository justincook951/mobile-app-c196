package com.example.c196project.database.term;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TermDao
{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTerm(TermEntity termEntity);

    @Delete
    void deleteTerm(TermEntity termEntity);

    @Query("SELECT * FROM term WHERE id = :id")
    TermEntity getTermByid(int id);

    @Query("SELECT * FROM term ORDER BY startDate")
    LiveData<List<TermEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TermEntity> terms);

    @Query("DELETE FROM term")
    int deleteAll();
}
