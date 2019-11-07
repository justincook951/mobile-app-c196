package com.example.c196project.database.coursenote;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "courseNote")
public class CourseNoteEntity
{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int courseId;
    private String courseNoteText;

    public CourseNoteEntity(int id, int courseId, String courseNoteText)
    {
        this.id = id;
        this.courseId = courseId;
        this.courseNoteText = courseNoteText;
    }

    @Override
    public String toString()
    {
        return "CourseNoteEntity{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", courseNoteText='" + courseNoteText + '\'' +
                '}';
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    @Ignore
    public CourseNoteEntity(int courseId, String courseNoteText)
    {
        this.courseId = courseId;
        this.courseNoteText = courseNoteText;
    }

    public int getCourseId()
    {
        return courseId;
    }

    public void setCourseId(int courseId)
    {
        this.courseId = courseId;
    }

    public String getCourseNoteText()
    {
        return courseNoteText;
    }

    public void setCourseNoteText(String courseNoteText)
    {
        this.courseNoteText = courseNoteText;
    }
}
