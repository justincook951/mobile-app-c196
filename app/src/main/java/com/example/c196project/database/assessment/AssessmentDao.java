package com.example.c196project.database.assessment;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.c196project.database.assessment.AssessmentEntity;

import java.util.List;

@Dao
public interface AssessmentDao
{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssessment(AssessmentEntity assessmentEntity);

    @Delete
    void deleteAssessment(AssessmentEntity assessmentEntity);

    @Query("SELECT * FROM assessment WHERE id = :id")
    AssessmentEntity getAssessmentByid(int id);

    @Query("SELECT * FROM assessment ORDER BY endDate")
    LiveData<List<AssessmentEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AssessmentEntity> assessments);

    @Query("DELETE FROM assessment")
    int deleteAll();
}
