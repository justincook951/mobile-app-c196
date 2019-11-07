package com.example.c196project.database.assessment;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "assessment")
public class AssessmentEntity
{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private Date endDate;

    @Ignore
    public AssessmentEntity() {
    }

    public AssessmentEntity(int id, String title, Date endDate)
    {
        this.id = id;
        this.title = title;
        this.endDate = endDate;
    }

    @Ignore
    public AssessmentEntity(String title, Date endDate) {
        this.title = title;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString()
    {
        return "CourseEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", endDate=" + endDate +
                '}';
    }
}
