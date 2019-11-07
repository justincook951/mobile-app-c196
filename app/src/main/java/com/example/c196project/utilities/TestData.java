package com.example.c196project.utilities;

import com.example.c196project.database.assessment.AssessmentEntity;
import com.example.c196project.database.course.CourseEntity;
import com.example.c196project.database.coursenote.CourseNoteEntity;
import com.example.c196project.database.mentor.MentorEntity;
import com.example.c196project.database.term.TermEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TestData
{

    private static Date getTestDate(int dayVal, int monthVal, int yearVal)
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setLenient(false);
        gc.set(GregorianCalendar.YEAR, yearVal);
        gc.set(GregorianCalendar.MONTH, monthVal);
        gc.set(GregorianCalendar.DATE, dayVal);
        return gc.getTime();
    }

    public static List<TermEntity> getTerms()
    {
        List<TermEntity> terms = new ArrayList<>();
        terms.add(new TermEntity("Test Term 1", getTestDate(1, 1, 2017), getTestDate(1,6, 2017)));
        terms.add(new TermEntity("Test Term 2", getTestDate(1, 5, 2018), getTestDate(1, 11, 2018)));
        return terms;
    }

    public static List<CourseEntity> getCourses()
    {
        List<CourseEntity> courses = new ArrayList<>();
        courses.add(new CourseEntity("Test Course 1", "Active", getTestDate(1, 1, 2017), getTestDate(1,6, 2017)));
        courses.add(new CourseEntity("Test Course 2", "Not Passed", getTestDate(1, 5, 2018), getTestDate(1, 11, 2018)));
        return courses;
    }

    public static List<AssessmentEntity> getAssessments()
    {
        List<AssessmentEntity> assessments = new ArrayList<>();
        assessments.add(new AssessmentEntity("Test Assessment 1",  getTestDate(1, 1, 2017)));
        assessments.add(new AssessmentEntity("Test Assessment 2", getTestDate(1, 5, 2018)));
        return assessments;
    }

    public static List<MentorEntity> getMentors()
    {
        List<MentorEntity> mentors = new ArrayList<>();
        mentors.add(new MentorEntity("Test Mentor1",  "A phone number", "An email"));
        mentors.add(new MentorEntity("Test Mentor2", "555-555-5555", "testyBoi@wgu.edu"));
        return mentors;
    }

    public static List<CourseNoteEntity> getCourseNotes()
    {
        List<CourseNoteEntity> courseNotes = new ArrayList<>();
        courseNotes.add(new CourseNoteEntity(1, "Sample text for Course ID 1"));
        courseNotes.add(new CourseNoteEntity(1, "Sample text #2 for Course ID 1"));
        courseNotes.add(new CourseNoteEntity(2, "Sample text for Course ID 2"));
        return courseNotes;
    }
}
