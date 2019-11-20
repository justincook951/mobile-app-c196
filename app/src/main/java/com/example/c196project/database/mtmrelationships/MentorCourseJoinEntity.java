package com.example.c196project.database.mtmrelationships;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.example.c196project.database.course.CourseEntity;
import com.example.c196project.database.mentor.MentorEntity;

@Entity(tableName = "mentorToCourse",
        primaryKeys = {"mentorId", "courseId"},
        foreignKeys = {
                @ForeignKey(entity = MentorEntity.class,
                        parentColumns = "id",
                        childColumns = "mentorId"),
                @ForeignKey(entity = CourseEntity.class,
                        parentColumns = "id",
                        childColumns = "courseId")
        }
)
public class MentorCourseJoinEntity
{
    private int mentorId;
    private int courseId;

    public MentorCourseJoinEntity(int mentorId, int courseId)
    {
        this.mentorId = mentorId;
        this.courseId = courseId;
    }

    public int getMentorId()
    {
        return mentorId;
    }

    public void setMentorId(int mentorId)
    {
        this.mentorId = mentorId;
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
        return "MentorCourseJoinEntity{" +
                "mentorId=" + mentorId +
                ", courseId=" + courseId +
                '}';
    }
}
