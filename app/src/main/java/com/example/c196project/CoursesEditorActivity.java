package com.example.c196project;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.c196project.utilities.Standardizer;
import com.example.c196project.viewmodel.course.CourseEditorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Const.KEY_EDIT;
import static com.example.c196project.utilities.Const.COURSE_ID;

public class CoursesEditorActivity extends AppCompatActivity
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
    private boolean isInEdit;
    private CourseEditorViewModel courseEditorViewModel;

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

        ((TextView)findViewById(R.id.assessment_nav_textview)).setText(R.string.assessments_by_course_tv);

        courseStatusSpinner = findViewById(R.id.course_status_dropdown);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.courses_dropdown_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        courseStatusSpinner.setAdapter(adapter);

        if (prevState != null) {
            isInEdit = prevState.getBoolean(KEY_EDIT);
        }
        ButterKnife.bind(this);
        initViewModel();
    }

    private void showCalendar(TextView displayLocation, String dateToSet)
    {
        cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        datePicker = new DatePickerDialog(CoursesEditorActivity.this,
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
        courseEditorViewModel.saveCourse(courseName, selectedStatus, startDate, endDate);
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

    public void navToCourseNote(View view)
    {

    }


}
