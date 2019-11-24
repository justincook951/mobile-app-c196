package com.example.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.example.c196project.database.assessment.AssessmentEntity;
import com.example.c196project.database.mentor.MentorEntity;
import com.example.c196project.utilities.Standardizer;
import com.example.c196project.viewmodel.course.CourseEditorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Const.COURSE_ID;
import static com.example.c196project.utilities.Const.KEY_EDIT;
import static com.example.c196project.utilities.Const.SAVE_EDITING;

public class CourseEditorActivity extends AppCompatActivity
{

    @BindView(R.id.course_name)
    TextView courseNameField;
    @BindView(R.id.start_date_replaceable)
    TextView startDateText;
    @BindView(R.id.end_date_replaceable)
    TextView endDateText;
    @BindView(R.id.course_status_dropdown)
    Spinner courseStatusSpinner;

    Button startBtn, endBtn;
    DatePickerDialog datePicker;
    Date startDate, endDate;
    Calendar cal;
    Bundle kvExtras;
    CheckBox setAlarmStart, setAlarmEnd;
    private boolean isInEdit;
    private CourseEditorViewModel courseEditorViewModel;
    private TextView assessmentsTextview, mentorsTextview;
    private List<AssessmentEntity> assignedAssessments;
    private List<MentorEntity> assignedMentors;

    @Override
    protected void onCreate(Bundle prevState)
    {
        super.onCreate(prevState);
        setContentView(R.layout.activity_courses_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        kvExtras = getIntent().getExtras();
        startBtn = findViewById(R.id.start_date_button);
        endBtn = findViewById(R.id.end_date_button);

        startBtn.setOnClickListener(v -> showCalendar(startDateText, "start"));
        endBtn.setOnClickListener(v -> showCalendar(endDateText, "end"));
        setAlarmStart = findViewById(R.id.set_alarm_start_date);
        setAlarmEnd = findViewById(R.id.set_alarm_end_date);

        assessmentsTextview = findViewById(R.id.assessmentChangeableTv);
        mentorsTextview = findViewById(R.id.mentor_changeable_tv);

        courseStatusSpinner = findViewById(R.id.course_status_dropdown);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.courses_dropdown_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        courseStatusSpinner.setAdapter(adapter);

        ButterKnife.bind(this);

        if (prevState != null) {
            isInEdit = prevState.getBoolean(SAVE_EDITING);
        }
        initViewModel();

    }

    private void showCalendar(TextView displayLocation, String dateToSet)
    {
        cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        datePicker = new DatePickerDialog(CourseEditorActivity.this,
                    (view, selYear, selMonth, selDay) -> {
                        displayLocation.setText(Standardizer.dateStringFromComponents(selYear, selMonth, selDay));
                        setDate(Standardizer.dateFromComponents(selYear, selMonth, selDay), dateToSet);
                    },
                    year,
                    month,
                    day);
        datePicker.show();
    }

    private void initViewModel()
    {
        courseEditorViewModel = ViewModelProviders.of(this).get(CourseEditorViewModel.class);
        courseEditorViewModel.mutableCourse.observe(this, courseEntity -> {
            if (courseEntity != null && !isInEdit) {
                // Do not overwrite any existing changes
                courseNameField.setText(courseEntity.getTitle());
                startDate = courseEntity.getStartDate();
                endDate = courseEntity.getEndDate();
                startDateText.setText(Standardizer.standardizeSingleDateString(startDate));
                endDateText.setText(Standardizer.standardizeSingleDateString(endDate));
            }
        });

        courseEditorViewModel.relatedAssessments.observe(this, assessmentEntities -> {
            assignedAssessments = courseEditorViewModel.relatedAssessments.getValue();
            String displayAssessmentsList = "";
            if (assignedAssessments != null) {
                for (AssessmentEntity assent : assignedAssessments) {
                    displayAssessmentsList += assent.getTitle() + ", ";
                }
            }
            if (displayAssessmentsList.isEmpty()) {
                displayAssessmentsList = "No assessments assigned to this course yet.";
            }
            else {
                displayAssessmentsList = displayAssessmentsList.substring(0, displayAssessmentsList.length() - 2);
            }
            assessmentsTextview.setText(displayAssessmentsList);
        });

        courseEditorViewModel.relatedMentors.observe(this, mentorEntities -> {
            assignedMentors = courseEditorViewModel.relatedMentors.getValue();
            String displayMentorsList = "";
            if (assignedMentors != null) {
                for (MentorEntity mentor : assignedMentors) {
                    displayMentorsList += mentor.getName() + ": Email - " + mentor.getEmail() + " / Phone - " + mentor.getPhone() + "\n";
                }
            }
            if (displayMentorsList.isEmpty()) {
                displayMentorsList = "No mentors assigned to this course yet.";
            }
            mentorsTextview.setText(displayMentorsList);
        });

        if (kvExtras == null) {
            // This is a brand new course
        }
        else {
            // This is a course that's being edited
            int courseId = kvExtras.getInt(COURSE_ID);
            courseEditorViewModel.loadById(courseId);
            final FloatingActionButton delButton = findViewById(R.id.delete_course);
            delButton.setOnClickListener(v -> deleteCourse());
            final FloatingActionButton navToCourseNote = findViewById(R.id.nav_to_course_note);
            navToCourseNote.setOnClickListener(v -> {
                Intent noteIntent = new Intent(this, CourseNotesListActivity.class);
                noteIntent.putExtra(COURSE_ID, courseId);
                startActivity(noteIntent);
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            saveAndExit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveAndExit();
    }

    private void saveAndExit() {
        String courseName = courseNameField.getText().toString();
        if (startDate == null || endDate == null) {
            // Do not even try to reference them in saveCourse
            finish();
        }
        String selectedStatus = courseStatusSpinner.getSelectedItem().toString();
        courseEditorViewModel.saveCourse(courseName, selectedStatus, startDate, endDate, setAlarmStart.isChecked(), setAlarmEnd.isChecked());
        finish();
    }

    public void deleteCourse() {
        courseEditorViewModel.deleteCourse();
        finish();
    }

    private void setDate(Date date, String dateLocation)
    {
        if (dateLocation == "start") {
            this.startDate = date;
        }
        else if (dateLocation == "end"){
            this.endDate = date;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putBoolean(SAVE_EDITING, true);
        super.onSaveInstanceState(outState);
    }
}
