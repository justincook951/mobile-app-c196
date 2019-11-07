package com.example.c196project.database.course;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "course")
public class CourseEntity
{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String status;
    private Date startDate;
    private Date endDate;

    @Ignore
    public CourseEntity() {
    }

    public CourseEntity(int id, String title, String status, Date startDate, Date endDate)
    {
        this.id = id;
        this.title = title;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Ignore
    public CourseEntity(String title, String status, Date startDate, Date endDate) {
        this.title = title;
        this.status = status;
        this.startDate = startDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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
                ", status='" + status + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
