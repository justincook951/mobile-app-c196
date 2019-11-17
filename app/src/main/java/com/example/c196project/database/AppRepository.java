package com.example.c196project.database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.c196project.database.assessment.AssessmentEntity;
import com.example.c196project.database.course.CourseEntity;
import com.example.c196project.database.coursenote.CourseNoteEntity;
import com.example.c196project.database.mentor.MentorEntity;
import com.example.c196project.database.mtmrelationships.TermCourseJoinEntity;
import com.example.c196project.database.term.TermEntity;
import com.example.c196project.utilities.TestData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository
{

    private static AppRepository existingInstance;
    private AppDatabase appDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    public LiveData<List<TermEntity>> appTerms;
    public LiveData<List<CourseEntity>> appCourses;
    public LiveData<List<AssessmentEntity>> appAssessments;
    public LiveData<List<MentorEntity>> appMentors;
    public LiveData<List<CourseNoteEntity>> appNotes;
    public LiveData<List<AssessmentEntity>> assessmentsByCourses;
    private int courseCount;
    private int courseToTermCount;
    private boolean courseToTermCountSet;

    public static AppRepository getInstance(Context context)
    {
        if (existingInstance == null) {
            existingInstance = new AppRepository(context);
        }
        return existingInstance;
    }

    private AppRepository(Context context)
    {
        appDb = AppDatabase.getInstance(context);
        appTerms = getAllTerms();
        appCourses = getAllCourses();
        appAssessments = getAllAssessments();
        appMentors = getAllMentors();
        appNotes = getAllNotes();

        executor.execute(() -> courseCount = appDb.courseDao().getCount());
        courseToTermCountSet = false;
    }

    private LiveData<List<TermEntity>> getAllTerms()
    {
        return appDb.termDao().getAll();
    }

    public void addSampleTerms()
    {
        executor.execute(() -> appDb.termDao().insertAll(TestData.getTerms()));
    }

    public void deleteAllTerms()
    {
        executor.execute(() -> appDb.termDao().deleteAll());
    }

    public TermEntity getTermById(int termId)
    {
        return appDb.termDao().getTermByid(termId);
    }

    public void insertTerm(TermEntity term)
    {
        executor.execute(() -> appDb.termDao().insertTerm(term));
    }

    public void deleteTerm(TermEntity term)
    {
        if (getCourseToTermCount(term) == 0) {
            executor.execute(() -> appDb.termDao().deleteTerm(term));
        }
    }

    // =======COURSES==========

    private LiveData<List<CourseEntity>> getAllCourses()
    {
        return appDb.courseDao().getAll();
    }

    public void addSampleCourses()
    {
        executor.execute(() -> appDb.courseDao().insertAll(TestData.getCourses()));
    }

    public void deleteAllCourses()
    {
        executor.execute(() -> appDb.courseDao().deleteAll());

    }

    public CourseEntity getCourseById(int courseId)
    {
        return appDb.courseDao().getCourseByid(courseId);
    }

    public void insertCourse(CourseEntity course)
    {
        executor.execute(() -> appDb.courseDao().insertCourse(course));
    }

    public void deleteCourse(CourseEntity course)
    {
        executor.execute(() -> appDb.courseDao().deleteCourse(course));
    }

    public int getCoursesCount()
    {
        return courseCount;
    }

    // =======ASSESSMENTS==========

    private LiveData<List<AssessmentEntity>> getAllAssessments()
    {
        return appDb.assessmentDao().getAll();
    }

    public void addSampleAssessments()
    {
        executor.execute(() -> appDb.assessmentDao().insertAll(TestData.getAssessments()));
    }

    public void deleteAllAssessments()
    {
        executor.execute(() -> appDb.assessmentDao().deleteAll());
    }

    public List<AssessmentEntity> getAssessmentsByCourseId(int courseId)
    {
        Log.i("MethodCalled", "Calling getAssessmentsByCourseId");
        return appDb.assessmentDao().getAssessmentsByCourseId(courseId);
    }

    public AssessmentEntity getAssessmentById(int assessmentId)
    {
        return appDb.assessmentDao().getAssessmentByid(assessmentId);
    }

    public void insertAssessment(AssessmentEntity assessment)
    {
        executor.execute(() -> appDb.assessmentDao().insertAssessment(assessment));
    }

    public void deleteAssessment(AssessmentEntity assessment)
    {
        executor.execute(() -> appDb.assessmentDao().deleteAssessment(assessment));
    }

    // =======MENTORS==========

    private LiveData<List<MentorEntity>> getAllMentors()
    {
        return appDb.mentorDao().getAll();
    }

    public void addSampleMentors()
    {
        executor.execute(() -> appDb.mentorDao().insertAll(TestData.getMentors()));
    }

    public void deleteAllMentors()
    {
        executor.execute(() -> appDb.mentorDao().deleteAll());

    }

    public MentorEntity getMentorById(int mentorId)
    {
        return appDb.mentorDao().getMentorByid(mentorId);
    }

    public void insertMentor(MentorEntity mentor)
    {
        executor.execute(() -> appDb.mentorDao().insertMentor(mentor));
    }

    public void deleteMentor(MentorEntity mentor)
    {
        executor.execute(() -> appDb.mentorDao().deleteMentor(mentor));
    }

    // =======NOTES==========

    private LiveData<List<CourseNoteEntity>> getAllNotes()
    {
        return appDb.courseNoteDao().getAll();
    }

    public void addSampleNotes()
    {
        executor.execute(() -> appDb.courseNoteDao().insertAll(TestData.getCourseNotes()));
    }

    public void deleteAllNotes()
    {
        executor.execute(() -> appDb.courseNoteDao().deleteAll());

    }

    public CourseNoteEntity getCourseNoteByid(int noteId)
    {
        return appDb.courseNoteDao().getCourseNoteByid(noteId);
    }

    public void insertCourseNote(CourseNoteEntity note)
    {
        executor.execute(() -> appDb.courseNoteDao().insertCourseNote(note));
    }

    public void deleteCourseNote(CourseNoteEntity note)
    {
        executor.execute(() -> appDb.courseNoteDao().deleteCourseNote(note));
    }

    // =======MANY-TO-MANY UTILITIES==========

    public int getCourseToTermCount(TermEntity termEntity)
    {
        courseToTermCountSet = false;
        int increment = 0;
        executor.execute(() -> {
            courseToTermCount = appDb.termCourseJoinDao().getCountCoursesInTerms(termEntity.getId());
            courseToTermCountSet = true;
        });
        while (courseToTermCountSet == false && increment < 1000) {
            // Wait
            increment++;
        }
        return courseToTermCount;
    }

    public List<CourseEntity> getCoursesByTerm(int termId)
    {
        return appDb.termCourseJoinDao().getAllCoursesInTerm(termId);
    }

    public void addTermToCourse(TermCourseJoinEntity joiner)
    {
        executor.execute(() -> appDb.termCourseJoinDao().insertTermCourseJoin(joiner));
    }

    public void deleteTermCourseRelationship(int termId, int courseId)
    {
        executor.execute(() -> {
            TermCourseJoinEntity tcc = appDb.termCourseJoinDao().getEntityByValues(termId, courseId);
            appDb.termCourseJoinDao().deleteTermCourseJoin(tcc);
        });
    }
}
