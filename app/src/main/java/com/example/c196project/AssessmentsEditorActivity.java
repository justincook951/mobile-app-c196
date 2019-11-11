package com.example.c196project;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.example.c196project.database.course.CourseEntity;
import com.example.c196project.utilities.Standardizer;
import com.example.c196project.viewmodel.assessment.AssessmentEditorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Const.KEY_EDIT;
import static com.example.c196project.utilities.Const.ASSESSMENT_ID;

public class AssessmentsEditorActivity extends AppCompatActivity
{

    @BindView(R.id.assessment_name)
    TextView assessmentNameField;
    @BindView(R.id.end_date_replaceable)
    TextView endDateText;
    @BindView(R.id.related_course_dropdown)
    Spinner relatedCourseDropdown;

    Button startBtn, endBtn;
    DatePickerDialog datePicker;
    Date endDate;
    Calendar cal;
    CheckBox setAlarm;
    private boolean isInEdit;
    private AssessmentEditorViewModel assessmentEditorViewModel;
    private static int existingCourseId;

    @Override
    protected void onCreate(Bundle prevState)
    {
        super.onCreate(prevState);
        setContentView(R.layout.activity_assessments_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startBtn = findViewById(R.id.start_date_button);
        endBtn = findViewById(R.id.end_date_button);
        setAlarm = findViewById(R.id.set_alarm);

        endBtn.setOnClickListener(v -> showCalendar(endDateText, "end"));

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

        datePicker = new DatePickerDialog(AssessmentsEditorActivity.this,
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
        assessmentEditorViewModel = ViewModelProviders.of(this).get(AssessmentEditorViewModel.class);
        assessmentEditorViewModel.mutableAssessment.observe(this, assessmentEntity -> {
            if (assessmentEntity != null && !isInEdit) {
                // Do not overwrite any existing changes
                assessmentNameField.setText(assessmentEntity.getTitle());
                endDate = assessmentEntity.getEndDate();
                endDateText.setText(Standardizer.standardizeSingleDateString(endDate));
                existingCourseId = assessmentEntity.getCourseId();
            }
        });

        assessmentEditorViewModel.liveDataCourses.observe(this, courseData -> {
            if (courseData != null) {
                ArrayAdapter coursesAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, courseData);
                relatedCourseDropdown.setAdapter(coursesAdapter);
                existingCourseId = assessmentEditorViewModel.courseId;
                if (existingCourseId != 0) {
                    for (int dropdownIndex = 0; dropdownIndex < courseData.size(); dropdownIndex++) {
                        if (courseData.get(dropdownIndex).getId() == existingCourseId) {
                            relatedCourseDropdown.setSelection(dropdownIndex);
                            break;
                        }
                    }
                }
            }
        });

        Bundle kvExtras = getIntent().getExtras();
        if (kvExtras == null) {
            // This is a brand new assessment
        }
        else {
            // This is a assessment that's being edited
            int assessmentId = kvExtras.getInt(ASSESSMENT_ID);
            assessmentEditorViewModel.loadById(assessmentId);
            existingCourseId = assessmentEditorViewModel.courseId;
            final FloatingActionButton delButton = findViewById(R.id.delete_assessment);
            delButton.setOnClickListener(v -> deleteAssessment());
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
        String assessmentName = assessmentNameField.getText().toString();
        if (endDate == null) {
            // Do not even try to reference saveAssessment
            finish();
        }
        int courseId = ((CourseEntity)relatedCourseDropdown.getSelectedItem()).getId();
        assessmentEditorViewModel.saveAssessment(assessmentName, endDate, courseId, setAlarm.isChecked());
        finish();
    }

    public void deleteAssessment() {
        assessmentEditorViewModel.deleteAssessment();
        finish();
    }

    private void setDate(Date date, String dateLocation)
    {
        if (dateLocation == "end"){
            this.endDate = date;
        }
    }
}
