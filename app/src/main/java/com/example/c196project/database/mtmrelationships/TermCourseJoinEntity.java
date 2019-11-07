package com.example.c196project.database.mtmrelationships;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.example.c196project.database.course.CourseEntity;
import com.example.c196project.database.term.TermEntity;

@Entity(tableName = "termToCourse",
        primaryKeys = {"termId", "courseId"},
        foreignKeys = {
                @ForeignKey(entity = TermEntity.class,
                        parentColumns = "id",
                        childColumns = "termId"),
                @ForeignKey(entity = CourseEntity.class,
                        parentColumns = "id",
                        childColumns = "courseId")
        }
)
public class TermCourseJoinEntity
{
    private int termId;
    private int courseId;

    public TermCourseJoinEntity(int termId, int courseId)
    {
        this.termId = termId;
        this.courseId = courseId;
    }

    public int getTermId()
    {
        return termId;
    }

    public void setTermId(int termId)
    {
        this.termId = termId;
    }

    public int getCourseId()
    {
        return courseId;
    }

    public void setCourseId(int courseId)
    {
        this.courseId = courseId;
    }

    @Override
    public String toString()
    {
        return "TermCourseJoinEntity{" +
                "termId=" + termId +
                ", courseId=" + courseId +
                '}';
    }
}
